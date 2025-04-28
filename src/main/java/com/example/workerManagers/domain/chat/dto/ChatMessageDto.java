package com.example.workerManagers.domain.chat.dto;

import com.example.workerManagers.domain.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;

    @Builder
    public ChatMessageDto(Long id, Long chatRoomId, Long senderId, String senderName, String content, 
                         LocalDateTime sentAt, boolean isRead) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getUserId())
                .senderName(chatMessage.getSender().getUserName())
                .content(chatMessage.getContent())
                .sentAt(chatMessage.getSentAt())
                .isRead(chatMessage.isRead())
                .build();
    }
} 