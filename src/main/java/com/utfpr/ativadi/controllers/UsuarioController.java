package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Constants;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.entities.Usuario;
import com.utfpr.ativadi.repositories.GrauRepository;
import com.utfpr.ativadi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaController auditoria;
    private final GrauRepository grauRepository;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_usuario";
    private final String TODOS_USUARIO = "usuarios";
    private final String LOAD_GRAUS = "listaGraus";
    private final String USUARIO = "usuario";
    private Usuario user;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, AuditoriaController auditoria, GrauRepository grauRepository) {
        this.usuarioRepository = usuarioRepository;
        this.auditoria = auditoria;
        this.grauRepository = grauRepository;
    }

    @GetMapping("/usuario")
    public String init(Model model, HttpServletRequest request) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        this.user = (Usuario) request.getSession().getAttribute(USUARIO);

        if (user != null && user.getTipo().equals(Constants.ADMIN))
            model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        else
            model.addAttribute(TODOS_USUARIO, usuarioRepository.findById(user.getId()).get());

        return INICIO;
    }

    @GetMapping("/newusuario")
    public String abrirNovo(Usuario usuario, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        model.addAttribute(LOAD_GRAUS, grauRepository.findAll());
        return "add_usuario";
    }

    @PostMapping("/addusuario")
    public String addUsuario(@Valid Usuario usuario, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            model.addAttribute(LOAD_GRAUS, grauRepository.findAll());
            return "add_usuario";
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            model.addAttribute(ERROR, "Este e-mail já foi cadastrado!");
            return "add_usuario";
        }

        if (!usuario.getTipo().equals(Constants.ADMIN)) {
            if (usuario.getGraus().size() == 0) {
                model.addAttribute(ERROR, "O Campo Grau é obrigatório!");
                return "add_usuario";
            }
            if (usuario.getTurno() <= 0) {
                model.addAttribute(ERROR, "O Campo Turno é obrigatório!");
                return "add_usuario";
            }
        }

        usuario.setId(usuarioRepository.getNewID());
        usuarioRepository.save(usuario);
        model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/editusuario/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Usuario inválido:" + id));
        model.addAttribute(LOAD_GRAUS, grauRepository.findAll());
        model.addAttribute("usuario", usuario);
        return "update_usuario";
    }

    @PostMapping("/updateusuario/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Usuario usuario, BindingResult result, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        if (result.hasErrors()) {
            usuario.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            model.addAttribute(LOAD_GRAUS, grauRepository.findAll());
            return "update_usuario";
        }

        Optional<Usuario> user_email = usuarioRepository.findByEmail(usuario.getEmail());
        if (user_email.isPresent() && user_email.get().getId() != usuario.getId()){
            model.addAttribute(ERROR, "Este e-mail já foi cadastrado!");
            return "update_usuario";
        }

        if (!usuario.getTipo().equals(Constants.ADMIN)) {
            if (usuario.getGraus().size() == 0) {
                model.addAttribute(ERROR, "O Campo Grau é obrigatório!");
                return "update_usuario";
            }
            if (usuario.getTurno() <= 0) {
                model.addAttribute(ERROR, "O Campo Turno é obrigatório!");
                return "update_usuario";
            }
        }

        usuarioRepository.save(usuario);
        model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show(), this.getClass().getSimpleName());
        return INICIO;
    }

    @GetMapping("/deleteusuario/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!SessionController.freeAccess())
            return SessionController.LOGIN;

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Usuario inválido:" + id));
        try {
            usuarioRepository.delete(usuario);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show(), this.getClass().getSimpleName());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com Auditoria.");
        }

        model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        return INICIO;
    }
}
