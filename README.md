# The Knife

Progetto di laboratorio per il corso di **Informatica** presso l'**Università degli Studi dell'Insubria**.

**Autori:** Girlanda Francesco, Lambertoni Mattia, Gallon Gabriele.

---

## Requisiti

Per compilare ed eseguire il progetto sono necessari:

* **Java JDK** v24.0.2
* **JavaFX** v24.0.1
* **Apache Maven** v3.9.9
* **PostgreSQL** v18.4
* **Git** (necessario per contribuire al progetto)

---

## Installazione di Maven

### 1. Download

Scaricare la versione specificata di Apache Maven dalla pagina ufficiale:

https://maven.apache.org/download.cgi

Scaricare l'archivio `.zip` e decomprimerlo in una cartella a scelta.

### 2. Configurazione delle variabili d'ambiente

Su Windows è necessario configurare le variabili d'ambiente.

#### `Path`

Aggiungere alla variabile `Path` il percorso della cartella `bin` contenuta nella directory di Maven.

Esempio:

```text
C:\Users\Pippo\Desktop\Dev Projects\Java\Maven\apache-maven-3.9.9\bin
```

#### `MAVEN_HOME`

Creare una nuova variabile di sistema denominata `MAVEN_HOME` e impostarla sul percorso della cartella principale di Maven, **senza** la cartella `bin`.

Esempio:

```text
C:\Users\Pippo\Desktop\Dev Projects\Java\Maven\apache-maven-3.9.9
```

#### `JAVA_HOME`

Verificare che `JAVA_HOME` punti alla directory principale del JDK e **non** alla relativa cartella `bin`.

Esempio:

```text
C:\Program Files\Java\jdk-24
```

È possibile verificare la corretta installazione di Java e Maven tramite:

```bash
java -version
mvn -version
```

---

## Creazione Database

1. Installare un gestore di database a scelta, per esempio **DBeaver**
2. Aprire una connessione con postgres
3. Copiare il percorso del file `TheKnife.sql`
4. Da terminale eseguire il comando:
```bash
psql -U postgres -f <percorso_file_punto3>
```

## Compilazione

Aprire un terminale nella cartella principale del progetto:

```text
The-Knife
```

ed eseguire:

```bash
mvn install
```

## Avvio dell'applicazione

L'applicazione può essere avviata direttamente da un IDE oppure tramite Maven.

### Tramite `launch.json`

Se si utilizza Visual Studio Code:

1. Aprire il progetto in Visual Studio Code.
2. Andare nella sezione **Run and Debug**.
3. Selezionare **Add Configuration**.
4. Configurare `launch.json` seguendo l'esempio presente nel file `launch_ex.json`.
5. Selezionare la configurazione **ServerBoot**
6. Eseguire tramite il pannello admin la connessione al database e l'avvio del server
7. Eseguire la configurazione **ClientBoot**


### Tramite terminale

Dalla root principale del progetto eseguire:

```bash
mvn -f server/pom.xml javafx:run 
```

Connettersi al database e avviare il server tramite appositi bottoni nel pannella admin, poi in un altro terminale:

```bash
mvn -f client/pom.xml javafx:run
```

### Tramite file jar

