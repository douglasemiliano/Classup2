package com.ifpb.classup.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.oauth2.Oauth2Scopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthService {
    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    private static final String REDIRECT_URI = "http://localhost:8081/Callback";

    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> scopes = Arrays.asList(
            ClassroomScopes.CLASSROOM_COURSES,
            ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS,
            ClassroomScopes.CLASSROOM_COURSES,
            ClassroomScopes.CLASSROOM_ROSTERS,
            ClassroomScopes.CLASSROOM_PROFILE_EMAILS,
            ClassroomScopes.CLASSROOM_PROFILE_PHOTOS,
            Oauth2Scopes.USERINFO_PROFILE
    );

    private static Credential storedCredential; // Variável estática para armazenar as credenciais

    public String getAuthUrl() throws GeneralSecurityException, IOException {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientId,
                clientSecret,
                scopes
        ).setAccessType("offline") // Permite obter o refresh token
                .build();

        return flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .build();
    }

    public void saveCredentials(String authorizationCode) throws IOException, GeneralSecurityException {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientId,
                clientSecret,
                scopes
        ).setAccessType("offline")
                .build();

        // Trocando o código de autorização pelo token
        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                .setRedirectUri(REDIRECT_URI)
                .execute();

        // Criando as credenciais com base no token recebido
        storedCredential = flow.createAndStoreCredential(tokenResponse, "user");
    }

    public Credential getCredentials() {
        return storedCredential; // Retorna as credenciais armazenadas
    }

    public void handleAuthCode(String code) throws IOException, GeneralSecurityException {
        // Configure o fluxo de autenticação
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientId,
                clientSecret,
                scopes
        ).setAccessType("offline").build();

        // Troque o código pelo token
        TokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(REDIRECT_URI)
                .execute();

        Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

        // Armazene ou utilize as credenciais como preferir
        System.out.println("Token de acesso obtido: " + credential.getAccessToken());
    }
}
