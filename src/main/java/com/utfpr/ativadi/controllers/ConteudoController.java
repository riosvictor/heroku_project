package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Conteudo;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.repositories.ConteudoRepository;
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
public class ConteudoController {
    private final ConteudoRepository conteudoRepository;
    private final JogoRepository jogoRepository;
    private final AuditoriaController auditoria;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_conteudo";
    private final String TODOS_CONTEUDOS = "conteudos";
    private final String LOAD_JOGOS = "listaJogos";

    @Autowired
    public ConteudoController(ConteudoRepository conteudoRepository, JogoRepository jogoRepository, AuditoriaController auditoria) {
        this.conteudoRepository = conteudoRepository;
        this.jogoRepository = jogoRepository;
        this.auditoria = auditoria;
    }

    @GetMapping("/conteudo")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODOS_CONTEUDOS, conteudoRepository.findAll());

        return INICIO;
    }

    @GetMapping("/newconteudo")
    public String abrirNovo(Conteudo conteudo, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(LOAD_JOGOS, jogoRepository.findAll());
        return "add_conteudo";
    }

    @PostMapping("/addconteudo")
    public String addConteudo(@Valid Conteudo conteudo, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            model.addAttribute(LOAD_JOGOS, jogoRepository.findAll());
            return "add_conteudo";
        }

        conteudo.setId(conteudoRepository.getNewID());
        conteudoRepository.save(conteudo);
        model.addAttribute(TODOS_CONTEUDOS, conteudoRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/editconteudo/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Conteudo conteudo = conteudoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Conteúdo inválido:" + id));
        model.addAttribute("conteudo", conteudo);
        model.addAttribute(LOAD_JOGOS, jogoRepository.findAll());
        return "update_conteudo";
    }

    @PostMapping("/updateconteudo/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Conteudo conteudo, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            conteudo.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            model.addAttribute(LOAD_JOGOS, jogoRepository.findAll());
            return "update_conteudo";
        }

        conteudoRepository.save(conteudo);
        model.addAttribute(TODOS_CONTEUDOS, conteudoRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/deleteconteudo/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Conteudo conteudo = conteudoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Conteúdo inválido:" + id));
        try {
            conteudoRepository.delete(conteudo);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(), this.getClass().getSimpleName());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Matéria.");
        }

        model.addAttribute(TODOS_CONTEUDOS, conteudoRepository.findAll());
        return INICIO;
    }
}
