package com.sesi.aula04.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sesi.aula04.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Encontra todas as tarefas associadas a um ID de usuário
    List<Tarefa> findByUsuarioId(Long usuarioId);
}