package org.example.dandd.service;

import org.example.dandd.model.dao.MonsterDao;
import org.example.dandd.model.dto.MonsterDto;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.mapper.MonsterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonsterService
{
	@Autowired
	MonsterDao monsterDao;
	@Autowired
	MonsterMapper monsterMapper;

	public List<MonsterDto> AllMonsterDtos()
	{
		List<Monster> monsters = monsterDao.findAll();
		return monsterMapper.toDtos(monsters);
	}

	public MonsterDto getMonsterDtoById(Long id)
	{
		Monster monster = monsterDao.findById(id).orElse(null);
		if(monster == null)
			return null;
		return monsterMapper.toDto(monster);
	}





}
