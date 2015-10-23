package org.lff.ip2geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015/10/23.
 */
public class IpFinder {

    static final String UNKONWN = "UNKNOWN";

    static final Map<String, TreeMap<Long, String>> store = new HashMap();

    private static final String KEY_PROV = "prov";
    private static final String KEY_ISP = "isp";

    static {
        loadData(KEY_ISP, "data/isp");
        loadData(KEY_PROV, "data/prov");
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    private static void loadData(String key, String folder) {

        store.put(key, new TreeMap<>());

        File f = new File(folder);
        log("\nStart to load folder" + f.getAbsolutePath());
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                load(key, file);
            }
        }
    }

    private static void load(String key, File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = br.readLine();
            while (s != null) {
                String[] ips = s.split(" ");
                if (ips.length != 2) {
                    log("invalid data " + s);
                    continue;
                }
                String endIp = ips[1];
                long end = parseIp(endIp);
                if (end > 0) {
                    Map<Long, String> map = store.get(key);
                    String name = removeExt(file.getName());
                    map.put(Long.valueOf(end), name);
                }
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String removeExt(String name) {
        int index = name.indexOf(".");
        return name.substring(0, index);
    }

    private static long parseIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return -1;
        }
        try {
            String[] digits = ip.split("\\.");
            if (digits.length != 4) {
                return -1;
            }
            long l3 = Long.parseLong(digits[3]);
            long l2 = Long.parseLong(digits[2]);
            long l1 = Long.parseLong(digits[1]);
            long l0 = Long.parseLong(digits[0]);
            return l3 + 256 * l2 + 256 * 256 * l1 + 256 * 256 * 256 * l0;
        } catch(Exception e) {
            return -1;
        }
    }

    public static String find(String ip) {
        long l = parseIp(ip);
        if (l < 0) {
            log("Invalid " + ip);
            return UNKONWN;
        }

        String g = get(l, store.get(KEY_PROV)) + "," + get(l, store.get(KEY_ISP));
        return g;
    }


    public static String get(long longIP, TreeMap<Long, String> map) {
        ///Use ceilingEntry·½·¨£¬to get nearest item
        Map.Entry<Long, String> e = map.ceilingEntry(longIP);
        if (e == null) {
            return "UNKNOWN";
        }
        String r = e.getValue();
        if (r == null) {
            return UNKONWN;
        }
        return r.toUpperCase();
    }

    public static void main(String[] argu) {
        String s = find("101.81.75.141");
        System.out.println(s);
    }
}
