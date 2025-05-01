package com.example.workerManagers.domain.chat.repository;

import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr WHERE " +
           "(cr.user1 = :user1 AND cr.user2 = :user2) OR " +
           "(cr.user1 = :user2 AND cr.user2 = :user1)")
    Optional<ChatRoom> findByUsers(@Param("user1") User user1, @Param("user2") User user2);

    List<ChatRoom> findByUser1OrUser2(User user1, User user2);
} 