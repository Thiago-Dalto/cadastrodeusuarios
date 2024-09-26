package com.sesi.aula04.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sesi.aula04.model.TaskStatus;
import com.sesi.aula04.model.Usuario;

@Repository
public interface TaskStatusRepository extends CrudRepository<TaskStatus, Integer> {


	Iterable<TaskStatus> findAll();

	Optional<Usuario> findById(int statusId);

}
