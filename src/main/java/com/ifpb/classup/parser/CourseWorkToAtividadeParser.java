package com.ifpb.classup.parser;

import com.google.api.services.classroom.model.CourseWork;
import com.ifpb.classup.model.Atividade;

public class CourseWorkToAtividadeParser {

    public static Atividade parseToAtividade(CourseWork courseWork) {
        if (courseWork == null) {
            throw new IllegalArgumentException("O objeto CourseWork não pode ser nulo.");
        }

        Atividade atividade = new Atividade();
        atividade.setTitulo(courseWork.getTitle());
        atividade.setDescricao(courseWork.getDescription());
        atividade.setId(courseWork.getId());
        atividade.setIdCurso(courseWork.getCourseId());
        atividade.setPontuacaoMaxima(courseWork.getMaxPoints());
        atividade.setStatus(courseWork.getState());
        atividade.setUltimaModificacao(courseWork.getUpdateTime());
        atividade.setLink(courseWork.getAlternateLink());

        return atividade;
    }
}

