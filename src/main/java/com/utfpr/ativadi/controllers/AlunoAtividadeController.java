package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Atividade;
import com.utfpr.ativadi.entities.Constants;
import com.utfpr.ativadi.entities.Turma;
import com.utfpr.ativadi.repositories.AtividadeRepository;
import com.utfpr.ativadi.repositories.AulaRepository;
import com.utfpr.ativadi.repositories.TurmaRepository;
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
    private final AtividadeRepository atividadeRepository;
    private final TurmaRepository turmaRepository;
    private final AuditoriaController auditoria;
    private final String INICIO = "index_aula_aluno";
    private final String TODAS_AULAS = "aulas";

    @Autowired
    public AlunoAtividadeController(AulaRepository aulaRepository, TurmaRepository turmaRepository, AtividadeRepository atividadeRepository, AuditoriaController auditoria) {
        this.aulaRepository = aulaRepository;
        this.atividadeRepository = atividadeRepository;
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

    @GetMapping("/realizaratividade/{id}")
    public String realizarAula(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Atividade atividade = atividadeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Atividade inválido:" + id));
        String url = atividade.getJogo().getUrl();

        if (url.contains("http:")) {
            url = url.replace("http:", "");
        } else if (url.contains("https:")) {
            url = url.replace("https:", "");
        }

        if (!(url.startsWith("//")))
            url = "//".concat(url);

        atividade.getJogo().setUrl(url);
        model.addAttribute("aula", atividade);

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();

        model.addAttribute("currentDate", today);

        return "make_atividade";
    }
}