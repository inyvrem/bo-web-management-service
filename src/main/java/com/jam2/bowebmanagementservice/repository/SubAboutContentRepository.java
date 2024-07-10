package com.jam2.bowebmanagementservice.repository;

import com.jam2.bowebmanagementservice.entity.SubAboutContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubAboutContentRepository extends JpaRepository<SubAboutContent, UUID> {

    List<SubAboutContent> findByWebAboutContentWebAboutId(UUID webAboutId);

    SubAboutContent findBySubAboutId(UUID subAboutId);

    List<SubAboutContent> findBySectionStatusAndWebAboutContentWebAboutId(Integer status, UUID webAboutId);
}
