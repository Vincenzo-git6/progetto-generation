package org.example.dandd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.dandd.model.entities.enums.ActionType;
import org.example.dandd.model.entities.enums.CharacterType;
import org.example.dandd.model.entities.pg.PgPlayable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action extends BaseEntity {
	private String nameAction;

	@Column(columnDefinition = "TEXT")
	private String descriptionAction;

	@Column(name = "accuracy")
	private int precision;

	private double molt;
	private int maxNumTarget; // Corrisponde a maxTargets
	private int cooldown;     // Corrisponde a maxCooldown o baseCooldown

	@Enumerated(EnumType.STRING)
	private ActionType actionType;

	// NUOVI CAMPI DA AGGIUNGERE (e relative colonne nel DB)
	@Column(name = "targets_allies")
	private boolean targetsAllies; // true se può bersagliare alleati (es. cure)
	@Column(name = "targets_self")
	private boolean targetsSelf;   // true se bersaglia solo sé stesso e non richiede selezione

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entity")
	private GameEntity entity;

	// Il metodo hit() e dmgCalculator() rimangono come sono
	public boolean hit() {
		int dice = (int) (Math.random() * 100) + 1;
		return precision > dice;
	}

	public int dmgCalculator(GameEntity attaccante, GameEntity difensore) {
		if (!hit()) return 0;
		double dmg = 0;
		if (attaccante instanceof PgPlayable p) {
			if (difensore instanceof Monster m) {
				switch (actionType) {
					case BASE: dmg = p.getAtk(); break;
					case HEAVY:

						dmg = p.getAtk() + p.getEquipments().stream()
							.mapToInt(arma -> arma.getPlusDmg())
							.sum();
						break;
					case SPECIALE: {
						switch (p.getCharacterType()) {
							case CODECLEANER: dmg = p.getAtk()+1000; break;
							case CODETHIEF:
							case DATAMYSTIC:
							case OMNICODER:
							case TROUBLESHOOTER: {
								int equipBonus = p.getEquipments().stream()
										.mapToInt(arma -> arma.getPlusDmg())
										.sum();
								dmg = p.getAtk() + (equipBonus * molt);
							}
							break;
							case GITBARD: dmg = -20; break; // fallback se cura un mostro
							case INFRASTRUCTURE: {
								int defBonus = p.getEquipments().stream()
										.mapToInt(arma -> arma.getPlusDef())
										.sum();
								dmg = p.getDef() + (defBonus * molt);
							}
							break;
						}
					}
					break; // Aggiunto break mancante
				}
				return (int) Math.round(dmg - m.getDef());
			} else if (difensore instanceof PgPlayable targetP) {
				if (actionType == ActionType.SPECIALE && p.getCharacterType() == CharacterType.GITBARD) {
					return -20; // Cura
				}
				if(actionType==ActionType.HEAVY && p.getCharacterType() == CharacterType.OMNICODER)
				{
					return -1000;
				}
			}
		}

		if (attaccante instanceof Monster m && difensore instanceof PgPlayable p) {
			switch (actionType) {
				case BASE: dmg = m.getAtk(); break;
				case SPECIALE: dmg = (m.getAtk() + m.getAtk()) * molt; break;
				// Potrebbe mancare HEAVY per i mostri, o usare un default
				default: dmg = m.getAtk(); break;
			}
			return (int) Math.round(dmg - p.getDef());
		}
		return 0;
	}

	// Getter e Setter per i nuovi campi (Lombok @Data dovrebbe generarli, ma è bene esserne consapevoli)
	// public boolean isTargetsAllies() { return targetsAllies; }
	// public void setTargetsAllies(boolean targetsAllies) { this.targetsAllies = targetsAllies; }
	// public boolean isTargetsSelf() { return targetsSelf; }
	// public void setTargetsSelf(boolean targetsSelf) { this.targetsSelf = targetsSelf; }
	// public int getMaxNumTarget() { return maxNumTarget; } // Già presente da @Data
	// public int getCooldown() { return cooldown; }       // Già presente da @Data
}
