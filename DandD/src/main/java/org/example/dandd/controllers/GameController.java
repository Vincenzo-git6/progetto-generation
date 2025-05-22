package org.example.dandd.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dandd.model.dao.MonsterDao;
import org.example.dandd.model.dao.PgPlayableDao;
import org.example.dandd.model.dto.ActionRequest;
import org.example.dandd.model.dto.GameStateDto;
import org.example.dandd.model.dto.PgDto;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.example.dandd.service.BattleService;
import org.example.dandd.service.PlayableService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/start")
@RequiredArgsConstructor
public class GameController
{
	final BattleService battleService;
	final PlayableService playableService;
	final PgPlayableDao pgPlayableDao;
	final MonsterDao monsterDao;


	@GetMapping()
	public List<PgDto> PgOnScreen()
	{
		return playableService.allPgDtos();
	}


	@PostMapping()
	public GameStateDto newGame(@RequestBody List<Long> pgIds)
	{
		List<PgPlayable> pgs = new ArrayList<>();
		for (Long id : pgIds)
			pgs.add(pgPlayableDao.findById(id).orElse(null));

		List<Monster> monsters = monsterDao.findAll();
		Collections.shuffle(monsters);
		monsters=monsters.subList(0,3);

		return battleService.iniziaFight(pgs, monsters);
	}

	@PostMapping("/actionEntity")
	public GameStateDto action(@RequestBody ActionRequest actionRequest)
	{
		return battleService.nextTurn(actionRequest.getPreviousDto(), actionRequest.getTarget(), actionRequest.getActionType());
	}

	@PostMapping("/next")
	public GameStateDto next(@RequestBody GameStateDto currentState)
	{
		return battleService.nextRound(currentState);
	}

	@PostMapping("/battleOver")
	public String battleOver(@RequestBody GameStateDto currentState)
	{
		return battleService.battleOver(currentState);
	}



}
