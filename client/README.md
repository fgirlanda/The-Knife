# Client

## Obiettivo

Il modulo client contiene l'interfaccia grafica dell'applicazione e la logica di interazione con l'utente. È sviluppato in JavaFX e si collega ai servizi del server tramite RMI.

## Funzionalità principali

- login e registrazione;
- visualizzazione della home dei ristoranti;
- ricerca e filtri;
- gestione dei preferiti;
- visualizzazione e modifica del profilo utente;
- aggiunta, modifica e rimozione recensioni;
- gestione delle risposte alle recensioni.

## Struttura consigliata

```text
client/
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           └── GUI/
├── pom.xml
└── target/
```

## Avvio

```bash
mvn -f client/pom.xml javafx:run
```

## Documentazione correlata

- [../README.md](../README.md)
- [../doc/UML/Sequence.md](../doc/UML/Sequence.md)
