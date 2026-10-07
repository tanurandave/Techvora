package com.techvora.repository;

import com.techvora.entity.RoadmapNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoadmapNodeRepository extends JpaRepository<RoadmapNode, UUID> {
}
