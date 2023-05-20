package com.coderly.inmobipay.core.repositories;

import com.coderly.inmobipay.core.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository  extends JpaRepository<BankEntity, Long> {
    Optional<BankEntity> findByName(String name);
    Boolean existsByName(String name);
}
