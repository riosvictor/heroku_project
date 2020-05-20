package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.component.EmailService;
import com.utfpr.ativadi.entities.Usuario;
import com.utfpr.ativadi.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;


/*
realizar o controller em todos os controllers, onde caso não tenha usuário na sessão, redirecionar para não autorizado
definir quais janelas terão quais acessos
criar uma tela não autorizado
*/



@Controller
public class SessionController {
    private final UsuarioRepository usuarioRepository;
    private final String USUARIO = "usuario";
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String LOGIN = "login";
    private final String INICIO = "index";
    private final String REGISTER = "registration";
    private final String RECOVER_PASSWORD = "recover_password";
    private Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    public EmailService emailService;

    @Autowired
    public SessionController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        logger.warn("Entrou em Session Controller");
    }

    @GetMapping("/")
    public String inicio(Model model, HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute(USUARIO);

        return user == null ? LOGIN : INICIO;
    }

    @PostMapping("/dologin")
    public String doLogin(Model model, @RequestParam("email") String email, @RequestParam("senha") String senha, HttpServletRequest request) throws ScriptException {
        Optional<Usuario> usuario = usuarioRepository.findByEmailSenha(email, senha);

        if(usuario.isPresent()){
            request.getSession().setAttribute(USUARIO, usuario.get());
            return INICIO;
        }else{
            model.addAttribute("email", email);
            model.addAttribute("senha", senha);

            model.addAttribute(ERROR, "O Usuário não está cadastrado com este e-mail e/ou senha!");
            return LOGIN;
        }
    }

    @PostMapping("/sendemail")
    public String senderRecoveryEmail(Model model, @RequestParam("email") String email, HttpServletRequest request) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if(usuario.isPresent()){
            emailService.sendEmail(Arrays.asList(usuario.get().getEmail())
                                 , "Recuperação da Senha de Acesso"
                                 , "Olá " + usuario.get().getNome() + ",\n" +
                                         "Como solicitado através do ATIVA, estamos enviado a sua " +
                                         "senha de acesso por meio do e-mail cadastrado por você.\n\n" +
                                         "Senha: " + usuario.get().getSenha() + "\n\n" +
                                         "Saudações da Equipe ATIVA!");

            model.addAttribute(SUCESS, "O e-mail foi enviado com sucesso!");
            return RECOVER_PASSWORD;
        }else{
            model.addAttribute(ERROR, "O e-mail não foi encontrado!");
            return RECOVER_PASSWORD;
        }
    }

    @GetMapping("/forgetpassword")
    public String sendPassword(Usuario usuario) {
        return RECOVER_PASSWORD;
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().setAttribute(USUARIO, null);

        return LOGIN;
    }
}
