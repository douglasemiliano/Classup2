package com.ifpb.classup.model;

public class Preferencias {
    private String tema;
    private Notificacoes notificacoes;

    // Getters e Setters

    public static class Notificacoes {
        private boolean email;
        private boolean push;

        // Getters e Setters
    }
}
