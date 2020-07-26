package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.component.EmailService;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.entities.Usuario;
import com.utfpr.ativadi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;


@Controller
public class SessionController {
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaController auditoria;
    public static String LOGIN = "login";
    private static String USUARIO = "usuario";
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index";
    private final String RECOVER_PASSWORD = "recover_password";

    @Autowired
    public EmailService emailService;

    @Autowired
    public SessionController(UsuarioRepository usuarioRepository, AuditoriaController auditoria) {
        this.usuarioRepository = usuarioRepository;
        this.auditoria = auditoria;
    }

    private static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }

    public static boolean freeAccess(){
        Usuario user = (Usuario) getSession().getAttribute(USUARIO);

        return user == null ? false : true;
    }

    public static Usuario getUser(){
        Usuario user = (Usuario) getSession().getAttribute(USUARIO);

        return user;
    }

    @GetMapping("/")
    public String inicio(Model model, HttpServletRequest request) {
        return freeAccess() ? INICIO : LOGIN;
    }

    @PostMapping("/dologin")
    public String doLogin(Model model, @RequestParam("email") String email, @RequestParam("senha") String senha, HttpServletRequest request) throws ScriptException {
        Optional<Usuario> usuario = usuarioRepository.findByEmailSenha(email, senha);

        if(usuario.isPresent()){
            getSession().setAttribute(USUARIO, usuario.get());
            auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.LOGIN).show(), this.getClass().getSimpleName());
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
        auditoria.addAuditoria(Mensagem.getInstance(true, Mensagem.Funcao.LOGOUT).show(), this.getClass().getSimpleName());
        getSession().setAttribute(USUARIO, null);

        return LOGIN;
    }
}
