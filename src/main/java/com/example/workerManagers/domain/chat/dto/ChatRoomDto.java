package com.example.workerManagers.domain.chat.dto;

import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.chat.entity.ChatMessage;
import com.example.workerManagers.domain.users.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Builder
public class ChatRoomDto {
    private Long id;
    private String user1Name;
    private String user1Email;
    private String user2Name;
    private String user2Email;
    private LocalDateTime createdAt;
    private int unreadCount;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatRoomDto from(ChatRoom chatRoom, User currentUser) {
        // 마지막 메시지 정보 가져오기
        ChatMessage lastChatMessage = chatRoom.getMessages().stream()
                .max(Comparator.comparing(ChatMessage::getSentAt))
                .orElse(null);

        // 읽지 않은 메시지 수 계산 (상대방이 보낸 메시지 중 읽지 않은 것만 카운트)
        long unreadCount = chatRoom.getMessages().stream()
                .filter(message -> !message.isRead() && 
                        !message.getSender().equals(currentUser))
                .count();

        // 현재 사용자가 user1인지 user2인지에 따라 상대방 정보 결정
        User otherUser = currentUser.equals(chatRoom.getUser1()) ? 
                chatRoom.getUser2() : chatRoom.getUser1();

        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .user1Name(chatRoom.getUser1().getUserName())
                .user1Email(chatRoom.getUser1().getUserEmail())
                .user2Name(chatRoom.getUser2().getUserName())
                .user2Email(chatRoom.getUser2().getUserEmail())
                .createdAt(chatRoom.getCreatedAt())
                .unreadCount((int) unreadCount)
                .lastMessage(lastChatMessage != null ? lastChatMessage.getContent() : null)
                .lastMessageTime(lastChatMessage != null ? lastChatMessage.getSentAt() : null)
                .build();
    }
} 