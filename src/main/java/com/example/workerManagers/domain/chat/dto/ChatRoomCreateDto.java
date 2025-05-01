package com.example.workerManagers.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateDto {
    private Long targetUserId;  // 채팅을 시작할 상대방 ID
} 