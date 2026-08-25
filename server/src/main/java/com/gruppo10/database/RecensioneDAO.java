/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;

/** Gestisce la persistenza PostgreSQL delle recensioni. */
public class RecensioneDAO extends BasicDAO {

    /** Query di base per leggere le recensioni con i relativi dati principali. */
    private static final String SELECT_BASE = """
            SELECT id_cliente, id_ristorante, id_recensione, username,
                   voto, testo, risposta
            FROM recensioni
            """;

    /** @return tutte le recensioni ordinate per identificativo @throws SQLException se la lettura fallisce */
    public List<Recensione> trovaTutte() throws SQLException {
        return selezionaLista(SELECT_BASE + " ORDER BY id_recensione", this::creaRecensione);
    }

    /** Alias del caricamento completo precedentemente svolto da RecensioneCSV. */
    public List<Recensione> caricaRecensioni() throws SQLException {
        return trovaTutte();
    }

    /** @param idRecensione identificativo da cercare @return recensione trovata, se presente @throws SQLException se la lettura fallisce */
    public Optional<Recensione> cercaPerId(int idRecensione) throws SQLException {
        return selezionaUnica(SELECT_BASE + " WHERE id_recensione = ?", this::creaRecensione, idRecensione);
    }

    /** @param idRistorante identificativo del ristorante @return recensioni associate @throws SQLException se la lettura fallisce */
    public List<Recensione> trovaPerRistorante(int idRistorante) throws SQLException {
        return selezionaLista(SELECT_BASE + " WHERE id_ristorante = ? ORDER BY id_recensione",
                this::creaRecensione, idRistorante);
    }

    /** @param idUtente identificativo del cliente @return recensioni scritte dal cliente @throws SQLException se la lettura fallisce */
    public List<Recensione> trovaPerUtente(int idUtente) throws SQLException {
        return selezionaLista(SELECT_BASE + " WHERE id_cliente = ? ORDER BY id_recensione",
                this::creaRecensione, idUtente);
    }

    /**
     * Restituisce le recensioni scritte da un utente associando a ciascuna il
     * relativo ristorante completo di tutte le sue recensioni.
     *
     * <p>Questa forma è adatta al profilo cliente: la card può mostrare il nome
     * del ristorante e aprirne la pagina senza lavorare su un oggetto
     * incompleto.</p>
     */
    public List<Recensione> trovaPerUtenteConRistorante(int idUtente) throws SQLException {
        String sql = """
                WITH ristoranti_recensiti AS (
                    SELECT DISTINCT id_ristorante
                    FROM recensioni
                    WHERE id_cliente = ?
                )
                SELECT r.id_ristorante, r.nome, r.indirizzo, r.delivery,
                       r.prenotazione_online, r.tipo_cucina, r.prezzo,
                       r.descrizione, r.latitudine, r.longitudine, r.proprietario,
                       COALESCE((SELECT AVG(media.voto)
                                 FROM recensioni media
                                 WHERE media.id_ristorante = r.id_ristorante), 0)
                           AS media_recensioni,
                       rec.id_cliente, rec.id_recensione, rec.username,
                       rec.voto, rec.testo, rec.risposta
                FROM ristoranti r
                JOIN ristoranti_recensiti rr
                  ON rr.id_ristorante = r.id_ristorante
                LEFT JOIN recensioni rec
                  ON rec.id_ristorante = r.id_ristorante
                ORDER BY r.id_ristorante, rec.id_recensione
                """;

        List<Ristorante> ristoranti = eseguiQuery(sql,
                result -> new RistoranteDAO().estraiRistoranti(result), idUtente);
        return ristoranti.stream()
                .flatMap(ristorante -> ristorante.getRecensioni().stream())
                .filter(recensione -> recensione.getIdUtente() == idUtente)
                .toList();
    }

    /**
     * Controlla se un cliente ha già recensito un determinato ristorante.
     */
    public boolean esisteRecensione(int idUtente, int idRistorante) throws SQLException {
        return selezionaBooleano("""
                SELECT EXISTS (
                    SELECT 1
                    FROM recensioni
                    WHERE id_cliente = ? AND id_ristorante = ?
                )
                """, idUtente, idRistorante);
    }

    /** @param recensione recensione da salvare @return recensione aggiornata con l'ID generato @throws SQLException se il salvataggio fallisce */
    public Recensione aggiungiRecensione(Recensione recensione) throws SQLException {
        validaPerInserimento(recensione);
        String sql = """
                INSERT INTO recensioni (
                    id_cliente, id_ristorante, username, voto, testo, risposta
                ) VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id_recensione
                """;
        int id = inserisciERitornaId(sql, "recensioni_cliente_ristorante_unique",
                RecensioneDuplicataException::new,
                recensione.getIdUtente(), recensione.getIdRistorante(), recensione.getUsername(),
                recensione.getStelle(), recensione.getTesto(), recensione.getRisposta());
        recensione.setIdRec(id);
        return recensione;
    }

    /** @param idRecensione identificativo della recensione @param risposta testo della risposta @return {@code true} se l'aggiornamento è avvenuto @throws SQLException se l'aggiornamento fallisce */
    public boolean aggiungiRisposta(int idRecensione, String risposta) throws SQLException {
        richiediNonNull(risposta, "La risposta non può essere null");
        return aggiorna("UPDATE recensioni SET risposta = ? WHERE id_recensione = ?",
                risposta, idRecensione) == 1;
    }

