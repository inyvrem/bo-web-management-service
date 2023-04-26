package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.UserWebRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserWebRelationRepository extends JpaRepository<UserWebRelation, UUID> {

    UserWebRelation findByAuthId(UUID authId);
}
