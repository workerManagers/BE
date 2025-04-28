package com.example.workerManagers.domain.chat.repository;

import com.example.workerManagers.domain.chat.entity.ChatRoom;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsByJobPostJobPostIdAndApplicant(Long jobPostId, User applicant);
    Optional<ChatRoom> findByJobPostJobPostIdAndApplicant(Long jobPostId, User applicant);
    List<ChatRoom> findByApplicantOrRecruiter(User applicant, User recruiter);
    List<ChatRoom> findByRecruiter(User recruiter);
} 