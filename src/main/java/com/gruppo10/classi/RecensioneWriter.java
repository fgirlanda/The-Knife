package com.gruppo10.classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class RecensioneWriter {
    static File dir = new File("fileCSV");
    static File fileRecensioni = new File(dir, "recensioni.csv");

    public static void scriviRecensione(Recensione recensione) {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                GestioneEccezioni.errore("Impossibile creare la directory fileCSV", null, false, null);
            }
        }

        boolean fileEsiste = fileRecensioni.exists();

        try (Writer writer = new FileWriter(fileRecensioni, true); CSVWriter csvWriter = new CSVWriter(writer)) {

            // Scrivi header se il file non esiste
            if (!fileEsiste) {
                String[] header = { "ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo",
                        "Risposta" };
                csvWriter.writeNext(header);
                csvWriter.flush();
            }

            // Estrai e scrivi i dati
            String[] dati = estraiDati(recensione, fileRecensioni);
            csvWriter.writeNext(dati);
            csvWriter.flush();

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true,
                        file -> fileRecensioni = file);
            }
        } catch (SecurityException e) {
            GestioneEccezioni.errore("Permesso negato per la scrittura del file", e.getMessage(), true,
                    file -> fileRecensioni = file);

        } catch (IllegalArgumentException e) {
            GestioneEccezioni.errore("Errore nei dati della recensione", e.getMessage(), false, null);
        }

    }

    private static String[] estraiDati(Recensione recensione, File file) {
        String[] dati = new String[7];

        dati[0] = Integer.toString(RistoranteWriter.ultimoID(file)); // Gestire eccezione in RistoranteWriter
        dati[1] = recensione.getUsername();
        dati[2] = Integer.toString(recensione.getIdUtente());
        dati[3] = Integer.toString(recensione.getIdRis());
        dati[4] = Integer.toString(recensione.getStelle());
        dati[5] = recensione.getTesto();
        dati[6] = recensione.getRisposta();

        return dati;
    }

    public static void aggiungiRisposta(Recensione recensione, String risposta) {
        List<String[]> listaTemp = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(fileRecensioni))) {
            String[] dati;
            try {
                reader.readNext(); // Salta header

                while ((dati = reader.readNext()) != null) {
                    try {
                        int idUt = Integer.parseInt(dati[2]);
                        int idRis = Integer.parseInt(dati[3]);

                        if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                            dati[6] = risposta;
                        }

                        listaTemp.add(dati);

                    } catch (NumberFormatException e) {
                        GestioneEccezioni.errore("Errore parsing ID", "Riga: " + Arrays.toString(dati), false, null);
                    }
                }

            } catch (CsvValidationException e) {
                GestioneEccezioni.errore("Errore formato csv", e.getMessage(), false, null);
            }
            // writer.writeNext(new String[] { "ID Recensione", "Username", "ID Cliente",
            // "ID Ristorante", "Voto", "Testo",
            // "Risposta" });
            // for (String[] riga : listaTemp) {
            // writer.writeNext(riga);
            // }
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true,
                        file -> fileRecensioni = file);
                return;
            }
        }

        // Sovrascrivi il file con i dati aggiornati
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileRecensioni))) {
            writer.writeNext(new String[] { "ID Recensione", "Username", "ID Cliente",
                    "ID Ristorante", "Voto", "Testo",
                    "Risposta" });
            for (String[] riga : listaTemp) {
                writer.writeNext(riga);
            }

        } catch (IOException e) {
            System.err.println("Errore di scrittura nel file CSV: " + e.getMessage());
        }
    }

    public static void modificaRecensione(Recensione recensione, String testoModificato, int nuovoVoto) {
        if (recensione == null || testoModificato == null) {
            System.err.println("Errore: recensione o testo modificato null.");
            return;
        }

        List<String[]> listaTemp = new ArrayList<>();

        // Lettura del CSV
        try (CSVReader reader = new CSVReader(new FileReader(fileRecensioni))) {
            String[] dati;
            reader.readNext(); // Salta l'header

            while ((dati = reader.readNext()) != null) {
                if (dati.length < 7) {
                    System.err.println("Riga CSV non valida, ignorata: " + Arrays.toString(dati));
                    continue;
                }

                try {
                    int idUt = Integer.parseInt(dati[2]);
                    int idRis = Integer.parseInt(dati[3]);

                    if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                        dati[4] = Integer.toString(nuovoVoto);
                        dati[5] = testoModificato;
                    }

                    listaTemp.add(dati);

                } catch (NumberFormatException e) {
                    System.err.println("Errore parsing ID nella riga: " + Arrays.toString(dati));
                }

            }

        } catch (FileNotFoundException e) {
            System.err.println("Errore: file CSV non trovato: " + fileRecensioni);
            return;

        } catch (IOException e) {
            System.err.println("Errore di lettura dal file CSV: " + e.getMessage());
            return;

        } catch (com.opencsv.exceptions.CsvValidationException e) {
            System.err.println("Errore di validazione CSV: " + e.getMessage());
            return;
        }

        // Scrittura del CSV aggiornato
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileRecensioni))) {
            writer.writeNext(new String[] { "ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo",
                    "Risposta" });
            for (String[] riga : listaTemp) {
                writer.writeNext(riga);
            }
        } catch (IOException e) {
            System.err.println("Errore di scrittura nel file CSV: " + e.getMessage());
        }
    }

    public static void rimuoviRecensione(Recensione recensione) {
        if (recensione == null) {
            System.err.println("Errore: recensione null.");
            return;
        }

        List<String[]> listaTemp = new ArrayList<>();

        // Lettura del CSV
        try (CSVReader reader = new CSVReader(new FileReader(fileRecensioni))) {
            String[] dati;
            reader.readNext(); // Salta l'header

            while ((dati = reader.readNext()) != null) {
                if (dati.length < 7) {
                    System.err.println("Riga CSV non valida, ignorata: " + Arrays.toString(dati));
                    continue;
                }

                try {
                    int idUt = Integer.parseInt(dati[2]);
                    int idRis = Integer.parseInt(dati[3]);

                    // Aggiungi tutte le righe tranne quella da rimuovere
                    if (!(idUt == recensione.getIdUtente() && idRis == recensione.getIdRis())) {
                        listaTemp.add(dati);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Errore parsing ID nella riga: " + Arrays.toString(dati));
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Errore: file CSV non trovato: " + fileRecensioni);
            return;

        } catch (IOException e) {
            System.err.println("Errore di lettura dal file CSV: " + e.getMessage());
            return;

        } catch (com.opencsv.exceptions.CsvValidationException e) {
            System.err.println("Errore di validazione CSV: " + e.getMessage());
            return;
        }

        // Scrittura del CSV aggiornato (senza cancellazione preventiva)
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileRecensioni))) {
            writer.writeNext(new String[] {
                    "ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"
            });
            for (String[] riga : listaTemp) {
                writer.writeNext(riga);
            }

        } catch (IOException e) {
            System.err.println("Errore di scrittura nel file CSV: " + e.getMessage());
        }
    }
}
