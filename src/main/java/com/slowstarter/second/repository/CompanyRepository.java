package com.slowstarter.second.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slowstarter.second.entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>
{
    boolean existsByName(String name);

    CompanyEntity findByName(String name);
}
