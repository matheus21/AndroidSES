package app.consultas.com.br.appconsultas.entity;

import java.io.Serializable;

/**
 * Created by Matheus Oliveira on 02/07/2017.
 */

public class Paciente implements Serializable{

    private int id;
    private String nome;
    private String cpf;
    private String cns;
    private String senha;
    private String email;
    private String telefone;
    private int acessou;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getAcessou() {
        return acessou;
    }

    public void setAcessou(int acessou) {
        this.acessou = acessou;
    }
}
