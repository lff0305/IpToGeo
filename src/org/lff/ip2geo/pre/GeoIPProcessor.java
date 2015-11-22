package org.lff.ip2geo.pre;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuff on 2015/11/22 16:48
 */
public class GeoIPProcessor {
    public static void main(String[] arug) {
        try {
            Map<String, List<String>> map = process("db/GeoLite2-City-Blocks-IPv4.csv");
            log("Read " + map.size() + " records.");
            File f = new File("db/geo.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            File in = new File("db/GeoLite2-City-Locations-en.csv");
            BufferedReader br = new BufferedReader(new FileReader(in));
            String l = br.readLine();
            while (l != null) {
                String[] keys = l.split(",");
                List<String> networks = map.get(keys[0]);
                if (networks != null) {
                    for (String network : networks) {
                        bw.write(network + "," + l);
                        bw.write("\r\n");
                    }
                }
                l = br.readLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<String>> process(String file) throws Exception {
        File f = new File(file);
        BufferedReader br = new BufferedReader(new FileReader(f));
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        String l = br.readLine();
        while (l != null) {
            String[] keys = l.split(",");
            List<String> networks = map.get(keys[1]);
            if (networks == null) {
                networks = new ArrayList<>();
            }
            networks.add(keys[0]);
            map.put(keys[1], networks);
            l = br.readLine();
        }
        return map;
    }

    private static void log(String s) {
        System.out.println(s);
    }
}
