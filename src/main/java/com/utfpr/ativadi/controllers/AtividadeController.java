package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Atividade;
import com.utfpr.ativadi.entities.AtividadeFactory;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.repositories.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
public class AtividadeController {
    private final AtividadeRepository atividadeRepository;
    private final AuditoriaController auditoria;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_atividade";
    private final String TODAS_ATIVIDADES = "atividades";

    @Autowired
    public AtividadeController(AtividadeRepository atividadeRepository, AuditoriaController auditoria) {
        this.atividadeRepository = atividadeRepository;
        this.auditoria = auditoria;
    }

    @GetMapping("/atividade")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODAS_ATIVIDADES, atividadeRepository.findAll());
        return INICIO;
    }

    @GetMapping("/newatividade")
    public String abrirNovo(Atividade atividade) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        return "add_atividade";
    }

    @PostMapping("/addatividade")
    public String addAtividade(@Valid Atividade atividade, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        AtividadeFactory.loadCache(atividadeRepository.findAll());
        atividade = (Atividade) AtividadeFactory.getAtividade(atividade);

        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            return "add_atividade";
        }

        if (atividade.getId() > 0) {
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.SHARE).show());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.SHARE).show(), this.getClass().getSimpleName());
        }else {
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
            atividade.setId(atividadeRepository.getNewID());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(), this.getClass().getSimpleName());
        }

        atividadeRepository.save(atividade);
        model.addAttribute(TODAS_ATIVIDADES, atividadeRepository.findAll());

        return INICIO;
    }

    @GetMapping("/editatividade/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Atividade atividade = atividadeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Atividade inválido:" + id));
        model.addAttribute("atividade", atividade);
        return "update_atividade";
    }

    @PostMapping("/updateatividade/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Atividade atividade, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            atividade.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_atividade";
        }

        atividadeRepository.save(atividade);
        model.addAttribute(TODAS_ATIVIDADES, atividadeRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/deleteatividade/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Atividade atividade = atividadeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Atividade inválido:" + id));

        try {
            atividadeRepository.delete(atividade);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(), this.getClass().getSimpleName());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Aula.");
        }

        model.addAttribute(TODAS_ATIVIDADES, atividadeRepository.findAll());
        return INICIO;
    }
}
