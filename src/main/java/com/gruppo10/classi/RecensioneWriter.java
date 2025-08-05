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
    private static final String nomeFile = "fileCSV/recensioni.csv";

    public void scriviRecensione(Recensione recensione) {
        File dir = new File("fileCSV");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("Errore: impossibile creare la directory fileCSV");
                return;
            }
        }

        File fileRecensioni = new File(dir, "recensioni.csv");
        boolean fileEsiste = fileRecensioni.exists();

        Writer writer = null;
        CSVWriter csvWriter = null;

        try {
            writer = new FileWriter(fileRecensioni, true);
            csvWriter = new CSVWriter(writer);

            // Scrivi header se il file non esiste
            if (!fileEsiste) {
                String[] header = { "ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta" };
                csvWriter.writeNext(header);
                csvWriter.flush();
            }

            // Estrai e scrivi i dati
            String[] dati = estraiDati(recensione, fileRecensioni);
            csvWriter.writeNext(dati);
            csvWriter.flush();

        } catch (IOException e) {
            System.err.println("Errore di I/O durante la scrittura della recensione: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("Errore: recensione inesistente o dati mancanti.");
        } catch (SecurityException e) {
            System.err.println("Permesso negato per la scrittura del file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Errore nei dati della recensione: " + e.getMessage());
        } finally {
            // Chiudi risorse
            try {
                if (csvWriter != null) csvWriter.close();
                if (writer != null) writer.close();
            } catch (IOException e) {
                System.err.println("Errore nella chiusura delle risorse: " + e.getMessage());
            }
        }
    }


    private String[] estraiDati(Recensione recensione, File file) throws FileNotFoundException, IOException {
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
        if (recensione == null || risposta == null) {
            System.err.println("Errore: recensione o risposta null.");
            return;
        }

        List<String[]> listaTemp = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
            String[] dati;
            try {
                reader.readNext(); // Salta header
                
                while ((dati = reader.readNext()) != null) {
                    if (dati.length < 7) {
                        System.err.println("Riga CSV non valida, ignorata: " + Arrays.toString(dati));
                        continue;
                    }
                    
                    try {
                        int idUt = Integer.parseInt(dati[2]);
                        int idRis = Integer.parseInt(dati[3]);
                        
                        if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                            dati[6] = risposta;
                        }

                        listaTemp.add(dati);

                    } catch (NumberFormatException e) {
                        System.err.println("Errore parsing ID nella riga: " + Arrays.toString(dati));
                    }
                    
                }

            } catch (CsvValidationException e) {
                System.err.println("Errore formato csv: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.err.println("Errore: file CSV non trovato: " + nomeFile);
            return;
        } catch (IOException e) {
            System.err.println("Errore di lettura dal file CSV: " + e.getMessage());
            return;
        }

        // Sovrascrivi il file con i dati aggiornati
        try (CSVWriter writer = new CSVWriter(new FileWriter(nomeFile))) {
            writer.writeNext(new String[]{"ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"});
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
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
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
            System.err.println("Errore: file CSV non trovato: " + nomeFile);
            return;

        } catch (IOException e) {
            System.err.println("Errore di lettura dal file CSV: " + e.getMessage());
            return;

        } catch (com.opencsv.exceptions.CsvValidationException e) {
            System.err.println("Errore di validazione CSV: " + e.getMessage());
            return;
        }

        // Scrittura del CSV aggiornato
        try (CSVWriter writer = new CSVWriter(new FileWriter(nomeFile))) {
            writer.writeNext(new String[]{"ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo", "Risposta"});
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
        try (CSVReader reader = new CSVReader(new FileReader(nomeFile))) {
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
            System.err.println("Errore: file CSV non trovato: " + nomeFile);
            return;

        } catch (IOException e) {
            System.err.println("Errore di lettura dal file CSV: " + e.getMessage());
            return;

        } catch (com.opencsv.exceptions.CsvValidationException e) {
            System.err.println("Errore di validazione CSV: " + e.getMessage());
            return;
        }

        // Scrittura del CSV aggiornato (senza cancellazione preventiva)
        try (CSVWriter writer = new CSVWriter(new FileWriter(nomeFile))) {
            writer.writeNext(new String[]{
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
