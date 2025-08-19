/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.util.ArrayList;
import java.util.List;

import com.gruppo10.classi.GestionePreferiti;
import com.gruppo10.classi.Recensione;
import com.gruppo10.classi.RecensioneCSV;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.RistoranteCSV;
import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ProfiloClienteController extends Controller{

    private Utente utenteLoggato = LoginController.utenteLoggato;

    private List<Ristorante> ristoranti;

    private List<Recensione> recensioni;

    @FXML
    private VBox contenitoreTessereRis;
    @FXML
    private VBox contenitoreTessereRec;


    public void caricaDati(){
        caricaDatiUtente();

        // Carica ristoranti preferiti
        RistoranteCSV ristoranteCSV = new RistoranteCSV();
        ristoranti = ristoranteCSV.caricaCSV();
        if (ristoranti != null) {
            List<Ristorante> listaRisFiltrata = filtraPreferiti(ristoranti);
            SceneManager.caricaTessere(
                    listaRisFiltrata,
                    contenitoreTessereRis,
                    stage,
                    "/GUI/card_ristorante.fxml",
                    (controller, _) -> {
                        ((CardRistoranteController) controller).setPrincipale(false);
                        ((CardRistoranteController) controller).setStage(stage);
                        ((CardRistoranteController) controller).setOnClick();
                    });

            // Carica recensioni utente
            RecensioneCSV recensioneCSV = new RecensioneCSV();
            recensioni = recensioneCSV.caricaCSV();
            if (recensioni != null) {
                List<Recensione> listaRecFiltrata = filtraRecensioni(recensioni);
                SceneManager.caricaTessere(
                        listaRecFiltrata,
                        contenitoreTessereRec,
                        stage,
                        "/GUI/card_recensione.fxml",
                        (controller, _) -> {
                            // ((CardRecensioneController) controller).setRistorante(null); // Necessario se
                            // si vuole fare in modo di aprire la pagina ristorante dalla card recensione
                            ((CardRecensioneController) controller).setPrincipale(false);
                        });
            }
        }
    }


    private List<Ristorante> filtraPreferiti(List<Ristorante> listaRistoranti) {
        List<Ristorante> nuovaLista = new ArrayList<>();

        for (Ristorante r : listaRistoranti) {
            if (GestionePreferiti.controlloPreferito(utenteLoggato.getId(), r.getId())) {
                nuovaLista.add(r);
            }
        }
        return nuovaLista;
    }

    private List<Recensione> filtraRecensioni(List<Recensione> listaRecensioni) {
        List<Recensione> nuovaLista = new ArrayList<>();

        for (Recensione r : listaRecensioni) {
            if (r.getIdUtente() == utenteLoggato.getId()) {
                nuovaLista.add(r);
            }
        }

        return nuovaLista;
    }

    @FXML
    private void tornaIndietro() {
        SceneManager.apriPaginaPrincipale(stage);
    }
}
