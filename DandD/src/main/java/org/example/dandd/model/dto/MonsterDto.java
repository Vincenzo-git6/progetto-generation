package org.example.dandd.model.dto;

import java.util.List;

public record MonsterDto
		(
				Long id,
				String name,
				String description,
				String danger,
				int hp,
				int atk,
				int def,
				int spd,
				List<String> actionsName,
				List<String> actionsDescriptions,
				String imageUrl
		)
{
	public MonsterDto nuoviHp(int nuoviHp)
	{
		return new MonsterDto(
				this.id,
				this.name,
				this.description,
				this.danger,
				nuoviHp,
				this.atk,
				this.def,
				this.spd,
				this.actionsName,
				this.actionsDescriptions,
				this.imageUrl
		);
	}
}
