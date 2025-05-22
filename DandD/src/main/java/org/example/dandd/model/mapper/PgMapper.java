package org.example.dandd.model.mapper;

import org.example.dandd.model.dto.ActionDetailDto;
import org.example.dandd.model.dto.PgDto;
import org.example.dandd.model.entities.Action;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PgMapper {

	public PgDto toDto(PgPlayable pg) {
		if (pg == null) {
			return null;
		}

		List<ActionDetailDto> detailedActionsList;
		if (pg.getActions() != null) {
			detailedActionsList = pg.getActions().stream()
					.map(actionEntity -> {
						// DEBUGGING: Stampa i valori letti dall'entità Action
						System.out.println("[DEBUG PgMapper] Azione: " + actionEntity.getNameAction());
						System.out.println("  > actionEntity.isTargetsAllies(): " + actionEntity.isTargetsAllies());
						System.out.println("  > actionEntity.isTargetsSelf(): " + actionEntity.isTargetsSelf());
						System.out.println("  > actionEntity.getMaxNumTarget(): " + actionEntity.getMaxNumTarget());
						System.out.println("  > actionEntity.getCooldown(): " + actionEntity.getCooldown());
						System.out.println("  > actionEntity.getActionType(): " + actionEntity.getActionType());


						return new ActionDetailDto(
								actionEntity.getNameAction(),
								actionEntity.getDescriptionAction(),
								actionEntity.getActionType(),
								0, // currentCooldown (placeholder, da gestire dinamicamente nel BattleService)
								actionEntity.getCooldown(),       // maxCooldown (il cooldown base dell'azione)
								actionEntity.getMaxNumTarget(),   // maxTargets
								actionEntity.isTargetsAllies(),   // targetsAllies
								actionEntity.isTargetsSelf()      // targetsSelf
						);
					})
					.collect(Collectors.toList());
		} else {
			detailedActionsList = Collections.emptyList();
		}

		return new PgDto(
				pg.getId(),
				pg.getName(),
				pg.getDescription(),
				pg.getHp(),
				pg.getAtk(),
				pg.getDef(),
				pg.getSpeed(),
				detailedActionsList,
				pg.getEquipments() != null ? pg.getEquipments().stream().map(e -> e.getName()).collect(Collectors.toList()) : Collections.emptyList(),
				pg.getEquipments() != null ? pg.getEquipments().stream().map(e -> e.getDescription()).collect(Collectors.toList()) : Collections.emptyList(),
				pg.getCharacterType() != null ? pg.getCharacterType().name() : null,
				pg.getImageUrl()
		);
	}

	public List<PgDto> toDtos(List<PgPlayable> pgs) {
		if (pgs == null) {
			return Collections.emptyList();
		}
		return pgs.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}
}
