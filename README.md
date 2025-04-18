The Knife

CONFIGURAZIONE INIZIALE:

Per utilizzare il programma è necessario configurare javaFX. Passaggi:

1) Scaricare javaFX
2) Unzippare la cartella e copiare il percorso della cartella lib
3) Per visual studio code:
    - Aprire la cartella TheKnife del progetto
    - Andare su Run -> Add Configuration...
    - Viene creato un file launch.json nella cartella .vscode, modificarlo inserendo questo codice (sostituire <javaFX_path> con il percorso copiato al passaggio 2):

        ,{
            "type": "java",
            "name": "Launch JavaFX App",
            "request": "launch",
            "mainClass": "loginfx.Login",
            "vmArgs": "--module-path <javaFX_path> --add-modules javafx.controls,javafx.fxml"
        }

nota: c'è un file "esempio.json" nella cartella principale del progetto, assicurarsi che il proprio launch.json assomigli all'esempio

------------------------------------------------------------------------------------------------------------------------------------------------------------------------

PASSAGGI PER CONTRIBUIRE:

1) git status (verificare di essere sul branch main)
2) git pull 
3) git checkout -b nome_branch (crea un nuovo branch, esempio: grafica/principale)
4) git add -A (a fine modifiche)
5) git commit -m "messaggio" (esempio: "aggiunta schermata principale")
6) git checkout main
7) git merge nome_branch

Se ci sono dei conflitti (indicati dal terminale)ç

8) Risolvere i conflitti (modificare i file in conflitto)
9) git add .
10) git commit -m "merge main-nome_branch"

11) git branch -d nome_branch (se è stato eseguito il merge correttamente, il branch viene eliminato)

nota: è meglio aprire e chiudere tanti branch uguali, facendo spesso merge, in modo da ridurre il rischio di conflitti

------------------------------------------------------------------------------------------------------------------------------------------------------------------------

LOG MODIFICHE:

18-04-2025: - riorganizzazione readme + esempio.json (fgirlanda)
            - modifica struttura src

------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Progetto di laboratorio per Università degli studi dell'Insubria, corso di Informatica, a cura di Girlanda Francesco e Lambertoni Mattia.

Il progetto è ancora in fase di progettazione, è disponibile il documento relativo, in lavorazione, scritto in Latex (formato tex)

Link per visualizzare e modificare il documento di progettazione: https://it.overleaf.com/6875229844vtytrbxfcyzc#8e5919

