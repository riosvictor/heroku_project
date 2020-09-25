package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Auditoria;
import com.utfpr.ativadi.repositories.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.TimeZone;



@Controller
public class AuditoriaController {
    private final String INICIO = "index_auditoria";
    private final String TODAS_AUDITORIAS = "auditorias";
    private final AuditoriaRepository auditoriaRepository;

    @Autowired
    public AuditoriaController(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @GetMapping("/auditoria")
    public String init(Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(TODAS_AUDITORIAS, auditoriaRepository.findAll());
        return INICIO;
    }

    public void addAuditoria(String descricao, String className) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		        
        Auditoria auditoria = new Auditoria();

        auditoria.setId(auditoriaRepository.getNewID());
        auditoria.setDescricao(className +": "+ descricao);
        auditoria.setData(new Date());
        auditoria.setUsuario(SessionController.getUser());

        auditoriaRepository.save(auditoria);
    }
}
