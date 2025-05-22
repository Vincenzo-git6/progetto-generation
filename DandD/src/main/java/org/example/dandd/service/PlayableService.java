package org.example.dandd.service;

import org.example.dandd.model.dao.PgPlayableDao;
import org.example.dandd.model.dto.PgDto;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.example.dandd.model.mapper.PgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayableService
{
	@Autowired
	PgPlayableDao pgPlayableDao;

	@Autowired
	PgMapper pgMapper;

	public PgDto getPgDtoById(Long id)
	{
		PgPlayable pgPlayable = pgPlayableDao.findById(id).orElse(null);
		if(pgPlayable == null)
			return null;
		return pgMapper.toDto(pgPlayable);
	}

	public List<PgDto> allPgDtos()
	{
		List<PgPlayable> pgPlayables = pgPlayableDao.findAll();
		return pgMapper.toDtos(pgPlayables);
	}
}
