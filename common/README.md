# Common

## Obiettivo

Il modulo common contiene le classi condivise tra client e server: modelli di dominio, DTO, utility e tipi utilizzati in più parti del sistema.

## Contenuti principali

- entità principali: utente, ristorante, recensione, preferito;
- classi di supporto per i dati condivisi;
- strategie e utility comuni;
- eventuali tipi serializzabili usati nel passaggio di informazioni tra i moduli.

## Struttura consigliata

```text
common/
├── src/
│   └── main/
│       └── java/
├── pom.xml
└── target/
```

## Documentazione correlata

- [../README.md](../README.md)
- [../Documentazione/UML/Sequence.md](../Documentazione/UML/Sequence.md)
