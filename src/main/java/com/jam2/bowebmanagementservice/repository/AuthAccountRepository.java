package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccount, UUID> {

    AuthAccount findByAuthId(UUID authId);

}
