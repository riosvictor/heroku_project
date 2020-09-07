package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Materia;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.entities.Turma;
import com.utfpr.ativadi.repositories.AssuntoRepository;
import com.utfpr.ativadi.repositories.MateriaRepository;
import com.utfpr.ativadi.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MateriaController {
    private final MateriaRepository materiaRepository;
    private final AssuntoRepository assuntoRepository;
    private final TurmaRepository turmaRepository;
    private final AuditoriaController auditoria;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_materia";
    private final String TODAS_MATERIAS = "materias";
    private final String LOAD_ASSUNTOS = "listaAssuntos";

    @Autowired
    public MateriaController(MateriaRepository materiaRepository, AssuntoRepository assuntoRepository, AuditoriaController auditoria, TurmaRepository turmaRepository) {
        this.materiaRepository = materiaRepository;
        this.assuntoRepository = assuntoRepository;
        this.auditoria = auditoria;
        this.turmaRepository = turmaRepository;
    }

    @GetMapping("/materia")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODAS_MATERIAS, materiaRepository.findAll());
        return INICIO;
    }

    @GetMapping("/newmateria")
    public String abrirNovo(Materia materia, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(LOAD_ASSUNTOS, assuntoRepository.findAll());
        return "add_materia";
    }

    @PostMapping("/addmateria")
    public String addMateria(@Valid Materia materia, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute(LOAD_ASSUNTOS, assuntoRepository.findAll());
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            return "add_materia";
        }

        materia.setId(materiaRepository.getNewID());
        materiaRepository.save(materia);
        model.addAttribute(TODAS_MATERIAS, materiaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/editmateria/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Materia materia = materiaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));
        model.addAttribute("materia", materia);
        model.addAttribute(LOAD_ASSUNTOS, assuntoRepository.findAll());
        return "update_materia";
    }

    @PostMapping("/updatemateria/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Materia materia, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            materia.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_materia";
        }

        materiaRepository.save(materia);
        model.addAttribute(TODAS_MATERIAS, materiaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/deletemateria/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Materia materia = materiaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));

        try {
            materiaRepository.delete(materia);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(), this.getClass().getSimpleName());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Aula.");
        }

        model.addAttribute(TODAS_MATERIAS, materiaRepository.findAll());
        return INICIO;
    }

    @RequestMapping(value = "/turma_materia", method = RequestMethod.GET)
    public @ResponseBody
    List<Materia> findMateriasByTurma(@RequestParam(value = "turmaId", required = true) Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId).orElseThrow(() -> new IllegalArgumentException("Id da Turma inválido:" + turmaId));
        List<Materia> m = materiaRepository.findMateriaByGrau(turma.getGrau());

        for (Materia materia : m){
            materia.setAssuntos(null);
        }

        return m;
    }
}
