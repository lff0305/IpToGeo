package org.lff.ip2geo.pre;


import java.io.*;

/**
 * Pre-Processing. Load txt data from db folder, process, and save to
 * folder data
 * Created by liuff on 2015/10/23.
 */
public class DataProcessor {
    public static void main(String[] argu) {
        process("db/isp");
        process("db/prov");
    }

    private static void process(String folder) {
        File f = new File(folder);
        log("\nStart to process folder" + f.getAbsolutePath());
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                process(f.getName(), file);
            }
        }
    }

    private static void log(String s) {
        System.out.println(s);
    }

    private static void process(String folder, File file) {
        log("   processing " + file);
        try {

            FileWriter fw = new FileWriter(new File("data" + File.separator + folder +
                File.separator + file.getName()));

            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = br.readLine();
            while (s != null) {
                String result = processLine(s);
                if (result != null) {
                    fw.write(result);
                    fw.write("\r\n");
                }
                s = br.readLine();
            }
            fw.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processLine(String s) {
        String[] data = s.split("\\s");
        if (data.length != 3) {
            return null;
        }
        String startIp = data[0];
        String endIp = data[1];
        return startIp + " " + endIp;
    }

}
