package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.WebAboutContent;
import com.jam2.bowebmanagementservice.entity.WebHeroContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WebAboutContentRepository extends JpaRepository<WebAboutContent, UUID> {

    WebAboutContent findByUserWebId(UUID authId);

    WebAboutContent findByWebAboutId(UUID webAboutId);

    WebAboutContent findByWebAboutIdAndSectionStatus(UUID webAboutId, Integer sectionStatus);

}
