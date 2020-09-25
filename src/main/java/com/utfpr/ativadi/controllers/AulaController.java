package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.*;
import com.utfpr.ativadi.repositories.AtividadeRepository;
import com.utfpr.ativadi.repositories.AulaRepository;
import com.utfpr.ativadi.repositories.TurmaRepository;
import com.utfpr.ativadi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AulaController {
    private final AulaRepository aulaRepository;
    private final UsuarioRepository professorRepository;
    private final TurmaRepository turmaRepository;
    private final AtividadeRepository atividadeRepository;
    private final AuditoriaController auditoria;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_aula";
    private final String TODAS_AULAS = "aulas";
    private final String LOAD_ATIVIDADES = "listaAtividades";
    private final String LOAD_PROFESSORES = "listaProfessores";
    private final String LOAD_TURMAS = "listaTurmas";

    @Autowired
    public AulaController(AulaRepository aulaRepository, UsuarioRepository professorRepository,
                          TurmaRepository turmaRepository,
                          AtividadeRepository atividadeRepository, AuditoriaController auditoria) {
        this.aulaRepository = aulaRepository;
        this.professorRepository = professorRepository;
        this.turmaRepository = turmaRepository;
        this.atividadeRepository = atividadeRepository;
        this.auditoria = auditoria;
    }

    @GetMapping("/aula")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (SessionController.getUser().getTipo().equals(Constants.PROFESSOR)) {
            long professorId = SessionController.getUser().getId();
            model.addAttribute(TODAS_AULAS, aulaRepository.findAllByProfessor(professorId));
        } else {
            model.addAttribute(TODAS_AULAS, aulaRepository.findAll());
        }

        return INICIO;
    }

    @GetMapping("/newaula")
    public String abrirNovo(AulaConcrete aula, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute("aula", aula);

        if (SessionController.getUser().getTipo().equals(Constants.PROFESSOR)) {
            long professorId = SessionController.getUser().getId();

            model.addAttribute(LOAD_PROFESSORES, professorRepository.findProfessorById(professorId).get());
            model.addAttribute(LOAD_TURMAS, turmaRepository.findAllByProfessorId(professorId));
            model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());
        } else {
            model.addAttribute(LOAD_PROFESSORES, professorRepository.findProfessorAll());
            model.addAttribute(LOAD_TURMAS, turmaRepository.findAll());
            model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());
        }

        return "add_aula";
    }

    @PostMapping("/addaula")
    public String addAula(@Valid AulaConcrete aula, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute("aula", aula);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());

            if (SessionController.getUser().getTipo().equals(Constants.PROFESSOR)) {
                long professorId = SessionController.getUser().getId();

                model.addAttribute(LOAD_PROFESSORES, professorRepository.findProfessorById(professorId).get());
                model.addAttribute(LOAD_TURMAS, turmaRepository.findAllByProfessorId(professorId));
                model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());
            } else {
                model.addAttribute(LOAD_PROFESSORES, professorRepository.findProfessorAll());
                model.addAttribute(LOAD_TURMAS, turmaRepository.findAll());
                model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());
            }

            return "add_aula";
        }

        aula.setId(aulaRepository.getNewID());
        aula.setStatus(Constants.ABERTO);
        aulaRepository.save(aula);
        model.addAttribute(TODAS_AULAS, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(), this.getClass().getSimpleName());

        return INICIO;
    }

    @GetMapping("/editaula/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Aula inválido:" + id));
        model.addAttribute("aula", aula);
        model.addAttribute(LOAD_PROFESSORES, professorRepository.findProfessorAll());
        model.addAttribute(LOAD_TURMAS, turmaRepository.findAll());
        model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());

        return "update_aula";
    }

    @PostMapping("/updateaula/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid AulaConcrete aula, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            aula.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_aula";
        }

        aulaRepository.save(aula);
        model.addAttribute(TODAS_AULAS, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(), this.getClass().getSimpleName());

        return INICIO;
    }

    @GetMapping("/deleteaula/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Aula inválido:" + id));
        aulaRepository.delete(aula);
        model.addAttribute(TODAS_AULAS, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/cloneaula/{id}")
    public String abrirClone(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Aula inválido:" + id));
        AulaConcrete clone = (AulaConcrete) aula.clone();

        clone.setAtividades(atividadeRepository.findAllByAulaId(aula.getId()));
        clone.setId(aulaRepository.getNewID());
        clone.setStatus(Constants.ABERTO);
        aulaRepository.save(clone);

        model.addAttribute(TODAS_AULAS, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.CLONE).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.CLONE).show(), this.getClass().getSimpleName());

        return INICIO;
    }

    @RequestMapping(value = "/turmas_por_professor", method = RequestMethod.GET)
    public @ResponseBody
    List<Turma> findTurmasByProfessor(@RequestParam(value = "professorId", required = true) Long professorId) {
        return professorId == null ? turmaRepository.findAll() : turmaRepository.findAllByProfessorId(professorId);
    }

    @RequestMapping(value = "/atividades_por_turma", method = RequestMethod.GET)
    public @ResponseBody
    List<Atividade> findAtividadesByTurma(@RequestParam(value = "turmaId", required = true) Long turmaId) {
        return turmaId == null ? atividadeRepository.findAll() : atividadeRepository.findAllByTurmaId(turmaId);
    }
}
