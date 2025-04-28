import subprocess
import time
import os

javafx_path = "C:\\Users\\Frank\\Desktop\\Dev Projects\\Java\\JavaFX\\javafx-sdk-24.0.1\\lib" ##modificare il percorso in base alla posizione della tua installazione di JavaFX 

main_classes = []
main_classes_path = os.path.join(os.path.dirname(__file__), "fileJava")
for file in os.listdir(main_classes_path):
    main_classes.append(file.split(".")[0])
    
controller_classes = []
controller_classes_path = os.path.join(os.path.dirname(__file__), "controller")
for file in os.listdir(controller_classes_path):
    if file.endswith(".java"):
        controller_classes.append(file)
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
    for controller_class in controller_classes:
        compila_java(controller_class)
        print(f"✅ Compilazione {controller_class} completata.")
    
    for main_class in main_classes:
        full_file = f"{main_class}.java"
        compila_java(full_file)
        print(f"✅ Compilato: {main_class}")
        print(f"\n▶️ Avvio {main_class}...")

        processo = esegui_app_javafx(main_class)
        time.sleep(DELAY_SECONDS)

        processo.terminate()
        print(f"✅ Testata: {main_class}")

    print("\n✅ Tutti i test completati.")


if __name__ == '__main__':
    main()
