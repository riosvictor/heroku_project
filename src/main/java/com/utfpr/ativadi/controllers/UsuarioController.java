package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.Usuario;
import com.utfpr.ativadi.entities.Mensagem;
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

@Controller
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_usuario";
    private final String TODOS_USUARIO = "usuarios";
    private final String USUARIO = "usuario";
    private Usuario user;


    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuario")
    public String init(Model model, HttpServletRequest request) {
        this.user = (Usuario) request.getSession().getAttribute(USUARIO);

        if (user.getTipo().equals("admin"))
            model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        else
            model.addAttribute(TODOS_USUARIO, usuarioRepository.findById(user.getId()).get());

        return INICIO;
    }

    @GetMapping("/newusuario")
    public String abrirNovo(Usuario usuario) {
        return "add_usuario";
    }

    @PostMapping("/addusuario")
    public String addUsuario(@Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());
            return "add_usuario";
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            model.addAttribute(ERROR, "Este e-mail já foi cadastrado!");
            return "add_usuario";
        }

        usuario.setId(usuarioRepository.getNewID());
        usuarioRepository.save(usuario);
        model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        return INICIO;
    }

    @GetMapping("/editusuario/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Usuario inválido:" + id));
        model.addAttribute("usuario", usuario);
        return "update_usuario";
    }

    @PostMapping("/updateusuario/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            usuario.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_usuario";
        }

        usuarioRepository.save(usuario);
        model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        return INICIO;
    }

    @GetMapping("/deleteusuario/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id do Usuario inválido:" + id));
        try {
            usuarioRepository.delete(usuario);
            model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
        } catch (Exception e) {
            model.addAttribute(ERROR, "Este registro não pode ser removido, pois possui vínculo com uma Matéria.");
        }

        model.addAttribute(TODOS_USUARIO, usuarioRepository.findAll());
        return INICIO;
    }
}
