package org.example.dandd;

import org.example.dandd.model.dao.ActionDao;
import org.example.dandd.model.dao.GameEntityDao;
import org.example.dandd.model.dao.MonsterDao;
import org.example.dandd.model.dao.PgPlayableDao;
import org.example.dandd.model.dto.GameStateDto;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.entities.enums.ActionType;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.example.dandd.service.BattleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TestOrdinamento
{
	@Autowired
	PgPlayableDao pgPlayableDao;

	@Autowired
	MonsterDao monsterDao;

	@Autowired
	BattleService battleService;

	@Autowired
	GameEntityDao gameEntityDao;

	@Autowired
	ActionDao actionDao;


//	@Test
//	public void TestOrdinamento()
//	{
//		List<PgPlayable> player = pgPlayableDao.findAll();
//		List<Monster> monster = monsterDao.findAll();
//		GameStateDto currentDto = battleService.iniziaFight(player, monster);
//
//
//		GameStateDto turn = battleService.nextTurn(currentDto, 6, ActionType.BASE);
//		GameStateDto round = battleService.nextRound(turn);
//		turn=battleService.nextTurn(round, 6, ActionType.BASE);
//		round=battleService.nextRound(turn);
//		turn=battleService.nextTurn(round, 6, ActionType.BASE);
//		round=battleService.nextRound(turn);
//		turn=battleService.nextTurn(round, 6, ActionType.BASE);
//		round=battleService.nextRound(turn);
//		turn=battleService.nextTurn(round, 6, ActionType.BASE);
//		round=battleService.nextRound(turn);
//	;
//
//
//
//	}
}
