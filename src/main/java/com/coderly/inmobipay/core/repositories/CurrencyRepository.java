package com.coderly.inmobipay.core.repositories;

import com.coderly.inmobipay.core.entities.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Boolean existsByName(String name);
    Optional<CurrencyEntity> findByName(String name);
}
