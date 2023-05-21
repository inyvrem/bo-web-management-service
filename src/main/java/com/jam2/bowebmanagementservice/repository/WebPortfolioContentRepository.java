package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.WebAboutContent;
import com.jam2.bowebmanagementservice.entity.WebPortfolioContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WebPortfolioContentRepository extends JpaRepository<WebPortfolioContent, UUID> {

    WebPortfolioContent findByUserWebId(UUID authId);

    WebPortfolioContent findByWebPortfolioId(UUID webPortfolioId);

    WebPortfolioContent findByWebPortfolioIdAndSectionStatus(UUID webPortfolioId, Integer sectionStatus);



}
