# MANUALE UTENTE – THE KNIFE

**Università degli Studi dell'Insubria**  
Dipartimento di Scienze Teoriche e Applicate  
Laboratorio Interdisciplinare B

---

## Frontespizio

**Titolo:** The Knife - Sistema di Gestione e Recensione Ristoranti  
**Versione Documento:** 1.0  
**Data:** Agosto 2025  

**Autori:**
- Girlanda Francesco
- Lambertoni Mattia
- Gallon Gabriele

---

## Indice

1. [Introduzione](#introduzione)
2. [Installazione](#installazione)
   - [Requisiti di Sistema](#requisiti-di-sistema)
   - [Setup Ambiente](#setup-ambiente)
   - [Installazione Programma](#installazione-programma)
3. [Esecuzione e Uso](#esecuzione-e-uso)
   - [Avvio dell'Applicazione](#avvio-dellapplicazione)
   - [Registrazione e Login](#registrazione-e-login)
   - [Uso delle Funzionalità](#uso-delle-funzionalità)
   - [Dataset di Test](#dataset-di-test)
4. [Limiti della Soluzione](#limiti-della-soluzione)
5. [Sitografia e Bibliografia](#sitografia-e-bibliografia)

---

## Introduzione

**The Knife** è un'applicazione per la ricerca, la gestione e la recensione di ristoranti. Il programma consente agli utenti di:

- Cercare ristoranti in base a vari filtri (nome, posizione, valutazione);
- Aggiungere nuovi ristoranti al sistema;
- Scrivere, modificare e visualizzare recensioni;
- Gestire una lista di ristoranti preferiti;
- Gestire il proprio profilo personale;
- Visualizzare i ristoranti sulla mappa geografica.

L'applicazione è divisa in due componenti principali:
- **Client:** l'interfaccia grafica che l'utente utilizza (sviluppata con JavaFX);
- **Server:** il sistema backend che gestisce i dati e le operazioni (sviluppato in Java).

---

## Installazione

### Requisiti di Sistema

Per eseguire The Knife, il vostro computer deve avere:

- **Sistema Operativo:** Windows, macOS o Linux
- **Java Development Kit (JDK):** versione 24.0.2 o superiore
- **PostgreSQL:** versione 18.4 o superiore (per la gestione del database)
- **Apache Maven:** versione 3.9.9 o superiore (per la compilazione del progetto)
- **Spazio disponibile:** almeno 2 GB
- **Connessione internet:** necessaria per il primo download delle dipendenze

#### Verificare l'Installazione

Aprire un terminale o prompt dei comandi e verificare che tutti i componenti siano installati:

```bash
java -version
mvn -version
psql --version
```

Se uno di questi comandi fallisce, installare il componente mancante.

### Setup Ambiente

#### 1. Installare Java Development Kit (JDK)

1. Visitare il sito ufficiale di Oracle: https://www.oracle.com/java/technologies/downloads/
2. Scaricare **Java SE 24** per il vostro sistema operativo
3. Eseguire l'installatore e seguire le istruzioni
4. Aggiungere la cartella `bin` di Java al vostro PATH:
   - **Windows:** Cercare "Variabili d'ambiente" e aggiungere il percorso alla cartella `bin`
   - **macOS/Linux:** Aggiungere a `~/.bashrc` o `~/.zshrc`:
     ```bash
     export JAVA_HOME=/path/to/jdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```

#### 2. Installare PostgreSQL

1. Visitare https://www.postgresql.org/download/
2. Scaricare l'installer per il vostro sistema operativo
3. Eseguire l'installatore:
   - Durante l'installazione, annotare la **password dell'utente postgres** (vi servirà successivamente)
   - Selezionare la porta predefinita (5432)
4. Completare l'installazione

#### 3. Installare Apache Maven

1. Visitare https://maven.apache.org/download.cgi
2. Scaricare l'archivio binario (.zip o .tar.gz)
3. Estrarre l'archivio in una cartella (ad es., `C:\Maven` o `~/maven`)
4. Aggiungere la cartella `bin` di Maven al PATH:
   - **Windows:** Aggiungere `C:\Maven\bin` alle Variabili d'Ambiente
   - **macOS/Linux:** Aggiungere a `~/.bashrc` o `~/.zshrc`:
     ```bash
     export PATH=/path/to/maven/bin:$PATH
     ```

#### 4. Configurare il Database

1. Aprire un terminale e connettersi a PostgreSQL:
   ```bash
   psql -U postgres
   ```
2. Inserire la password che avete fornito durante l'installazione
3. Creare il database TheKnife:
   ```sql
   CREATE DATABASE theknife;
   ```
4. Connettersi al nuovo database:
   ```sql
   \c theknife
   ```
5. Importare lo schema del database (il file `TheKnife.sql` è incluso nel progetto):
   ```sql
   \i /percorso/a/TheKnife.sql
   ```
6. Uscire da PostgreSQL:
   ```sql
   \q
   ```

### Installazione Programma

#### Passo 1: Clonare il Progetto

1. Aprire un terminale nella cartella dove desiderate salvare il progetto
2. Eseguire il comando:
   ```bash
   git clone https://github.com/username/The-Knife.git
   cd The-Knife
   ```

#### Passo 2: Compilare il Progetto

1. Posizionarsi nella cartella principale del progetto (`The-Knife`)
2. Eseguire il comando Maven per compilare:
   ```bash
   mvn clean install
   ```
   Questo processo scaricherà tutte le dipendenze necessarie (potrebbe richiedere alcuni minuti)

#### Passo 3: Configurare le Credenziali del Database

Prima di avviare il server, è necessario configurare le credenziali di connessione al database:

1. Nel progetto, localizzare il file di configurazione del database
2. Modificare i seguenti parametri:
   - **Host:** `localhost` (se il database è sulla stessa macchina)
   - **Porta:** `5432` (porta predefinita di PostgreSQL)
   - **Username:** `postgres` (o l'utente che avete creato)
   - **Password:** la password che avete impostato durante l'installazione di PostgreSQL
   - **Database:** `theknife`

---

## Esecuzione e Uso

### Avvio dell'Applicazione

L'applicazione è composta da due componenti: il **Server** e il **Client**.

#### 1. Avviare il Server

1. Aprire un terminale nella cartella principale del progetto
2. Posizionarsi nella cartella del server:
   ```bash
   cd server
   ```
3. Eseguire il server:
   ```bash
   mvn javafx:run
   ```
4. Si aprirà una finestra intitolata **"Pannello Amministrativo"**
5. Nel pannello, inserire le credenziali del database PostgreSQL:
   - **Host:** localhost
   - **Porta:** 5432
   - **Username:** postgres (o l'utente che avete configurato)
   - **Password:** la password del database
6. Fare clic su **"Connetti al Database"**
7. Una volta connesso, fare clic su **"Avvia Server"**
8. Il server è ora in esecuzione e in ascolto per le connessioni dei client

#### 2. Avviare il Client

1. Aprire un nuovo terminale nella cartella principale del progetto
2. Posizionarsi nella cartella del client:
   ```bash
   cd client
   ```
3. Eseguire il client:
   ```bash
   mvn javafx:run
   ```
4. L'interfaccia grafica di The Knife si aprirà in una nuova finestra

### Registrazione e Login

#### Registrazione di un Nuovo Utente

1. All'avvio dell'applicazione, visualizzerete la **schermata di login**
2. Fare clic su **"Non hai un account? Registrati"**
3. Compilare il modulo di registrazione:
   - **Nome Utente:** scegliere un nome univoco (non possono esistere due account con lo stesso nome)
   - **Email:** inserire un indirizzo email valido
   - **Password:** inserire una password sicura
   - **Conferma Password:** riperitare la password
   - **Tipo di Account:** scegliere tra:
     - **Cliente:** per cercherare e recensire ristoranti
     - **Ristoratore:** per gestire i propri ristoranti
4. Fare clic su **"Registrati"**
5. Se la registrazione ha successo, potrete effettuare il login

#### Effettuare il Login

1. Dalla schermata di login, inserire:
   - **Nome Utente:** il vostro nome utente
   - **Password:** la vostra password
2. Fare clic su **"Accedi"**
3. Se le credenziali sono corrette, accederete all'applicazione

### Uso delle Funzionalità

#### Pagina Principale (Per Clienti)

Dopo il login, i clienti visualizzano la **pagina principale** con:

- **Barra di Ricerca:** permette di cercare ristoranti per nome
- **Filtri:** è possibile filtrare i ristoranti per:
  - Localizzazione geografica
  - Valutazione minima
  - Numero di recensioni
- **Elenco Ristoranti:** mostra i ristoranti disponibili con:
  - Nome, indirizzo e valutazione media
  - Numero di recensioni
  - Pulsante per visualizzare i dettagli
  - Pulsante per aggiungere ai preferiti (♥)

#### Ricerca e Visualizzazione Dettagli Ristorante

1. Cercare un ristorante utilizzando la barra di ricerca o i filtri
2. Fare clic sul ristorante desiderato per visualizzare i **dettagli completi**:
   - Nome, indirizzo, telefono, website
   - Valutazione media
   - Elenco completo delle recensioni
   - Mappa con la localizzazione del ristorante
3. È possibile:
   - Leggere le recensioni lasciate da altri utenti
   - Aggiungere il ristorante ai preferiti (♥)
   - Scrivere una nuova recensione (se non l'avete già fatto)

#### Gestione Recensioni

##### Aggiungere una Recensione

1. Visualizzare il ristorante che desiderate recensire
2. Fare clic su **"Aggiungi Recensione"**
3. Compilare il modulo:
   - **Valutazione:** scegliere un voto da 1 a 5 stelle
   - **Titolo:** scrivere un titolo breve e descrittivo
   - **Testo:** scrivere il vostro commento sulla esperienza al ristorante
4. Fare clic su **"Pubblica Recensione"**
5. La vostra recensione sarà visibile immediatamente

##### Modificare una Recensione

1. Visualizzare il ristorante la cui recensione desiderate modificare
2. Trovare la vostra recensione nell'elenco
3. Fare clic su **"Modifica"** (disponibile solo per le vostre recensioni)
4. Modificare i dati desiderati
5. Fare clic su **"Salva Modifiche"**

##### Eliminare una Recensione

1. Visualizzare il ristorante la cui recensione desiderate eliminare
2. Trovare la vostra recensione
3. Fare clic su **"Elimina"**
4. Confermare l'eliminazione nel popup che appare

#### Gestione Preferiti

1. Visualizzare un ristorante
2. Fare clic sul pulsante **♥ (Aggiungi ai Preferiti)**
3. Il ristorante sarà aggiunto alla vostra lista personale
4. Accedere alla lista dei preferiti dal menu principale
5. Per rimuovere un ristorante dai preferiti, fare clic di nuovo su **♥**

#### Gestione Profilo Personale

1. Dal menu principale, fare clic su **"Profilo"**
2. Nella pagina del profilo potete:
   - Visualizzare i vostri dati personali
   - Modificare l'email e la password
   - Visualizzare la storia delle vostre recensioni
   - Visualizzare la lista dei vostri ristoranti preferiti (per i clienti)
   - Gestire i vostri ristoranti (per i ristoratori)

#### Funzionalità per Ristoratori

Se avete registrato un account come **Ristoratore**, potrete:

1. **Aggiungere un Ristorante:**
   - Dal menu principale, fare clic su **"I Miei Ristoranti"**
   - Fare clic su **"Aggiungi Ristorante"**
   - Compilare i dati del ristorante (nome, indirizzo, telefono, website)
   - Fare clic su **"Salva"**

2. **Modificare i Dettagli del Ristorante:**
   - Selezionare il ristorante dalla lista
   - Fare clic su **"Modifica"**
   - Aggiornare i dati desiderati
   - Fare clic su **"Salva"**

3. **Visualizzare le Recensioni:**
   - Selezionare il ristorante
   - Visualizzare tutte le recensioni lasciate dai clienti
   - Possibilità di rispondere alle recensioni

4. **Eliminare un Ristorante:**
   - Selezionare il ristorante
   - Fare clic su **"Elimina"**
   - Confermare l'eliminazione

### Dataset di Test

Sono disponibili alcuni account di test per provare l'applicazione:

#### Account Cliente
- **Username:** cliente_test
- **Password:** password123
- **Email:** cliente@example.com

#### Account Ristoratore
- **Username:** ristoratore_test
- **Password:** password123
- **Email:** ristoratore@example.com

#### Ristoranti di Test
Nel database sono presenti diversi ristoranti con valutazioni e recensioni di esempio.

---

## Limiti della Soluzione

1. **Limitazioni Geografiche:** L'applicazione utilizza un database geografico locale; la precisione delle coordinate dipende dalla qualità dei dati nel database.

2. **Autenticazione:** Il sistema non implementa autenticazione a due fattori o recupero password via email.

3. **Upload di Foto:** La versione attuale non supporta l'upload di foto durante la creazione di una recensione.

4. **Lingua:** L'interfaccia è disponibile solo in italiano.

5. **Offline Mode:** L'applicazione richiede una connessione costante al server; non è disponibile una modalità offline.

6. **Gestione Della Concorrenza:** La versione attuale non gestisce scenari di accesso simultaneo a risorse critiche oltre a quanto fornito da PostgreSQL.

7. **Report e Moderazione:** Non è disponibile un sistema di report per le recensioni inappropriate.

8. **Backup Automatico:** L'applicazione non effettua backup automatico dei dati.

---

## Sitografia e Bibliografia

- **PostgreSQL Official Documentation:** https://www.postgresql.org/docs/
- **JavaFX Official Documentation:** https://openjfx.io/
- **Apache Maven Official Website:** https://maven.apache.org/
- **Java SE Platform Documentation:** https://docs.oracle.com/javase/
- **GitHub Repository:** https://github.com/username/The-Knife

---

**Fine del Manuale Utente**
