package org.example.dandd.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dandd.model.dao.GameEntityDao;
import org.example.dandd.model.dao.MonsterDao;
import org.example.dandd.model.dao.PgPlayableDao;
import org.example.dandd.model.dto.GameStateDto;
import org.example.dandd.model.dto.MonsterDto;
import org.example.dandd.model.dto.PgDto;
import org.example.dandd.model.entities.Action;
import org.example.dandd.model.entities.GameEntity;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.entities.enums.ActionType;
import org.example.dandd.model.entities.enums.CharacterType;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.example.dandd.model.mapper.MonsterMapper;
import org.example.dandd.model.mapper.PgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class BattleService {
	@Autowired
	PgPlayableDao pdao;
	@Autowired
	PgMapper pgMapper;

	@Autowired
	MonsterDao mdao;
	@Autowired
	MonsterMapper monsterMapper;

	@Autowired
	GameEntityDao gDao;

	private static final Random random = new Random();

	public GameStateDto iniziaFight(List<PgPlayable> player, List<Monster> mostri) {
		GameStateDto dto = new GameStateDto();

		List<PgDto> pgDtos = pgMapper.toDtos(player);
		List<MonsterDto> monsterDtos = monsterMapper.toDtos(mostri);

		List<Long> order = Stream.concat(player.stream(), mostri.stream())
				.sorted(Comparator.comparingInt(GameEntity::getSpeed).reversed())
				.map(GameEntity::getId)
				.collect(Collectors.toList());

		dto.setGood(pgDtos);
		dto.setEvil(monsterDtos);
		dto.setOrder(order);
		if (!order.isEmpty())
			dto.setCurrentEntity(order.get(0));

		return dto;
	}

	public GameStateDto nextTurn(GameStateDto previousDto, List<Long> targetIdsFromRequest, ActionType actionTypeFromRequest) {
		Long attackerId = previousDto.getCurrentEntity();
		GameEntity attacker = gDao.findById(attackerId)
				.orElseThrow(() -> new IllegalStateException("Attaccante non trovato con ID: " + attackerId));

		Action selectedAction;
		List<GameEntity> actualTargets = new ArrayList<>();

		if (attacker instanceof Monster currentMonster) {
			if (currentMonster.getActions() == null || currentMonster.getActions().isEmpty()) {
				throw new IllegalStateException("Il mostro " + currentMonster.getName() + " non ha azioni definite!");
			}
			selectedAction = currentMonster.getActions().get(random.nextInt(currentMonster.getActions().size()));

			List<PgDto> alivePgs = previousDto.getGood().stream()
					.filter(pg -> pg.hp() > 0)
					.collect(Collectors.toList());

			if (alivePgs.isEmpty()) {
				return previousDto;
			}
			PgDto targetPgDto = alivePgs.get(random.nextInt(alivePgs.size()));
			GameEntity targetPgEntity = gDao.findById(targetPgDto.id())
					.orElseThrow(() -> new IllegalStateException("PG bersaglio (ID: " + targetPgDto.id() + ") non trovato per l'IA del mostro."));
			actualTargets.add(targetPgEntity);

		} else if (attacker instanceof PgPlayable currentPlayer) {
			selectedAction = currentPlayer.getActions().stream()
					.filter(a -> a.getActionType().equals(actionTypeFromRequest))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("Azione " + actionTypeFromRequest + " non trovata per PG " + currentPlayer.getName()));

			if (targetIdsFromRequest == null || targetIdsFromRequest.isEmpty()) {
				if (actionRequiresTarget(selectedAction, currentPlayer)) { // Implementa actionRequiresTarget
					throw new IllegalStateException("L'azione " + selectedAction.getNameAction() + " richiede un bersaglio, ma nessuno è stato fornito.");
				}
				// Se l'azione non richiede bersaglio o bersaglia sé stesso (es. cura speciale Gitbard su sé stesso)
				if (selectedAction.getActionType() == ActionType.SPECIALE && currentPlayer.getCharacterType().equals(CharacterType.GITBARD) || selectedAction.getActionType() == ActionType.HEAVY && currentPlayer.getCharacterType().equals(CharacterType.OMNICODER)){
					// Per ora, se è SPECIALE e GITBARD e non ci sono target, assumiamo che curi sé stesso
					// Questo potrebbe richiedere una logica più fine se SPECIALE può avere altri effetti senza target
					actualTargets.add(attacker);
				} else if (!actionRequiresTarget(selectedAction, currentPlayer)) {
					actualTargets.add(attacker); // Azione su sé stesso
				} else {
					throw new IllegalStateException("Nessun bersaglio fornito per un'azione che lo richiede.");
				}
			} else {
				actualTargets = gDao.findAllById(targetIdsFromRequest);
				if (actualTargets.isEmpty() || actualTargets.stream().anyMatch(Objects::isNull)) {
					throw new IllegalStateException("Uno o più bersagli specificati per il PG non sono validi o non trovati.");
				}
			}
		} else {
			throw new IllegalStateException("Tipo di attaccante non riconosciuto: " + attacker.getClass().getName());
		}

		for (GameEntity currentTarget : actualTargets) {
			if (attacker instanceof PgPlayable player && currentTarget instanceof Monster monsterTarget) {
				int damage = GameService.precisionCheck(selectedAction.dmgCalculator(player, monsterTarget), selectedAction.getPrecision());
				int hpMonster = previousDto.getEvilTargetHp(monsterTarget.getId()) - damage;
				if (hpMonster < 0) hpMonster = 0;
				previousDto.substituteEvilTargetHp(monsterTarget.getId(), hpMonster);
				if (hpMonster == 0) {
					previousDto.getOrder().remove(monsterTarget.getId());
				}
			} else if (attacker instanceof Monster monsterAttacker && currentTarget instanceof PgPlayable playerTarget) {
				int damage = GameService.precisionCheck(selectedAction.dmgCalculator(monsterAttacker, playerTarget), selectedAction.getPrecision());
				int hpPlayer = previousDto.getGoodTargetHp(playerTarget.getId()) - damage;
				if (hpPlayer < 0) hpPlayer = 0;
				previousDto.substituteGoodTargetHp(playerTarget.getId(), hpPlayer);
				if (hpPlayer == 0) {
					previousDto.getOrder().remove(playerTarget.getId());
				}
			} else if (attacker instanceof PgPlayable player && currentTarget instanceof PgPlayable targetPlayer) {
				if (selectedAction.getActionType() == ActionType.SPECIALE && player.getCharacterType().equals(CharacterType.GITBARD) || selectedAction.getActionType() == ActionType.HEAVY && player.getCharacterType().equals(CharacterType.OMNICODER)) {
					int healing = GameService.precisionCheck(-selectedAction.dmgCalculator(player, targetPlayer), selectedAction.getPrecision()); // Danno negativo per cura
					int maxHp = targetPlayer.getHp(); // Assumendo che getHp() sull'entità sia maxHp
					int currentHp = previousDto.getGoodTargetHp(targetPlayer.getId());
					int healedHp = Math.min(currentHp + healing, maxHp);
					previousDto.substituteGoodTargetHp(targetPlayer.getId(), healedHp);
				} else {
					// Logica per PG che attacca PG, se permessa (altrimenti lancia eccezione o ignora)
					// Per ora, assumiamo che non sia un caso comune se non per cure
					// throw new IllegalStateException("PG non può attaccare PG con azioni non curative.");
				}
			} else {
				throw new IllegalStateException("Combinazione Attaccante/Bersaglio non gestita: "
						+ attacker.getClass().getSimpleName() + " vs " + currentTarget.getClass().getSimpleName());
			}
		}
		return previousDto;
	}

	// Metodo helper (da implementare con più logica se necessario)
	private boolean actionRequiresTarget(Action action, PgPlayable player) {
		// Se l'azione è SPECIALE e il PG è GITBARD, potrebbe non richiedere un bersaglio
		// se l'intenzione è una cura ad area o su sé stesso (non gestito qui)
		if (action.getActionType() == ActionType.SPECIALE && player.getCharacterType().equals(CharacterType.GITBARD)|| action.getActionType() == ActionType.HEAVY && player.getCharacterType().equals(CharacterType.OMNICODER)) {
			// La cura del Gitbard nel tuo codice originale sembra sempre richiedere un target esplicito.
			// Se ci sono azioni speciali che non richiedono target, aggiungi la logica qui.
			return true; // Per ora, assumiamo che anche la speciale del Gitbard richieda un target.
		}
		// Aggiungi altre logiche per azioni che non richiedono bersaglio (es. buff su sé stesso)
		// if (action.targetsSelfOnly()) return false;
		return true; // Default: l'azione richiede un bersaglio
	}


	public GameStateDto nextRound(GameStateDto currentState) {
		if (currentState.getOrder().isEmpty()) {
			// Non ci sono più entità nell'ordine, la battaglia dovrebbe essere finita
			// battleOver dovrebbe gestire questo, ma è bene avere un controllo
			return currentState;
		}
		battleOver(currentState); // Controlla se la battaglia è finita prima di procedere

		List<Long> order = currentState.getOrder();
		if (order.isEmpty()) { // Ricontrolla dopo battleOver che potrebbe aver modificato l'ordine
			return currentState;
		}

		Long currentEntityId = currentState.getCurrentEntity();
		int currentEntityIndex = order.indexOf(currentEntityId);

		if (currentEntityIndex == -1) {
			// L'entità corrente non è più nell'ordine (probabilmente sconfitta)
			// Scegli la prima entità nell'ordine attuale come prossima
			if (!order.isEmpty()) {
				currentState.setCurrentEntity(order.get(0));
			} else {
				// Nessuno è rimasto, la battaglia è finita
			}
			return currentState;
		}

		int nextEntityIndex = (currentEntityIndex + 1) % order.size();
		Long nextEntityId = order.get(nextEntityIndex);
		currentState.setCurrentEntity(nextEntityId);

		return currentState;
	}

	public String battleOver(GameStateDto currentState) {
		List<Long> order = currentState.getOrder();

		boolean monstersAlive = order.stream()
				.map(gDao::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.anyMatch(e -> e instanceof Monster && e.getHp() > 0);

		boolean playersAlive = order.stream()
				.map(gDao::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.anyMatch(e -> e instanceof PgPlayable && e.getHp() > 0);

		// Controlla anche direttamente dalle liste good/evil in GameStateDto
		// perché l'ordine potrebbe non essere aggiornato istantaneamente se un PG/Mostro viene sconfitto
		// e rimosso dall'ordine *dopo* che questo metodo è chiamato.
		boolean playersInDtoAlive = currentState.getGood().stream().anyMatch(pg -> pg.hp() > 0);
		boolean monstersInDtoAlive = currentState.getEvil().stream().anyMatch(m -> m.hp() > 0);


		if (!monstersInDtoAlive && playersInDtoAlive)
			return "Hai vinto";
		else if (monstersInDtoAlive && !playersInDtoAlive)
			return "Hai perso";
		else if (!monstersInDtoAlive && !playersInDtoAlive)
			return "Pareggio? O tutti sconfitti."; // Caso anomalo
		else
			return "La battaglia è ancora in corso";
	}
}
