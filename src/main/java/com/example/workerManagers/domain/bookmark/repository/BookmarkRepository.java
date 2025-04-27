package com.example.workerManagers.domain.bookmark.repository;

import com.example.workerManagers.domain.bookmark.entity.Bookmark;
import com.example.workerManagers.domain.jobpost.entity.JobPost;
import com.example.workerManagers.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
    Optional<Bookmark> findByUserAndJobPost(User user, JobPost jobPost);
    boolean existsByUserAndJobPost(User user, JobPost jobPost);
} 