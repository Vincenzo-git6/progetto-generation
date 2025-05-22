package org.example.dandd.model.mapper;

import org.example.dandd.model.dto.MonsterDto;
import org.example.dandd.model.dto.PgDto;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonsterMapper
{
	public MonsterDto toDto(Monster monster)
	{
		return new MonsterDto(
				monster.getId(),
				monster.getName(),
				monster.getDescription(),
				monster.getDanger().name(),
				monster.getHp(),
				monster.getAtk(),
				monster.getDef(),
				monster.getSpeed(),
				monster.getActions().stream().map(a->a.getNameAction()).toList(),
				monster.getActions().stream().map(a->a.getDescriptionAction()).toList(),
				monster.getImageUrl()
		);
	}

	public List<MonsterDto> toDtos(List<Monster> monsters)
	{
		return monsters.stream().map(p->toDto(p)).collect(Collectors.toList());
	}
}
