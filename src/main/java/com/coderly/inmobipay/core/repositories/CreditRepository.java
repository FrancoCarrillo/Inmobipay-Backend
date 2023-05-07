package com.coderly.inmobipay.core.repositories;

import com.coderly.inmobipay.core.entities.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, UUID> {
}
