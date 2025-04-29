import shutil
import subprocess
import time
import os
import private_keys

javafx_path = private_keys.javafx_path
main_classes = []
main_classes_path = os.path.join(os.path.dirname(__file__), "fileJava")
for file in os.listdir(main_classes_path):
    main_classes.append(file.split(".")[0])

    
controller_classes = []
controller_classes_path = os.path.join(os.path.dirname(__file__), "controller")
for file in os.listdir(controller_classes_path):
    if file.endswith(".java"):
        controller_classes.append(file)
DELAY_SECONDS = 2




def esegui_app_javafx(main_class):
    full_class_name = f"fileJava.{main_class}"
    sep = ";" if os.name == "nt" else ":"
    classpath = "bin" + sep + "src"
    return subprocess.Popen([
        "java",
        "--module-path", javafx_path,
        "--add-modules", "javafx.controls,javafx.fxml",
        "--enable-native-access=javafx.graphics",
        "-cp", classpath,  
        full_class_name
    ])


def main():
    all_java_files = []

    for controller_class in controller_classes:
        all_java_files.append(os.path.join(controller_classes_path, controller_class))

    for main_class in main_classes:
        java_file = f"{main_class}.java"
        all_java_files.append(os.path.join(main_classes_path, java_file))
    
    if os.path.exists("bin"):
        shutil.rmtree("bin")
        os.mkdir("bin")
        
    subprocess.run([
        "javac",
        "--module-path", javafx_path,
        "--add-modules", "javafx.controls,javafx.fxml",
        "-d", "bin"
    ] + all_java_files)
    print("✅ Tutti i file java compilati.\n")
    
    for main_class in main_classes:
        print(f"▶️ Avvio {main_class}...")

        processo = esegui_app_javafx(main_class)
        time.sleep(DELAY_SECONDS)

        processo.terminate()
        print(f"✅ Testata: {main_class}\n")

    print("\n✅ Tutti i test completati.")


if __name__ == '__main__':
    main()
