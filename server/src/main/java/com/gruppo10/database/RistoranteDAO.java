package com.gruppo10.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.gruppo10.classi.Coordinate;
import com.gruppo10.classi.Delivery;
import com.gruppo10.classi.Distanza;
import com.gruppo10.classi.MediaRecensioni;
import com.gruppo10.classi.Prenotazione;
import com.gruppo10.classi.Prezzo;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.TipoCucina;

/**
 * Gestisce la persistenza PostgreSQL dei ristoranti.
 *
 * <p>
 * I metodi di lettura caricano anche le recensioni associate, replicando il
 * comportamento che in precedenza era fornito da {@code RistoranteCSV}.
 * </p>
 */
public class RistoranteDAO extends BasicDAO {

    private static final String SELECT_COMPLETA = """
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
            LEFT JOIN recensioni rec ON rec.id_ristorante = r.id_ristorante
            """;

    /**
     * Stampa una sintesi dei ristoranti. Mantenuto per compatibilità con il
     * programma di prova {@link TestConnection}.
     */
    public void stampaRistoranti() {
        try {
            for (Ristorante ristorante : trovaTutti()) {
                System.out.println(ristorante.getId() + " - "
                        + ristorante.getNomeRistorante() + " - "
                        + ristorante.getTipoCucina());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce tutti i ristoranti, ordinati per ID, con le recensioni.
     *
     * @return lista dei ristoranti presenti nel database
     * @throws SQLException se la lettura dal database non riesce
     */
    public List<Ristorante> trovaTutti() throws SQLException {
        return eseguiQuery(SELECT_COMPLETA + " ORDER BY r.id_ristorante, rec.id_recensione",
                this::estraiRistoranti);
    }

    /**
     * Alias esplicito per il caricamento completo precedentemente svolto da
     * {@code RistoranteCSV.caricaCSV()}.
     */
    public List<Ristorante> caricaRistoranti() throws SQLException {
        return trovaTutti();
    }

    /**
     * Cerca i ristoranti applicando i filtri direttamente in PostgreSQL.
     *
     * <p>
     * I valori {@code null} e i valori enum {@code TUTTO} non aggiungono
     * condizioni alla query. Il prezzo rappresenta la soglia massima, mentre la
     * media delle recensioni rappresenta la soglia minima, mantenendo la stessa
     * semantica usata dalla pagina principale.
     * </p>
     *
     * @param nome            testo contenuto nel nome del ristorante, senza
     *                        distinzione tra
     *                        maiuscole e minuscole
     * @param cucina          tipo di cucina richiesto
     * @param prezzo          prezzo massimo richiesto
     * @param mediaRecensioni media minima richiesta
     * @param delivery        disponibilità del delivery
     * @param prenotazione    disponibilità della prenotazione online
     * @param posizioneUtente coordinate da cui calcolare la distanza
     * @param distanza        distanza massima richiesta
     * @return una nuova lista contenente i ristoranti che rispettano i filtri
     * @throws SQLException             se la ricerca nel database non riesce
     * @throws IllegalArgumentException se viene richiesta una distanza senza
     *                                  coordinate valide
     */
    public List<Ristorante> cercaConFiltri(String nome, TipoCucina cucina,
            Prezzo prezzo, MediaRecensioni mediaRecensioni, Delivery delivery,
            Prenotazione prenotazione, Coordinate posizioneUtente,
            Distanza distanza) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_COMPLETA).append(" WHERE 1 = 1");
        List<Object> parametri = new ArrayList<>();

        if (nome != null && !nome.isBlank()) {
            sql.append(" AND LOWER(r.nome) LIKE LOWER(?)");
            parametri.add("%" + nome.trim() + "%");
        }
        if (cucina != null && cucina != TipoCucina.TUTTO) {
            sql.append(" AND r.tipo_cucina = ?");
            parametri.add(cucina.name());
        }
        if (prezzo != null && prezzo != Prezzo.TUTTO) {
            sql.append(" AND CHAR_LENGTH(r.prezzo) <= ?");
            parametri.add(prezzo.getSoglia());
        }
        if (mediaRecensioni != null && mediaRecensioni != MediaRecensioni.TUTTO) {
            sql.append("""
                     AND (SELECT COALESCE(AVG(media.voto), 0)
                          FROM recensioni media
                          WHERE media.id_ristorante = r.id_ristorante) >= ?
                    """);
            parametri.add(mediaRecensioni.getSoglia());
        }
        if (delivery != null && delivery != Delivery.TUTTO) {
            sql.append(" AND r.delivery = ?");
            parametri.add(delivery == Delivery.DELIVERY_DISPONIBILE);
        }
        if (prenotazione != null && prenotazione != Prenotazione.TUTTO) {
            sql.append(" AND r.prenotazione_online = ?");
            parametri.add(prenotazione == Prenotazione.PRENOTAZIONE_ONLINE_DISPONIBILE);
        }
        if (distanza != null && distanza != Distanza.OLTRE) {
            validaPosizionePerDistanza(posizioneUtente);
            sql.append("""
                     AND 6371 * ACOS(LEAST(1.0, GREATEST(-1.0,
                         COS(RADIANS(?)) * COS(RADIANS(r.latitudine))
                         * COS(RADIANS(r.longitudine) - RADIANS(?))
                         + SIN(RADIANS(?)) * SIN(RADIANS(r.latitudine))
                     ))) <= ?
                    """);
            parametri.add(posizioneUtente.getLat());
            parametri.add(posizioneUtente.getLon());
            parametri.add(posizioneUtente.getLat());
            parametri.add(distanza.getKM());
        }

        sql.append(" ORDER BY r.id_ristorante, rec.id_recensione");

        return eseguiQuery(sql.toString(), this::estraiRistoranti, parametri.toArray());
    }

    /**
     * Cerca un ristorante tramite il suo ID e carica le recensioni associate.
     *
     * @param idRistorante ID del ristorante
     * @return il ristorante, oppure un Optional vuoto se non esiste
     * @throws SQLException se la lettura dal database non riesce
     */
    public Optional<Ristorante> cercaRistorante(int idRistorante) throws SQLException {
        List<Ristorante> risultati = eseguiQuery(
                SELECT_COMPLETA + " WHERE r.id_ristorante = ? ORDER BY rec.id_recensione",
                this::estraiRistoranti, idRistorante);
        return risultati.stream().findFirst();
    }

    /**
     * Restituisce i ristoranti appartenenti a uno specifico ristoratore.
     *
     * @param idProprietario ID dell'utente ristoratore
     * @return ristoranti del proprietario, con le rispettive recensioni
     * @throws SQLException se la lettura dal database non riesce
     */
    public List<Ristorante> trovaPerProprietario(int idProprietario) throws SQLException {
        return eseguiQuery(
                SELECT_COMPLETA + " WHERE r.proprietario = ? ORDER BY r.id_ristorante, rec.id_recensione",
                this::estraiRistoranti, idProprietario);
    }

    /**
     * Restituisce i ristoranti aggiunti ai preferiti da uno specifico utente,
     * completi delle recensioni associate.
     *
     * @param idUtente ID del cliente
     * @return ristoranti preferiti del cliente
     * @throws SQLException se la lettura dal database non riesce
     */
    public List<Ristorante> trovaPreferitiPerUtente(int idUtente) throws SQLException {
        String sql = SELECT_COMPLETA + """
                 JOIN preferiti p ON p.id_ristorante = r.id_ristorante
                 WHERE p.id_cliente = ?
                 ORDER BY r.id_ristorante, rec.id_recensione
                """;
        return eseguiQuery(sql, this::estraiRistoranti, idUtente);
    }

    /**
     * Inserisce un nuovo ristorante e assegna all'oggetto l'ID generato.
     *
     * @param ristorante ristorante da salvare
     * @return lo stesso oggetto, aggiornato con l'ID assegnato dal database
     * @throws SQLException             se l'inserimento non riesce
     * @throws IllegalArgumentException se mancano dati obbligatori
     */
    public Ristorante aggiungiRistorante(Ristorante ristorante) throws SQLException {
        validaPerInserimento(ristorante);

        String inserimentoSql = """
                INSERT INTO ristoranti (
                    nome, indirizzo, delivery,
                    prenotazione_online, tipo_cucina, prezzo, descrizione,
                    latitudine, longitudine, proprietario
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id_ristorante
                """;

        int nuovoId = inserisciERitornaId(inserimentoSql,
                ristorante.getNomeRistorante(), ristorante.getIndirizzo(),
                ristorante.getDelivery() == Delivery.DELIVERY_DISPONIBILE,
                ristorante.getPrenotazione() == Prenotazione.PRENOTAZIONE_ONLINE_DISPONIBILE,
                ristorante.getTipoCucina().name(), ristorante.getPrezzo().toString(),
                ristorante.getDescrizione(), ristorante.getCords().getLat(),
                ristorante.getCords().getLon(), ristorante.getIdproprietario());
        ristorante.setId(nuovoId);
        return ristorante;
    }

    /**
     * Calcola nel database la media delle recensioni di un ristorante.
     *
     * @param idRistorante ID del ristorante
     * @return media dei voti, oppure {@code 0.0} se non ci sono recensioni
     * @throws SQLException             se il calcolo nel database non riesce
     * @throws IllegalArgumentException se l'ID non è valido
     */
    public double calcolaMediaRecensioni(int idRistorante) throws SQLException {
        if (idRistorante <= 0) {
            throw new IllegalArgumentException("Per calcolare la media serve un ID ristorante valido");
        }
        return selezionaDouble(
                "SELECT COALESCE(AVG(voto), 0) AS media_recensioni FROM recensioni WHERE id_ristorante = ?",
                idRistorante);
    }

    /**
     * Ricalcola nel database la media e la assegna all'oggetto ristorante.
     *
     * @param ristorante ristorante da aggiornare
     * @throws SQLException             se il calcolo nel database non riesce
     * @throws IllegalArgumentException se il ristorante non è valido
     */
    public void aggiornaMediaRecensioni(Ristorante ristorante) throws SQLException {
        richiediNonNull(ristorante, "Il ristorante non può essere null");
        ristorante.setMediaRec(calcolaMediaRecensioni(ristorante.getId()));
    }

    List<Ristorante> estraiRistoranti(ResultSet result) throws SQLException {
        Map<Integer, Ristorante> ristoranti = new LinkedHashMap<>();

        while (result.next()) {
            int idRistorante = result.getInt("id_ristorante");
            Ristorante ristorante = ristoranti.get(idRistorante);
            if (ristorante == null) {
                ristorante = creaRistorante(result);
                ristoranti.put(idRistorante, ristorante);
            }

            Integer idRecensione = (Integer) result.getObject("id_recensione");
            if (idRecensione != null) {
                Recensione recensione = creaRecensione(result, ristorante, idRecensione);
                ristorante.aggiungiRecensione(recensione);
            }
        }

        return new ArrayList<>(ristoranti.values());
    }

    private Ristorante creaRistorante(ResultSet result) throws SQLException {
        Ristorante ristorante = new Ristorante();
        ristorante.setId(result.getInt("id_ristorante"));
        ristorante.setNomeRistorante(result.getString("nome"));
        ristorante.setIndirizzo(result.getString("indirizzo"));
        ristorante.setDelivery(result.getBoolean("delivery"));
        ristorante.setPrenotazioneOnline(result.getBoolean("prenotazione_online"));
        ristorante.setCucina(result.getString("tipo_cucina"));
        ristorante.setPrezzo(result.getString("prezzo").length());
        ristorante.setDescrizione(result.getString("descrizione"));
        ristorante.setCords(new Coordinate(
                result.getDouble("latitudine"), result.getDouble("longitudine")));
        ristorante.setIdproprietario(result.getInt("proprietario"));
        ristorante.setMediaRec(result.getDouble("media_recensioni"));
        return ristorante;
    }

    private Recensione creaRecensione(ResultSet result, Ristorante ristorante,
            int idRecensione) throws SQLException {
        Recensione recensione = new Recensione();
        recensione.setIdRec(idRecensione);
        recensione.setIdUtente(result.getInt("id_cliente"));
        recensione.setIdRistorante(ristorante.getId());
        recensione.setUsername(result.getString("username"));
        recensione.setStelle(result.getInt("voto"));
        recensione.setTesto(result.getString("testo"));
        String risposta = result.getString("risposta");
        recensione.setRisposta(risposta == null ? "" : risposta);
        recensione.setRistorante(ristorante);
        return recensione;
    }

    private void validaPosizionePerDistanza(Coordinate posizioneUtente) {
        if (posizioneUtente == null || posizioneUtente.getLat() == null
                || posizioneUtente.getLon() == null) {
            throw new IllegalArgumentException(
                    "Le coordinate dell'utente sono necessarie per filtrare per distanza");
        }
    }

    private void validaPerInserimento(Ristorante ristorante) {
        richiediNonNull(ristorante, "Il ristorante non può essere null");
        richiediNonNull(ristorante.getNomeRistorante(), "Il nome è obbligatorio");
        richiediNonNull(ristorante.getIndirizzo(), "L'indirizzo è obbligatorio");
        richiediNonNull(ristorante.getDelivery(), "Il delivery è obbligatorio");
        richiediNonNull(ristorante.getPrenotazione(), "La prenotazione è obbligatoria");
        richiediNonNull(ristorante.getTipoCucina(), "Il tipo di cucina è obbligatorio");
        richiediNonNull(ristorante.getPrezzo(), "Il prezzo è obbligatorio");
        richiediNonNull(ristorante.getDescrizione(), "La descrizione è obbligatoria");
        richiediNonNull(ristorante.getCords(), "Le coordinate sono obbligatorie");
        richiediNonNull(ristorante.getCords().getLat(), "La latitudine è obbligatoria");
        richiediNonNull(ristorante.getCords().getLon(), "La longitudine è obbligatoria");

        if (ristorante.getDelivery() == Delivery.TUTTO
                || ristorante.getPrenotazione() == Prenotazione.TUTTO
                || ristorante.getTipoCucina().name().equals("TUTTO")
                || ristorante.getPrezzo().toString().equals("TUTTO")) {
            throw new IllegalArgumentException(
                    "I valori di filtro TUTTO non possono essere salvati come dati del ristorante");
        }
    }

    public boolean isRistoranteOwner(int id, int id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isRistoranteOwner'");
    }
}