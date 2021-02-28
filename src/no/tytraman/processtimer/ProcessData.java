package no.tytraman.processtimer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProcessData {
    private static File file;
    private static String data;
    public static String PROCESS_PATH;
    public static long TIME_PASSED;
    public static Boolean RUNNING;

    public static int loadDataFile(File processFile) {
        PROCESS_PATH = processFile.getAbsolutePath();
        data = "";
        file = new File("process_timer.data");

        // On charge les données
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line, opt, value;
            int sepIndex, endIndex;
            Boolean found = false;
            while((line = reader.readLine()) != null) {
                data += line + '\n';
                sepIndex = line.indexOf('>');
                endIndex = line.indexOf(';');
                opt = line.substring(0, sepIndex);
                value = line.substring(sepIndex + 1, endIndex);
                if(opt.equals(PROCESS_PATH)) {
                    TIME_PASSED = Long.parseLong(value);
                    found = true;
                    break;
                }
            }
            reader.close();
            if(!found) {
                data += PROCESS_PATH + ">0;\n";
                TIME_PASSED = 0L;
            }
            save();
        }catch(FileNotFoundException notFound) {
            return 1;
        }catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Donnees chargees !");
        return 0;
    }

    public static void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(data);
            writer.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDataFile() {
        try {
            file.createNewFile();
            System.out.println("Fichier \"process_timer.data\" cree avec succes !");
        }catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void edit() {
        String line = data.substring(data.indexOf(PROCESS_PATH));
        line = line.substring(0, line.indexOf(';'));
        data = data.replace(line, PROCESS_PATH + ">" + TIME_PASSED);
        save();
    }
}
