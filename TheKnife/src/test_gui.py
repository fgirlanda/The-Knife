import json
import subprocess
import time
import os

# === CONFIGURAZIONE ===
javafx_path = "C:\\Users\\Frank\\Desktop\\Dev Projects\\Java\\JavaFX\\javafx-sdk-24.0.1\\lib" 
launch_json_path = os.path.join(os.path.dirname(__file__), "../.vscode/launch.json")

main_classes = []
main_classes_path = os.path.join(os.path.dirname(__file__), "fileJava")
for file in os.listdir(main_classes_path):
    main_classes.append(file.split(".")[0])
    
controller_classes = []
controller_classes_path = os.path.join(os.path.dirname(__file__), "controller")
for file in os.listdir(controller_classes_path):
    if file.endswith(".java"):
        controller_classes.append(file)
# Quanto tempo lasciare l'app aperta prima di chiuderla
DELAY_SECONDS = 1

def compila_java(file):    
    if 'Controller' in file:
        path_file = os.path.join(controller_classes_path, file)
    else:
        path_file = os.path.join(main_classes_path, file)

    subprocess.run([
        "javac",
        "--module-path", javafx_path,
        "--add-modules", "javafx.controls,javafx.fxml",
        "-sourcepath", "src",
        "-d", "bin",
        path_file
    ])
    
    
def carica_launch_json(percorso):
    with open(percorso, 'r', encoding='utf-8') as f:
        return json.load(f)


def salva_launch_json(percorso, file_json):
    with open(percorso, 'w', encoding='utf-8') as f:
        json.dump(file_json, f, indent=4)


def modifica_main_class(original_json, nuova_main):
    full_class_name = f"fileJava.{nuova_main}"
    new_json = original_json
    for config in new_json.get('configurations', []):
        if 'mainClass' in config:
            config['mainClass'] = full_class_name
    return new_json


def esegui_app_javafx(main_class):
    full_class_name = f"fileJava.{main_class}"
    return subprocess.Popen([
        "java",
        "--module-path", javafx_path,
        "--add-modules", "javafx.controls,javafx.fxml",
        "-cp", "bin",  
        full_class_name
    ])


def main():
    original_json = carica_launch_json(launch_json_path)
    
    for controller_class in controller_classes:
        compila_java(controller_class)
        print(f"✅ Compilazione {controller_class} completata.")
    
    for main_class in main_classes:
        full_file = f"{main_class}.java"
        compila_java(full_file)
        print(f"✅ Compilato: {main_class}")
        print(f"\n▶️ Avvio {main_class}...")

        # 1. Aggiorna launch.json
        new_json = modifica_main_class(original_json.copy(), main_class)
        salva_launch_json(launch_json_path, new_json)

        # 2. Avvia l'app JavaFX
        processo = esegui_app_javafx(main_class)
        time.sleep(DELAY_SECONDS)

        # 3. Termina l'app (se possibile)
        processo.terminate()
        print(f"✅ Testata: {main_class}")
    
    # Ripristina il file originale
    salva_launch_json(launch_json_path, original_json)
    print("\n✅ Tutti i test completati.")


if __name__ == '__main__':
    main()
