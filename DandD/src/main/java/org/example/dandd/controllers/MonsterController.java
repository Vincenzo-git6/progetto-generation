package org.example.dandd.controllers;

import org.example.dandd.model.dao.MonsterDao;
import org.example.dandd.model.dto.MonsterDto;
import org.example.dandd.service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monsters")
public class MonsterController
{
	@Autowired
	MonsterService monsterService;

	@GetMapping("/all")
	public List<MonsterDto> monsters()
	{
		return monsterService.AllMonsterDtos();
	}

	@GetMapping("/{id}")
	public MonsterDto getMonsterById(@PathVariable Long id)
	{
		return monsterService.getMonsterDtoById(id);
	}
}
