package org.example.dandd.controllers;

import org.example.dandd.model.dto.PgDto;
import org.example.dandd.service.PlayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/playables")
public class PlayableController
{
	@Autowired
	PlayableService playableService;

	@GetMapping("/all")
	public List<PgDto> playables()
	{
		return playableService.allPgDtos();
	}

	@GetMapping("/{id}")
	public PgDto getPgById(@PathVariable Long id)
	{
		return playableService.getPgDtoById(id);
	}
}
