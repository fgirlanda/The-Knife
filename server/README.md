# Server

## Obiettivo

Il modulo server gestisce la logica applicativa, l'accesso al database e i servizi remoti esposti al client. È il layer centrale del sistema e coordina tutte le operazioni di business.

## Funzionalità principali

- connessione al database PostgreSQL;
- servizi di autenticazione;
- gestione di ristoranti e recensioni;
- gestione profili e preferiti;
- registrazione dei servizi RMI;
- pannello amministrativo per l'avvio del server.

## Struttura consigliata

```text
server/
├── src/
│   └── main/
│       ├── java/
│       └── resources/
├── pom.xml
└── target/
```

## Avvio

```bash
mvn -f server/pom.xml javafx:run
```

## Documentazione correlata

- [../README.md](../README.md)
- [../doc/UML/Sequence.md](../doc/UML/Sequence.md)
