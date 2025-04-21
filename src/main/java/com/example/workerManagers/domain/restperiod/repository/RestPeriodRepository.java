package com.example.workerManagers.domain.restperiod.repository;

import com.example.workerManagers.domain.restperiod.entity.RestPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestPeriodRepository extends JpaRepository<RestPeriod, Long> {
} 