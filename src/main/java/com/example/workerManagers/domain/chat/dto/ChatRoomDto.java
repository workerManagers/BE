package com.example.workerManagers.domain.chat.dto;

import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Builder
public class ChatRoomDto {
    private Long id;
    private Long jobPostId;
    private String jobName;
    private String applicantName;
    private String applicantEmail;
    private String recruiterName;
    private LocalDateTime createdAt;
    private int unreadCount;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        // 마지막 메시지 정보 가져오기
        ChatMessage lastChatMessage = chatRoom.getMessages().stream()
                .max(Comparator.comparing(ChatMessage::getSentAt))
                .orElse(null);

        // 읽지 않은 메시지 수 계산 (지원자가 보낸 메시지 중 읽지 않은 것만 카운트)
        long unreadCount = chatRoom.getMessages().stream()
                .filter(message -> !message.isRead() && !message.getSender().equals(chatRoom.getRecruiter()))
                .count();

        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .jobPostId(chatRoom.getJobPost().getJobPostId())
                .jobName(chatRoom.getJobPost().getJobName())
                .applicantName(chatRoom.getApplicant().getUserName())
                .applicantEmail(chatRoom.getApplicant().getUserEmail())
                .recruiterName(chatRoom.getRecruiter().getUserName())
                .createdAt(chatRoom.getCreatedAt())
                .unreadCount((int) unreadCount)
                .lastMessage(lastChatMessage != null ? lastChatMessage.getContent() : null)
                .lastMessageTime(lastChatMessage != null ? lastChatMessage.getSentAt() : null)
                .build();
    }
} 