Il comando `mvn install` è configurato per generare anche i file jar corrispondenti ai moduli client, server e common (quest'ultimo è irrilevante)

I file .jar eseguibili si trovano nelle rispettive cartelle target all'interno dei singoli moduli

Per eseguire l'applicazione è necessario:
1. Avviare il jar del server `server\target\server-2.0-SNAPSHOT.jar`
2. Connettersi al database
3. Avviare il server 
4. Avviare il file jar del client `client\target\client-2.0-SNAPSHOT.jar`
5. 
---

# Contribuire al progetto

Per contribuire allo sviluppo è consigliato lavorare sempre su un **branch separato**, evitando di effettuare direttamente modifiche sul branch `main`.

## 1. Aggiornare il repository

Prima di iniziare a lavorare, verificare lo stato del repository:

```bash
git fetch
git status
```

Se sono presenti aggiornamenti sul repository remoto, sincronizzare il branch corrente:

```bash
git pull
```

## 2. Creare un nuovo branch

Creare un branch dedicato alla modifica che si vuole sviluppare:

```bash
git checkout -b nome_branch
```

A questo punto è possibile effettuare le modifiche al progetto.

## 3. Salvare le modifiche

Al termine del lavoro, aggiungere le modifiche allo staging:

```bash
git add -A
```

In alternativa, è possibile aggiungere solamente determinati file:

```bash
git add nome_file
```

Creare quindi un commit:

```bash
git commit -m "messaggio del commit"
```

## 4. Pubblicare il branch

Pubblicare il branch sul repository remoto:

```bash
git push origin nome_branch
```

---

# Merge di un branch

Quando il lavoro su un branch è terminato, è possibile integrarlo nel branch `main`.

### 1. Passare a `main`

```bash
git checkout main
```

### 2. Eseguire il merge

```bash
git merge nome_branch
```

### 3. Risolvere eventuali conflitti

Se Git segnala dei conflitti:

1. aprire i file indicati da Git;
2. risolvere manualmente i conflitti;
3. aggiungere i file modificati:

```bash
git add .
```

4. creare il commit del merge:

```bash
git commit -m "merge main-nome_branch"
```

### 4. Eliminare il branch

Se il merge è stato completato correttamente, è possibile eliminare il branch locale:

```bash
git branch -d nome_branch
```

Per eliminare anche il branch dal repository remoto:

```bash
git push origin --delete nome_branch
```

> **Consiglio:** è preferibile creare branch relativamente piccoli e fare merge frequenti. In questo modo si riduce il rischio di conflitti e diventa più semplice individuare eventuali problemi.

---

# Testare un merge prima di integrarlo in `main`

Se si vuole verificare un merge senza modificare direttamente `main`, è possibile utilizzare un branch temporaneo.

### 1. Creare il branch di test

Partendo da `main`:

```bash
git checkout main
git checkout -b test-merge
```

Il branch `test-merge` conterrà una copia dello stato attuale di `main`.

### 2. Eseguire il merge sul branch di test

```bash
git merge nome_branch
```

### 3. Risolvere eventuali conflitti

Se vengono rilevati conflitti, risolverli e completare il merge come descritto nella sezione precedente.

### 4. Verificare il risultato

A questo punto è possibile compilare ed eseguire il progetto sul branch `test-merge`, verificando che il merge non abbia introdotto problemi.

### 5. Integrare il risultato in `main`

Se il test ha avuto esito positivo:

```bash
git checkout main
git merge test-merge
```

---

# Lavorare su un branch già esistente

Se il branch sul quale si vuole lavorare esiste già sul repository remoto, è possibile recuperarlo con:

### 1. Aggiornare i riferimenti remoti

```bash
git fetch origin
```

### 2. Visualizzare i branch disponibili

Per visualizzare i branch presenti sul repository remoto:

```bash
git branch -r
```

Per visualizzare i branch presenti localmente:

```bash
git branch
```

Per visualizzare entrambi:

```bash
git branch -a
```

### 3. Passare al branch desiderato

```bash
git checkout nome_branch
```

---

## Branch remoto predefinito

Nell'elenco dei branch potrebbe comparire una voce simile a:

```text
origin/HEAD -> origin/main
```

Questa indicazione significa che **`main` è il branch predefinito del repository remoto `origin`**.

---

## Workflow consigliato

In generale, il flusso di lavoro consigliato è:

```text
main
 │
 ├── git checkout -b nuova-funzionalita
 │
 ├── sviluppo
 │
 ├── git add -A
 │
 ├── git commit
 │
 ├── git push origin nuova-funzionalita
 │
 └── merge → main
```

È consigliato mantenere i branch **specifici e di dimensioni contenute**, effettuando frequentemente il merge delle funzionalità completate. Questo permette di ridurre i conflitti e mantenere il progetto più semplice da gestire.


## TO DO:

### ilTacco:

- Criptare password (FATTO)
- Leggere file csv (FATTO)
- Aggiungere id al cliente (FATTO)
- Implementare nell'interfaccia (dati errati, utente inesistente, login effettuato) (FATTO)
- Collegare tasto login a pagina principale (FATTO)
- Utente loggato (FATTO)
- Username univoco (FATTO)
- Visualizzazione del profilo (FATTO)
- Login senza registrazione (+disabilita tasto preferiti) (FATTO)
- Visualizza miei ristoranti (FATTO)
- Ottimizzazione (FATTO)
- Immagini ristoranti (FATTO)

### matlmbe:

- File csv ristoranti (FATTO)
- Dialog per aggiungere (FATTO)
- Inserire card ristorante nella pagina principale (FATTO)
- Gestire ricerca e filtri (FATTO)
- Ristoranti preferiti (FATTO)
- Sistemare card ristorante (FATTO)
- Tasto filtro distanza (FATTO)
- Tasto indietro pagina ristorante (FATTO)
- Tasto-pagina aggiungi recensione (FATTO)
- Card apribile (FATTO)
- Card recensione (FATTO)
- Documentazione

### fgirlanda:

- Trova ristoranti vicini (FATTO)
- Gestire posizione utente/ristorante (FATTO)
- Calcola distanza (FATTO)
- Recensioni (file csv, classi) (FATTO)
- Sistemare tipo cucina csv (FATTO)
- Sfoltire ristoranti (50 - nomi corti - proprietario - id da 1) (FATTO)
- Funzione filtro distanza (FATTO)
- Fixare abilita/disabilita pulsanti  (FATTO)
- Ultimi fix (FATTO)

### Generale

Generale:

- File csv con coppie id utente-ristorante_preferito/recensioni (FATTO)
- ID a ristoranti e recensioni (FATTO)

- CSV recensioni (FATTO)

    - ID recensione
    - ID cliente
    - ID ristorante
    - Voto
    - Testo
    - Risposta

- Gestione ristorante aperto (FATTO)
- Rimozione recensione (FATTO)
- Modifica recensione (FATTO)
- Numero di recensioni (FATTO)
- Modificare filtri ricerca ristoranti (FATTO)
- Calcolo media recensioni modificate (FATTO)
- Disabilitare bottone risposta recensione dopo aver risposto (FATTO)
- Rendere visibile la risposta (FATTO)

Grafica:

- Fix dimensione finestra profilo (FATTO)
- Fix login status (popup al posto di label?) 
- Modificare filtri ricerca ristoranti (FATTO)
- Immagini ristoranti legate a tipo cucina (FATTO)
- Nomi ristoranti in le mie recensioni (al posto di username) (FATTO)
- Fix spazio vuoto in le mie recensioni (FATTO)
- Fix bordo recensioni (FATTO)




Pulizia codice:

- Writer e Reader non sono coerenti tra di loro (alcuni hanno metodi static altri no) (FATTO)
- Classe astratta Controller (FATTO)
- Classe astratta CSVHandler (FATTO)
- Gestione eccezioni (FATTO)
- Generalizzazione dei percorsi file (FATTO)

Issues:

- L'utente può non selezionare un indirizzo generato dalla ricerca con nominatim e il programma funziona ugualmente perchè lat non è null (per esempio indirizzo: mario) (RISOLTO)
- Non aggiorna la recensione se si modifica solo il voto (RISOLTO)
- Rotta la modifica/rimozioni di recensioni per il calcolo media - probabile causa: manca l'assegnazione del ristorante alla recensione in alcuni punti (RISOLTO)
- Stesso problema di modifica recensioni, ma solo dopo la prima modifica, che funziona correttamente (RISOLTO)
- Non funziona rispondere a una recensione, il tasto non si disabilita e la risposta non appare (RISOLTO)
- Se aggiungo una recensione, non la posso modificare (RISOLTO)
- Errore caricamento card recensioni in profilo cliente, perchè le recensioni caricate non hanno il ristorante settato (RISOLTO)


Extra:

- Whitelist caratteri (opzionale)
- Soluzione per ripetizione metodo caricaTessere (FATTO)
- Pulizia grafica (FATTO)
- Aggiungere controllo indirizzo (FATTO)

Ottimizzazioni:

- Calcolo media per un ristorante quando viene rimossa una recensione prevede .remove da Lista, che ha complessità O(n), si potrebbe usare un contatore(?)

---

## LAB-B

### TODO

diagrammi:

- interaction(?)
- package(?)

### matlambe
- er (FATTO)
- class
- implementare db (FATTO)
- creare GUI pannello admin:
  - loading/status connessione database
  - loading/status avvio server
  - lista utenti registrati
  - counter utenti connessi
  - altro(?)

### fgirlanda
- use-case
- sequence
- ottimizzare gestione db (FATTO)
- organizzare cartelle client-server (FATTO)
- fixare problemi dovuti a divisione in moduli separati (FATTO)
- implementare avvio server (FATTO)
- gestione permessi (FATTO)
- standardizzare il codice (FATTO)

### ggallon
- activity
- aggiustare istruzioni installazione (FATTO)
- aggiungere istruzioni db (FATTO)
- distribuzione client/server/database su macchine diverse

### Generale

- aggiornamento gui in risposta a eventi client

- documentazione (vedere pdf di lab-b su elearning):
  - manuale utente
  - manuale tecnico
  - javadoc (FATTO)