package com.ifpb.classup.controller;

import com.ifpb.classup.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth-url")
    public ResponseEntity<String> getAuthUrl() {
        try {
            String authUrl = authService.getAuthUrl();
            return ResponseEntity.ok(authUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar URL de autenticação: " + e.getMessage());
        }
    }

    @GetMapping("/Callback")
    public ResponseEntity<String> saveCredentials(@RequestParam("code") String code) {
        try {
            authService.saveCredentials(code);
            return ResponseEntity.ok("Autenticação realizada com sucesso e credenciais salvas!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar credenciais: " + e.getMessage());
        }
    }

}
