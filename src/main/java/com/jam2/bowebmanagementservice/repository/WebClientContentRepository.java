package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.WebClientContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WebClientContentRepository extends JpaRepository<WebClientContent, UUID> {

    WebClientContent findByUserWebId(UUID authId);

    WebClientContent findByWebClientId(UUID webClientId);

    WebClientContent findByWebClientIdAndSectionStatus(UUID webClientId, Integer sectionStatus);

}
