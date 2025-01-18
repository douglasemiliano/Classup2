package com.ifpb.classup.controller;

import com.ifpb.classup.model.UrlResponse;
import com.ifpb.classup.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
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
                    .body("Erro ao gerar URL de autenticação.");
        }
    }

    @PostMapping("/callback")
    public ResponseEntity<String> saveCredentials(@RequestParam("code") String code) throws GeneralSecurityException, IOException {
            authService.saveCredentials(code);
            return ResponseEntity.ok("Autenticação realizada com sucesso e credenciais salvas!");

    }
}
