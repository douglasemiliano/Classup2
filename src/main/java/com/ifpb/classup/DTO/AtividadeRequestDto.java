package com.ifpb.classup.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtividadeRequestDto {
    String titulo;
    String descricao;
    Integer dia;
    Integer mes;
    Integer ano;
    Integer horas;
    Integer minutos;
    Double pontuacaoMaxima;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public Double getPontuacaoMaxima() {
        return pontuacaoMaxima;
    }

    public void setPontuacaoMaxima(Double pontuacaoMaxima) {
        this.pontuacaoMaxima = pontuacaoMaxima;
    }
}