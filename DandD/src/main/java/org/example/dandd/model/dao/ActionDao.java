package org.example.dandd.model.dao;


import org.example.dandd.model.entities.Action;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.entities.enums.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionDao extends JpaRepository<Action, Long>
{
	Action findByNameAction(String name);

	Action findByActionType(ActionType actionType);

	Action findByActionTypeAndEntity_Id(ActionType actionType, Long entityId);
}

