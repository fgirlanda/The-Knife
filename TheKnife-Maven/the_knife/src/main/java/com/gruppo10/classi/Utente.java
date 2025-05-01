package com.gruppo10.classi;

import java.time.LocalDate;

public class Utente {
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private LocalDate dataDiNascita;
    private String indirizzo;
    private Ruolo ruolo;

    // Enum per il ruolo
    public enum Ruolo {
        CLIENTE,
        RISTORATORE,
        NON_REGISTRATO
    }

    // Costruttore
    public Utente(String nome, String cognome, String username, String password, LocalDate dataDiNascita, String indirizzo, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataDiNascita = dataDiNascita;
        this.indirizzo = indirizzo;
        this.ruolo = ruolo;
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    // Metodo toString per rappresentazione testuale
    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", dataDiNascita=" + dataDiNascita +
                ", indirizzo='" + indirizzo + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }
}
