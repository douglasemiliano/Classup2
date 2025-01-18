package com.ifpb.classup.service;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import com.ifpb.classup.DTO.AlunoQueConcluiuAtividadeDto;
import com.ifpb.classup.DTO.AlunoRankingDto;
import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.model.Badge;
import com.ifpb.classup.model.Usuario;
import com.ifpb.classup.repository.BadgeRepository;
import com.ifpb.classup.repository.UsuarioRepository;
import com.ifpb.classup.utils.BadgesPadrao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Classroom initializeClassroomClient(String accessToken) throws GeneralSecurityException, IOException {
        Credential credentials = new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(accessToken);

        return new Classroom.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credentials
        ).setApplicationName("Classroom App").build();
    }


    public Usuario getGoogleProfile(String accessToken) throws IOException {
        try {
            // Inicializar o cliente do Google Classroom
            Classroom service = initializeClassroomClient(accessToken);

            // Obter o perfil do usuário
            UserProfile userGoogle = service.userProfiles().get("me").execute();

            // Mapear os dados para o modelo `Usuario`
            Usuario usuario = new Usuario();
            usuario.setId(userGoogle.getId());
            usuario.setNome(userGoogle.getName().getFullName());
            usuario.setEmail(userGoogle.getEmailAddress());
            usuario.setFoto(userGoogle.getPhotoUrl());
            usuario.setCriadoEm(LocalDateTime.now());
            usuario.setBadges(Arrays.asList(
                    BadgesPadrao.COMPLETAR_DUAS_ATIVIDADES,
                    BadgesPadrao.ESTRELA_DE_DEDICACAO
            ));

            // Salvar no repositório
            return this.usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new IOException("Erro ao obter o perfil do Google: " + e.getMessage(), e);
        }
    }

}
