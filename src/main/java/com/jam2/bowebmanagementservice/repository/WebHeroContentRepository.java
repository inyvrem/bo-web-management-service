package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.WebHeroContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WebHeroContentRepository extends JpaRepository<WebHeroContent, UUID> {

    WebHeroContent findByUserWebId(UUID authId);

    WebHeroContent findByWebHeroId(UUID webHeroId);
}
