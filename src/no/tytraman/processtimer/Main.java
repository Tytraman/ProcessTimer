package no.tytraman.processtimer;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String processPath;
        File file;
        Boolean processFileExists;

        // On récupère le chemin d'accès vers le processus
        do {
            System.out.print("Entrez le chemin d'acces du processus : ");
            processPath = scanner.nextLine().replace("\"", "");
            file = new File(processPath);
            processFileExists = file.exists();
            if (!processFileExists)
                System.out.println("Fichier introuvable.");
        } while (!processFileExists);
        scanner.close();

        int result = ProcessData.loadDataFile(file);
        while (result != 0) {
            System.out.print("[" + result + "] ");
            switch (result) {
                case 1:
                    System.out.println("Fichier \"process_timer.data\" introuvable, tentative de creation en cours...");
                    ProcessData.createDataFile();
                    result = ProcessData.loadDataFile(file);
                    break;
                default:
                    System.out.println("Erreur inconnue, arret du programme...");
                    System.exit(1);
                    break;
            }
        }

        System.out.println("Lancement du processus...");
        Process process = Runtime.getRuntime().exec(ProcessData.PROCESS_PATH);
        Thread thread = new Thread() {
            @Override
            public void run() {
                ProcessData.RUNNING = true;
                while(ProcessData.RUNNING) {
                    try {
                        Thread.sleep(1000);
                        ProcessData.TIME_PASSED += 1000L;
                        ProcessData.edit();
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        System.out.println("Processus lance ! Le temps d'utilisation est maintenant comptabilise.\n*Ne pas fermer ce terminal*");
        process.waitFor();
        ProcessData.RUNNING = false;
        System.out.println("Au revoir !");
    }
}
