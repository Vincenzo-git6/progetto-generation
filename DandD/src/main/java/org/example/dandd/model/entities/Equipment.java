package org.example.dandd.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.dandd.model.entities.enums.EquipmentType;
import org.example.dandd.model.entities.pg.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equipment extends BaseEntity
{
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	private int plusDmg;
	private int plusDef;
	@Enumerated(EnumType.STRING)
	private EquipmentType equipmentType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_playable")
	private PgPlayable playable;
}
