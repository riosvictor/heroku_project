package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Turma;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.repositories.AssuntoRepository;
import com.utfpr.ativadi.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class TurmaController {
    private final TurmaRepository turmaRepository;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_turma";
    private final String TODAS_TURMAS = "turmas";

    @Autowired
    public TurmaController(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    @GetMapping("/turma")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODAS_TURMAS, turmaRepository.findAll());
        return INICIO;
    }

    @GetMapping("/newturma")
    public String abrirNovo(Turma turma, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        return "add_turma";
    }

    @PostMapping("/addturma")
    public String addTurma(@Valid Turma turma, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            return "add_turma";
        }

        turma.setId(turmaRepository.getNewID());
        turmaRepository.save(turma);
        model.addAttribute(TODAS_TURMAS, turmaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        return INICIO;
    }

    @GetMapping("/editturma/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Turma turma = turmaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));
        model.addAttribute("turma", turma);
        return "update_turma";
    }

    @PostMapping("/updateturma/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Turma turma, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            turma.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_turma";
        }

        turmaRepository.save(turma);
        model.addAttribute(TODAS_TURMAS, turmaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        return INICIO;
    }

    @GetMapping("/deleteturma/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Turma turma = turmaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));

        try {
            turmaRepository.delete(turma);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Aula.");
        }

        model.addAttribute(TODAS_TURMAS, turmaRepository.findAll());
        return INICIO;
    }
}
