package com.ifpb.classup.parser;

import com.google.api.services.classroom.model.Course;
import com.ifpb.classup.model.Curso;

public class CourseToCursoParser {

    public static Curso parseToCurso(Course course, boolean criadoNoSistema) {
        if (course == null) {
            throw new IllegalArgumentException("O objeto Course não pode ser nulo.");
        }
        System.err.println(course.getEnrollmentCode());

        Curso curso = new Curso();
        curso.setId(course.getId());
        curso.setDataCriacao(course.getCreationTime());
        curso.setDescricao(course.getDescription());
        curso.setCodigoAcesso(course.getEnrollmentCode());
        curso.setIdProprietario(course.getOwnerId());
        curso.setSection(course.getSection());
        curso.setTitulo(course.getName());
        curso.setStatus(course.getCourseState());
        curso.setLinkAlternativo(course.getAlternateLink());
        curso.setUltimaModificacao(course.getUpdateTime());
        curso.setCriadoNoSistema(criadoNoSistema);
        curso.setNome(course.getName());

        return curso;
    }
}

