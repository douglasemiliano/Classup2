package com.ifpb.classup.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Badges")
public class Badge {
    @Id
    private String id;
    private String nome;
    private String descricao;
    private LocalDateTime dataObtida;

    public Badge(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataObtida = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataObtida() {
        return dataObtida;
    }

    public void setDataObtida(LocalDateTime dataObtida) {
        this.dataObtida = dataObtida;
    }
}

