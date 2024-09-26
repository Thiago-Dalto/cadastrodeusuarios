package com.sesi.aula04.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sesi.aula04.Repository.TarefaRepository;
import com.sesi.aula04.Repository.UsuarioRepository;
import com.sesi.aula04.model.Tarefa;
import com.sesi.aula04.model.Usuario;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Exibe a lista de tarefas de um usuário específico
    @SuppressWarnings("unchecked")
	@GetMapping("/{usuarioId}")
    public String listarTarefasPorUsuario(@PathVariable("usuarioId") Long usuarioId, Model modelo) {
        // Busca o usuário pelo ID
        var usuario = usuarioRepository.findById(usuarioId);
        
 
        if (((Optional<Usuario>) usuario).isPresent()) {
        	modelo.addAttribute("usuario", ((Optional<Usuario>) usuario).get());
            modelo.addAttribute("tarefas", tarefaRepository.findByUsuarioId(usuarioId));
            modelo.addAttribute("tarefa", new Tarefa());
            return "tarefas"; // Nome do template HTML a ser exibido
        } else {
            // Redireciona para a lista de usuários se o usuário não for encontrado
            return "redirect:/usuarios";
        }
    }

    // Salva uma nova tarefa ou atualiza uma tarefa existente
    @PostMapping("/{usuarioId}/salvar")
    public String salvarTarefa(@PathVariable("usuarioId") Long usuarioId, @ModelAttribute Tarefa tarefa) {
      
        ((Optional<Usuario>) usuarioRepository.findById(usuarioId)).ifPresent(tarefa::setUsuario);
        // Define a data de criação se não estiver definida
        tarefa.setDataCriacao(tarefa.getDataCriacao() == null ? java.time.LocalDate.now() : tarefa.getDataCriacao());
        // Salva a tarefa no banco de dados
        tarefaRepository.save(tarefa);
        // Redireciona para a lista de tarefas do usuário
        return "redirect:/tarefas/" + usuarioId;
    }

    // Exibe o formulário para editar uma tarefa específica
    @GetMapping("/{usuarioId}/editar/{tarefaId}")
    public String editarTarefa(@PathVariable("usuarioId") Long usuarioId, @PathVariable("tarefaId") Long tarefaId, Model modelo) {
        var tarefa = tarefaRepository.findById(tarefaId);
        var usuario = usuarioRepository.findById(usuarioId);
     
        if (tarefa.isPresent() && ((Optional<Tarefa>) usuario).isPresent()) {
            ((Model) modelo).addAttribute("tarefa", tarefa.get());
            modelo.addAttribute("usuario", ((Optional<Usuario>) usuario).get());
            modelo.addAttribute("tarefas", tarefaRepository.findByUsuarioId(usuarioId));
            return "tarefas"; // Nome do template HTML a ser exibido
        } else {
            // Redireciona para a lista de tarefas do usuário se não for encontrado
            return "redirect:/tarefas/" + usuarioId;
        }
    }

    // Remove uma tarefa pelo ID
    @GetMapping("/{usuarioId}/remover/{tarefaId}")
    public String removerTarefa(@PathVariable("usuarioId") Long usuarioId, @PathVariable("tarefaId") Long tarefaId) {
        tarefaRepository.deleteById(tarefaId);
        // Redireciona para a lista de tarefas do usuário
        return "redirect:/tarefas/" + usuarioId;
    }
}
