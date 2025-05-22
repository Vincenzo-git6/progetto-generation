package org.example.dandd.model.dto;

import org.example.dandd.model.entities.enums.ActionType;

public record ActionDetailDto(
		String name,
		String description,
		ActionType actionType,
		int currentCooldown,
		int maxCooldown,
		int maxTargets,
		boolean targetAllies,
		boolean targetSelf
)
{

}
