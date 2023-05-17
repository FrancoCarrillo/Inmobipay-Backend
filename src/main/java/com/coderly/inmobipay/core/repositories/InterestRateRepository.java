package com.coderly.inmobipay.core.repositories;

import com.coderly.inmobipay.core.entities.InterestRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestRateRepository extends JpaRepository<InterestRateEntity, Long> {

    Optional<InterestRateEntity> findByType(String type);
    Boolean existsByType(String type);
}
