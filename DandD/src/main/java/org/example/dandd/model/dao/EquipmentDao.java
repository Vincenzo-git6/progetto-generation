package org.example.dandd.model.dao;

import org.example.dandd.model.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentDao extends JpaRepository<Equipment, Long>
{
}
