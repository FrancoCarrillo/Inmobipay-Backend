package com.coderly.inmobipay.core.repositories;

import com.coderly.inmobipay.core.entities.GracePeriodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GracePeriodRepository extends JpaRepository<GracePeriodEntity, Long> {
}
