package com.ifpb.classup.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Getter
@Setter

@Document(collection = "Cursos")
public class Curso {

    String id;
    String dataCriacao;
    String descricao;
    String codigoAcesso;
    String idProprietario;
    String section;
    String titulo;
    String status;
    String linkAlternativo;
    String ultimaModificacao;
    Boolean criadoNoSistema;
    String nome;
    List<Usuario> alunos;

    public Curso() {
    }

    public Curso(String id, String dataCriacao, String descricao, String codigoAcesso, String idProprietario, String section, String titulo, String status, String linkAlternativo, String ultimaModificacao, Boolean criadoNoSistema, String nome, List<Usuario> alunos) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
        this.codigoAcesso = codigoAcesso;
        this.idProprietario = idProprietario;
        this.section = section;
        this.titulo = titulo;
        this.status = status;
        this.linkAlternativo = linkAlternativo;
        this.ultimaModificacao = ultimaModificacao;
        this.criadoNoSistema = criadoNoSistema;
        this.nome = nome;
        this.alunos = alunos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoAcesso() {
        return codigoAcesso;
    }

    public void setCodigoAcesso(String codigoAcesso) {
        this.codigoAcesso = codigoAcesso;
    }

    public String getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(String idProprietario) {
        this.idProprietario = idProprietario;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkAlternativo() {
        return linkAlternativo;
    }

    public void setLinkAlternativo(String linkAlternativo) {
        this.linkAlternativo = linkAlternativo;
    }

    public String getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(String ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public Boolean getCriadoNoSistema() {
        return criadoNoSistema;
    }

    public void setCriadoNoSistema(Boolean criadoNoSistema) {
        this.criadoNoSistema = criadoNoSistema;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Usuario> alunos) {
        this.alunos = alunos;
    }
}