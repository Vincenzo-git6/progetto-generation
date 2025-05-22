package org.example.dandd.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dandd.model.entities.Action;
import org.example.dandd.model.entities.GameEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStateDto
{
	private List<PgDto> good = new ArrayList<>();
	private List<MonsterDto> evil = new ArrayList<>();
	private List<Long> order= new ArrayList<>();
	private Long currentEntity;

	public void substituteGoodTargetHp(Long id, int hp)
	{
		PgDto old = good.stream().filter(p->p.id().equals(id)).findFirst().orElse(null);
		good.set(good.indexOf(old), old.nuoviHp(hp));
	}

	public void substituteEvilTargetHp(Long id,int hp)
	{
		MonsterDto old = evil.stream().filter(p->p.id().equals(id)).findFirst().orElse(null);
		evil.set(evil.indexOf(old), old.nuoviHp(hp));
	}

	public int getGoodTargetHp(Long id)
	{
		PgDto old = good.stream().filter(p->p.id().equals(id)).findFirst().orElse(null);
		return old.hp();
	}
	public int getEvilTargetHp(Long id)
	{
		MonsterDto old = evil.stream().filter(p->p.id().equals(id)).findFirst().orElse(null);
		return old.hp();
	}
}
