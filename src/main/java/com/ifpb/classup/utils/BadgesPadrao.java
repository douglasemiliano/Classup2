package com.ifpb.classup.utils;

import com.ifpb.classup.model.Badge;

import java.util.List;

public class BadgesPadrao {

    public static final Badge COMPLETAR_DUAS_ATIVIDADES = new Badge(
            "Completar 2 Atividades",
            "Concedido a quem completa 2 atividades.");

    public static final Badge COMPLETAR_DEZ_ATIVIDADES = new Badge(
            "Completar 10 Atividades",
            "Concedido a quem completa 10 atividades.");

    public static final Badge ESTRELA_DE_DEDICACAO = new Badge(
            "Estrela de Dedicação",
            "Concedido a quem demonstra esforço contínuo."
    );

    // Método para obter todos os badges padrões como lista
    public static List<Badge> obterTodosBadgesPadrao() {
        return List.of(
                COMPLETAR_DUAS_ATIVIDADES,
                COMPLETAR_DEZ_ATIVIDADES,
                ESTRELA_DE_DEDICACAO
        );
    }
}
