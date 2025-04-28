package com.example.workerManagers.domain.chat.service;

import com.example.workerManagers.domain.chat.dto.ChatMessageDto;
import com.example.workerManagers.domain.chat.dto.ChatRoomCreateDto;
import com.example.workerManagers.domain.chat.dto.ChatRoomDto;
import com.example.workerManagers.domain.chat.entity.ChatMessage;
import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.chat.exception.ChatException;
import com.example.workerManagers.domain.chat.repository.ChatMessageRepository;
import com.example.workerManagers.domain.chat.repository.ChatRoomRepository;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.jobpost.repository.JobPostRepository;
import com.example.workerManagers.domain.users.entity.User;
import com.example.workerManagers.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;

    public ChatRoomDto createChatRoom(ChatRoomCreateDto createDto, String userEmail) {
        User currentUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));
        
        JobPost jobPost = jobPostRepository.findById(createDto.getJobPostId())
                .orElseThrow(() -> new ChatException("채용공고를 찾을 수 없습니다."));

        // 이미 존재하는 채팅방이 있는지 확인
        ChatRoom existingChatRoom = chatRoomRepository.findByJobPostJobPostIdAndApplicant(createDto.getJobPostId(), currentUser)
                .orElse(null);

        if (existingChatRoom != null) {
            return ChatRoomDto.from(existingChatRoom);
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .jobPost(jobPost)
                .applicant(currentUser)
                .recruiter(jobPost.getCompany().getUser())
                .build();

        return ChatRoomDto.from(chatRoomRepository.save(chatRoom));
    }

    public ChatMessageDto saveAndSendMessage(ChatMessageDto messageDto, String senderEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User sender = userRepository.findByUserEmail(senderEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        validateMessageSending(sender, chatRoom);

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(messageDto.getContent())
                .build();

        return ChatMessageDto.from(chatMessageRepository.save(chatMessage));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDto> getMyChatRooms(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        return chatRoomRepository.findByApplicantOrRecruiter(user, user).stream()
                .map(ChatRoomDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDto> getRecruiterChatRooms(String userEmail) {
        User recruiter = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        return chatRoomRepository.findByRecruiter(recruiter).stream()
                .map(ChatRoomDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatMessages(Long roomId, String userEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        validateChatRoomAccess(user, chatRoom);

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderBySentAtAsc(chatRoom);
        
        if (messages.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        return messages.stream()
                .map(ChatMessageDto::from)
                .collect(Collectors.toList());
    }

    private void validateMessageSending(User user, ChatRoom chatRoom) {
        if (!user.equals(chatRoom.getApplicant()) && !user.equals(chatRoom.getRecruiter())) {
            throw new ChatException("채팅방 참여자만 메시지를 보낼 수 있습니다.");
        }
    }

    private void validateChatRoomAccess(User user, ChatRoom chatRoom) {
        if (!user.equals(chatRoom.getApplicant()) && !user.equals(chatRoom.getRecruiter())) {
            throw new ChatException("채팅방 참여자만 메시지를 조회할 수 있습니다.");
        }
    }

    public void markMessagesAsRead(Long roomId, String userEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        validateChatRoomAccess(user, chatRoom);

        List<ChatMessage> unreadMessages = chatMessageRepository.findByChatRoomAndSenderNotAndIsReadFalse(chatRoom, user);
        unreadMessages.forEach(ChatMessage::markAsRead);
    }

    public void leaveChatRoom(Long roomId, String userEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        validateChatRoomAccess(user, chatRoom);

        // 채팅방에서 사용자 제거
        if (chatRoom.getApplicant().equals(user)) {
            chatRoom.setApplicant(null);
        } else if (chatRoom.getRecruiter().equals(user)) {
            chatRoom.setRecruiter(null);
        }

        // 양쪽 사용자가 모두 나간 경우 채팅방 삭제
        if (chatRoom.getApplicant() == null && chatRoom.getRecruiter() == null) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    public void deleteMessage(Long messageId, String userEmail) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ChatException("메시지를 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if (!message.getSender().equals(user)) {
            throw new ChatException("메시지를 삭제할 권한이 없습니다.");
        }

        chatMessageRepository.delete(message);
    }
} 