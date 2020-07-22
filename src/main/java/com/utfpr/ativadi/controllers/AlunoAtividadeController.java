package com.utfpr.ativadi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlunoAtividadeController {
    //private final AtividadeRepository atividadeRepository;
    //private final String ERROR = "errorMessage";
    //private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_aluno_atividade";
    //private final String TODAS_ATIVIDADES = "atividades";

    @GetMapping("/aluno_atividade")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;
        //model.addAttribute(TODAS_ATIVIDADES, atividadeRepository.findAll());
        return INICIO;
    }
}