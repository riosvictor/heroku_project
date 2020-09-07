package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Auditoria;
import com.utfpr.ativadi.entities.AulaConcrete;
import com.utfpr.ativadi.entities.Constants;
import com.utfpr.ativadi.entities.Turma;
import com.utfpr.ativadi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Calendar;
import java.util.Date;

@Controller
public class AlunoAtividadeController {
    private final AulaRepository aulaRepository;
    private final TurmaRepository turmaRepository;
    private final AuditoriaController auditoria;
    private final String INICIO = "index_aula_aluno";
    private final String TODAS_AULAS = "aulas";

    @Autowired
    public AlunoAtividadeController(AulaRepository aulaRepository, TurmaRepository turmaRepository, AuditoriaController auditoria) {
        this.aulaRepository = aulaRepository;
        this.turmaRepository = turmaRepository;
        this.auditoria = auditoria;
    }

    @GetMapping("/aluno_atividade")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (SessionController.getUser().getTipo().equals(Constants.ALUNO)) {
            long alunoId = SessionController.getUser().getId();
            Turma turma = turmaRepository.findAllByAlunoId(alunoId).orElseThrow(() -> new IllegalArgumentException("Id do Aluno inválido:" + alunoId));
            model.addAttribute(TODAS_AULAS, aulaRepository.findAllByTurma(turma.getId()));
        }

        return INICIO;
    }

    @GetMapping("/realizaraula/{id}")
    public String realizarAula(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));
        String url = aula.getAtividade().getUrl_externa();

        if (url.contains("http:")) {
            url = url.replace("http:", "");
        } else if (url.contains("https:")) {
            url = url.replace("https:", "");
        }

        if (!(url.startsWith("//")))
            url = "//".concat(url);

        aula.getAtividade().setUrl_externa(url);
        model.addAttribute("aula", aula);

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();

        model.addAttribute("currentDate", today);

        return "make_atividade";
    }
}