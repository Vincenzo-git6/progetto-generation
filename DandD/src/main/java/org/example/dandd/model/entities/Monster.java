package org.example.dandd.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.dandd.model.entities.enums.Danger;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("monster")
public class Monster extends GameEntity
{
	@Enumerated(EnumType.STRING)
	private Danger danger;

	@Column(name = "image_url")
	private String imageUrl;

}
