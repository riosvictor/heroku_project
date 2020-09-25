package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.entities.Turma;
import com.utfpr.ativadi.repositories.MateriaRepository;
import com.utfpr.ativadi.repositories.TurmaRepository;
import com.utfpr.ativadi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;

@Controller
public class TurmaController {
    private final TurmaRepository turmaRepository;
    private final MateriaRepository materiaRepository;
    private final AuditoriaController auditoria;
    private final UsuarioRepository usuarioRepository;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_turma";
    private final String TODAS_TURMAS = "turmas";
    private final String LOAD_PROFESSORES = "listaProfessores";
    private final String LOAD_ALUNOS = "listaAlunos";
    private final String LOAD_MATERIAS = "listaMaterias";

    @Autowired
    public TurmaController(TurmaRepository turmaRepository, UsuarioRepository usuarioRepository, MateriaRepository materiaRepository, AuditoriaController auditoria) {
        this.turmaRepository = turmaRepository;
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoria = auditoria;
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

    @GetMapping("/manageturma/{id}")
    public String abrirGerenciamento(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Turma turma = turmaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));

        model.addAttribute(LOAD_PROFESSORES, usuarioRepository.findProfessorAllWhere(turma.getTurno()));
        model.addAttribute(LOAD_ALUNOS, usuarioRepository.findAlunoAllWhere(turma.getTurno()));
        model.addAttribute(LOAD_MATERIAS, materiaRepository.findAll());

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.YEAR, 1); // to get previous year add -1
        Date nextYear = cal.getTime();

        model.addAttribute("currentDate", today);
        model.addAttribute("nextDate", nextYear);

        model.addAttribute("turma", turma);
        return "manage_turma";
    }

    @PostMapping("/salvargerenciamento")
    public String salvarGerenciamento(Turma turma, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        turmaRepository.save(turma);

        model.addAttribute(TODAS_TURMAS, turmaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(),  this.getClass().getSimpleName());
        return INICIO;
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
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(),  this.getClass().getSimpleName());
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
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(),  this.getClass().getSimpleName());
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
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(),  this.getClass().getSimpleName());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Aula.");
        }

        model.addAttribute(TODAS_TURMAS, turmaRepository.findAll());
        return INICIO;
    }
}
