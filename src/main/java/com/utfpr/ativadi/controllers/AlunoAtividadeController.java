package com.utfpr.ativadi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlunoAtividadeController {
    private final String INICIO = "index_aluno_atividade";

    @GetMapping("/aluno_atividade")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        return INICIO;
    }
}