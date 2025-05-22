package org.example.dandd.model.entities.pg;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dandd.model.entities.Equipment;
import org.example.dandd.model.entities.GameEntity;
import org.example.dandd.model.entities.enums.CharacterType;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("playable")
public class PgPlayable extends GameEntity
{
	@Enumerated(EnumType.STRING)
	private CharacterType characterType;

	@OneToMany(fetch = FetchType.EAGER,mappedBy = "playable",cascade = CascadeType.ALL)
	private List<Equipment> equipments;

	@Column(name = "image_url")
	private String imageUrl;
}
