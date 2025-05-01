package com.example.workerManagers.domain.chat.service;

import com.example.workerManagers.domain.chat.dto.ChatMessageDto;
import com.example.workerManagers.domain.chat.dto.ChatRoomCreateDto;
import com.example.workerManagers.domain.chat.dto.ChatRoomDto;
import com.example.workerManagers.domain.chat.entity.ChatMessage;
import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.chat.exception.ChatException;
import com.example.workerManagers.domain.chat.repository.ChatMessageRepository;
import com.example.workerManagers.domain.chat.repository.ChatRoomRepository;
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

    public ChatRoomDto createChatRoom(ChatRoomCreateDto createDto, String userEmail) {
        User currentUser = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));
        
        User targetUser = userRepository.findById(createDto.getTargetUserId())
                .orElseThrow(() -> new ChatException("대화 상대를 찾을 수 없습니다."));

        // 이미 존재하는 채팅방이 있는지 확인
        ChatRoom existingChatRoom = chatRoomRepository.findByUsers(currentUser, targetUser)
                .orElse(null);

        if (existingChatRoom != null) {
            return ChatRoomDto.from(existingChatRoom, currentUser);
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .user1(currentUser)
                .user2(targetUser)
                .build();

        return ChatRoomDto.from(chatRoomRepository.save(chatRoom), currentUser);
    }

    public ChatMessageDto saveAndSendMessage(ChatMessageDto messageDto, String senderEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User sender = userRepository.findByUserEmail(senderEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if (!chatRoom.isParticipant(sender)) {
            throw new ChatException("채팅방 참여자만 메시지를 보낼 수 있습니다.");
        }

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

        return chatRoomRepository.findByUser1OrUser2(user, user).stream()
                .map(chatRoom -> ChatRoomDto.from(chatRoom, user))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getChatMessages(Long roomId, String userEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if (!chatRoom.isParticipant(user)) {
            throw new ChatException("채팅방 참여자만 메시지를 조회할 수 있습니다.");
        }

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderBySentAtAsc(chatRoom);
        
        if (messages.isEmpty()) {
            return List.of();
        }

        return messages.stream()
                .map(ChatMessageDto::from)
                .collect(Collectors.toList());
    }

    public void markMessagesAsRead(Long roomId, String userEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if (!chatRoom.isParticipant(user)) {
            throw new ChatException("채팅방 참여자만 메시지를 읽을 수 있습니다.");
        }

        List<ChatMessage> unreadMessages = chatMessageRepository.findByChatRoomAndSenderNotAndIsReadFalse(chatRoom, user);
        unreadMessages.forEach(ChatMessage::markAsRead);
    }

    public void leaveChatRoom(Long roomId, String userEmail) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
        
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if (!chatRoom.isParticipant(user)) {
            throw new ChatException("채팅방 참여자만 나갈 수 있습니다.");
        }

        if (chatRoom.getUser1().equals(user)) {
            chatRoom.setUser1(null);
        } else {
            chatRoom.setUser2(null);
        }

        if (chatRoom.getUser1() == null && chatRoom.getUser2() == null) {
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