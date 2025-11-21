package Distribuido.Proyecto.Storage;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TXTLoader {

    public void ensureFile(String path, String header) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                File parent = f.getParentFile();
                if (parent != null && !parent.exists()) parent.mkdirs();
                try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                    pw.println(header);
                }
                System.out.println("TXTLoader.ensureFile -> creado: " + path);
            } catch (Exception e) {
                System.err.println("TXTLoader.ensureFile ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<String[]> read(String path) {
        List<String[]> rows = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) return rows;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split("\\|"));
            }
        } catch (Exception e) {
            System.err.println("TXTLoader.read ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    public void write(String path, List<String[]> rows, String header) {
        try {
            File parent = new File(path).getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
                pw.println(header);
                for (String[] r : rows) pw.println(String.join("|", r));
            }
        } catch (Exception e) {
            System.err.println("TXTLoader.write ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
