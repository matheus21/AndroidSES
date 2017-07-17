package app.consultas.com.br.appconsultas.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matheus Oliveira on 02/07/2017.
 */

public class Consulta implements Serializable {

    private int id;
    private int paciente_id;
    private Date data;
    private String hora;
    private int situacao_id;

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getPaciente_id() {
        return paciente_id;
    }

    public void setPaciente_id(int paciente_id) {
        this.paciente_id = paciente_id;
    }


    public int getSituacao_id() {
        return situacao_id;
    }

    public void setSituacao_id(int situacao_id) {
        this.situacao_id = situacao_id;
    }

    @Override
    public String toString() {

        String dataFormatada = new SimpleDateFormat("dd/MM/yyyy").format(data);
        String situacaoFormatada = "";

        //1 = Agendado / 2 = Confirmado / 3 = Aguardando cancelamento / 4 = Cancelada

        switch (situacao_id) {
            case 1:
                situacaoFormatada = "CONSULTA SOLICITADA";
                break;
            case 2:
                situacaoFormatada = "CONSULTA CONFIRMADA";
                break;
            case 3:
                situacaoFormatada = "AGUARDANDO CANCELAMENTO";
                break;
            case 4:
                situacaoFormatada = "CONSULTA CANCELADA";
                break;

        }

        return "\nCONSULTA - " + id + "\n" +
                "\nHorario Marcado: " + hora +
                "\nData da consulta: " + dataFormatada +
                "\nSituação da Consulta: " + situacaoFormatada + "\n";
    }
}
