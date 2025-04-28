package com.example.workerManagers.domain.chat.repository;

import com.example.workerManagers.domain.chat.entity.ChatMessage;
import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderBySentAtAsc(ChatRoom chatRoom);
    List<ChatMessage> findByChatRoomAndSenderNotAndIsReadFalse(ChatRoom chatRoom, User sender);
} 