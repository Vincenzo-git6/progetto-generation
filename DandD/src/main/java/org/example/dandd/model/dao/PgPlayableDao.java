package org.example.dandd.model.dao;

import org.example.dandd.model.entities.enums.CharacterType;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PgPlayableDao extends JpaRepository<PgPlayable, Long>
{
	PgPlayable findByName(String name);
}
