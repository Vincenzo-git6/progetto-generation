package org.example.dandd.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.dandd.service.BattleService;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class GameEntity extends BaseEntity
{
	/**
	 * Hp min=80, max=200;
	 * atk min=10, max=50;
	 * def min=10, max=20;
	 * speed min=10, max=50
	 */
	protected int hp, atk, def, speed;
	protected String name;
	protected String imgurl;

	@Column(columnDefinition = "TEXT")
	protected String description;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "entity")
	private List<Action> actions;
}
