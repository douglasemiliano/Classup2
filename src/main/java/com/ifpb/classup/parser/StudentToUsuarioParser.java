package com.ifpb.classup.parser;

import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.Student;
import com.google.api.services.classroom.model.UserProfile;
import com.ifpb.classup.model.Atividade;
import com.ifpb.classup.model.Usuario;
import com.ifpb.classup.utils.BadgesPadrao;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.Arrays;

public class StudentToUsuarioParser {

    public static Usuario parseToUsuario(Student student, UserProfile userGoogle) {
        if (student == null) {
            throw new IllegalArgumentException("O objeto Student não pode ser nulo.");
        }

        Usuario usuario = new Usuario();
        usuario.setId(userGoogle.getId());
        usuario.setNome(userGoogle.getName().getFullName());
        usuario.setEmail(userGoogle.getEmailAddress());
        usuario.setFoto(userGoogle.getPhotoUrl());
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setBadges(Arrays.asList(
                BadgesPadrao.COMPLETAR_DUAS_ATIVIDADES,
                BadgesPadrao.ESTRELA_DE_DEDICACAO));

        return usuario;
    }
}

