package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.SubPortfolioContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubPortfolioContentRepository extends JpaRepository<SubPortfolioContent, UUID> {

    List<SubPortfolioContent> findByWebPortfolioContentWebPortfolioId(UUID webPortfolioId);

    SubPortfolioContent findBySubPortfolioId(UUID subPortfolioId);

    List<SubPortfolioContent> findBySectionStatusAndWebPortfolioContentWebPortfolioId(Integer status, UUID webPortfolioId);
}
