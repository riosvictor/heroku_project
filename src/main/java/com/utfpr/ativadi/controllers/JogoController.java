package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Jogo;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.repositories.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class JogoController {
    private final JogoRepository jogoRepository;
    private final AuditoriaController auditoria;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_jogo";
    private final String TODOS_JOGOS = "jogos";

    @Autowired
    public JogoController(JogoRepository jogoRepository, AuditoriaController auditoria) {
        this.jogoRepository = jogoRepository;
        this.auditoria = auditoria;
    }

    @GetMapping("/jogo")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODOS_JOGOS, jogoRepository.findAll());

        return INICIO;
    }

    @GetMapping("/newjogo")
    public String abrirNovo(Jogo jogo, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        return "add_jogo";
    }

    @PostMapping("/addjogo")
    public String addJogo(@Valid Jogo jogo, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            return "add_jogo";
        }

        jogo.setId(jogoRepository.getNewID());
        jogoRepository.save(jogo);
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        model.addAttribute(TODOS_JOGOS, jogoRepository.findAll());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/editjogo/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Jogo jogo = jogoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Jogo inválido:" + id));
        model.addAttribute("jogo", jogo);
        return "update_jogo";
    }

    @PostMapping("/updatejogo/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Jogo jogo, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            jogo.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_jogo";
        }

        jogoRepository.save(jogo);
        model.addAttribute(TODOS_JOGOS, jogoRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/deletejogo/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Jogo jogo = jogoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Jogo inválido:" + id));
        try {
            jogoRepository.delete(jogo);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(), this.getClass().getSimpleName());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com um Conteúdo.");
        }

        model.addAttribute(TODOS_JOGOS, jogoRepository.findAll());
        return INICIO;
    }
}
