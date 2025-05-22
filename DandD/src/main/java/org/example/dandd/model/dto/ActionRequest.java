package org.example.dandd.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dandd.model.entities.enums.ActionType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionRequest
{
	private GameStateDto previousDto;
	private List<Long> target;
	private ActionType actionType;
}