    /**
     * Aggiorna la risposta contenuta nell'oggetto recensione dopo averla salvata.
     *
     * @param recensione recensione da aggiornare
     * @param risposta testo della risposta
     * @return {@code true} se l'aggiornamento è avvenuto
     * @throws SQLException se l'aggiornamento fallisce
     * @throws IllegalArgumentException se la recensione o il suo ID non sono validi
     */
    public boolean aggiungiRisposta(Recensione recensione, String risposta) throws SQLException {
        richiediNonNull(recensione, "La recensione non può essere null");
        validaIdRecensione(recensione.getIdRec());
        boolean aggiornata = aggiungiRisposta(recensione.getIdRec(), risposta);
        if (aggiornata) {
            recensione.setRisposta(risposta);
        }
        return aggiornata;
    }

    /** @param idRecensione identificativo della recensione @param testo nuovo testo @param voto nuovo voto da 1 a 5 @return {@code true} se la modifica è avvenuta @throws SQLException se l'aggiornamento fallisce */
    public boolean modificaRecensione(int idRecensione, String testo, int voto) throws SQLException {
        validaTestoEVoto(testo, voto);
        return aggiorna("UPDATE recensioni SET testo = ?, voto = ? WHERE id_recensione = ?",
                testo, voto, idRecensione) == 1;
    }

    /**
     * Modifica una recensione e aggiorna l'oggetto in memoria se il salvataggio riesce.
     *
     * @param recensione recensione da modificare
     * @param testo nuovo testo della recensione
     * @param voto nuovo voto da 1 a 5
     * @return {@code true} se la modifica è avvenuta
     * @throws SQLException se l'aggiornamento fallisce
     * @throws IllegalArgumentException se i dati o l'ID non sono validi
     */
    public boolean modificaRecensione(Recensione recensione, String testo, int voto)
            throws SQLException {
        richiediNonNull(recensione, "La recensione non può essere null");
        validaTestoEVoto(testo, voto);
        validaIdRecensione(recensione.getIdRec());
        boolean aggiornata = modificaRecensione(recensione.getIdRec(), testo, voto);
        if (aggiornata) {
            recensione.setTesto(testo);
            recensione.setStelle(voto);
        }
        return aggiornata;
    }

    /** @param idRecensione identificativo della recensione @return {@code true} se la cancellazione è avvenuta @throws SQLException se la cancellazione fallisce */
    public boolean rimuoviRecensione(int idRecensione) throws SQLException {
        return aggiorna("DELETE FROM recensioni WHERE id_recensione = ?", idRecensione) == 1;
    }

    /**
     * Rimuove una recensione usando l'ID contenuto nell'oggetto.
     *
     * @param recensione recensione da rimuovere
     * @return {@code true} se la recensione è stata rimossa
     * @throws SQLException se la cancellazione fallisce
     * @throws IllegalArgumentException se la recensione o il suo ID non sono validi
     */
    public boolean rimuoviRecensione(Recensione recensione) throws SQLException {
        richiediNonNull(recensione, "La recensione non può essere null");
        validaIdRecensione(recensione.getIdRec());
        return rimuoviRecensione(recensione.getIdRec());
    }

    /**
     * Crea una recensione leggendo la riga corrente del result set.
     *
     * @param result result set posizionato sulla riga da leggere
     * @return recensione ricostruita
     * @throws SQLException se la lettura fallisce
     */
    private Recensione creaRecensione(ResultSet result) throws SQLException {
        Recensione recensione = new Recensione();
        recensione.setIdUtente(result.getInt("id_cliente"));
        recensione.setIdRistorante(result.getInt("id_ristorante"));
        recensione.setIdRec(result.getInt("id_recensione"));
        recensione.setUsername(result.getString("username"));
        recensione.setStelle(result.getInt("voto"));
        recensione.setTesto(result.getString("testo"));
        String risposta = result.getString("risposta");
        recensione.setRisposta(risposta == null ? "" : risposta);
        return recensione;
    }

    /** Verifica i dati obbligatori necessari per inserire una recensione. */
    private void validaPerInserimento(Recensione recensione) {
        richiediNonNull(recensione, "La recensione non può essere null");
        richiediNonNull(recensione.getUsername(), "Lo username è obbligatorio");
        richiediNonNull(recensione.getTesto(), "Il testo è obbligatorio");
        validaTestoEVoto(recensione.getTesto(), recensione.getStelle());
    }

    /**
     * Verifica che testo e voto rispettino i vincoli applicativi.
     *
     * @param testo testo della recensione
     * @param voto voto compreso tra 1 e 5
     */
    private void validaTestoEVoto(String testo, int voto) {
        richiediNonNull(testo, "Il testo non può essere null");
        if (voto < 1 || voto > 5) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5");
        }
    }

    /**
     * Verifica che l'ID della recensione sia positivo.
     *
     * @param idRecensione identificativo da verificare
     */
    private void validaIdRecensione(int idRecensione) {
        if (idRecensione <= 0) {
            throw new IllegalArgumentException(
                    "Per modificare o rimuovere una recensione serve un ID valido");
        }
    }

    /** @param idRec identificativo della recensione @param id identificativo del cliente @return {@code true} se il cliente è proprietario della recensione @throws SQLException se la verifica fallisce */
    public boolean isRecensioneOwner(int idRec, int id) throws SQLException {
        return selezionaBooleano("SELECT EXISTS (SELECT 1 FROM recensioni WHERE id_recensione = ? AND id_cliente = ?)",
                idRec, id);
    }
}