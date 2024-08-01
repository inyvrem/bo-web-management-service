package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.SubClientContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubClientContentRepository extends JpaRepository<SubClientContent, UUID> {

    List<SubClientContent> findByWebClientContentWebClientId(UUID webClientId);

    SubClientContent findBySubClientId(UUID subClientId);

    List<SubClientContent> findBySectionStatusAndWebClientContentWebClientId(Integer status, UUID webClientId);
}
