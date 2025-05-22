package org.example.dandd.model.dao;

import org.example.dandd.model.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameEntityDao extends JpaRepository<GameEntity, Long>
{
}
