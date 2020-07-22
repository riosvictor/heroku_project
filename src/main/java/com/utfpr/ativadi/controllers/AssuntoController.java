package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Assunto;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.repositories.AssuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
public class AssuntoController {
    private final AssuntoRepository assuntoRepository;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_assunto";
    private final String TODOS_ASSUNTOS = "assuntos";

    @Autowired
    public AssuntoController(AssuntoRepository assuntoRepository) {
        this.assuntoRepository = assuntoRepository;
    }

    @GetMapping("/assunto")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODOS_ASSUNTOS, assuntoRepository.findAll());

        return INICIO;
    }

    @GetMapping("/newassunto")
    public String abrirNovo(Assunto assunto) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        return "add_assunto";
    }

    @PostMapping("/addassunto")
    public String addAssunto(@Valid Assunto assunto, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            return "add_assunto";
        }

        assunto.setId(assuntoRepository.getNewID());
        assuntoRepository.save(assunto);
        model.addAttribute(TODOS_ASSUNTOS, assuntoRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        return INICIO;
    }

    @GetMapping("/editassunto/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Assunto assunto = assuntoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Assunto inválido:" + id));
        model.addAttribute("assunto", assunto);
        return "update_assunto";
    }

    @PostMapping("/updateassunto/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Assunto assunto, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            assunto.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_assunto";
        }

        assuntoRepository.save(assunto);
        model.addAttribute(TODOS_ASSUNTOS, assuntoRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        return INICIO;
    }

    @GetMapping("/deleteassunto/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Assunto assunto = assuntoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Assunto inválido:" + id));
        try {
            assuntoRepository.delete(assunto);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Matéria.");
        }

        model.addAttribute(TODOS_ASSUNTOS, assuntoRepository.findAll());
        return INICIO;
    }
}
