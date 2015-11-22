package org.lff.ip2geo;

import org.apache.commons.net.util.SubnetUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liuff on 2015/11/22 17:10
 */
public class IpFinderGeoChina {
    static final String UNKONWN = "UNKNOWN";

    private static TreeMap<Long, Record> map = new TreeMap<>();

    static {
        loadData("db/china.csv");
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    private static void loadData(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = br.readLine();
            while (s != null) {
                // 0 network
                // 1 geoname_id
                // 2 locale_code
                // 3 continent_code
                // 4 continent_name
                // 5 country_iso_code
                // 6 country_name
                // 7 subdivision_1_iso_code
                // 8 subdivision_1_name
                // 9 subdivision_2_iso_code
                // 10 subdivision_2_name
                // 11 city_name
                // 12 metro_code
                // 13 time_zone

                String[] ips = s.split(",");

                String network = get(ips, 0);
                String prov = get(ips, 1);
                String city = get(ips, 2);
                String country = "China";
                SubnetUtils util = new SubnetUtils(network);
                long end = parseIp(util.getInfo().getHighAddress());
                if (end > 0) {
                    Record r = Record.create(network, country, prov, city);
                    map.put(Long.valueOf(end), r);
                }
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log("Load finished. " + map.size() + " records");
        long free = Runtime.getRuntime().freeMemory() / ( 1024 * 1024);
        long total = Runtime.getRuntime().totalMemory() / ( 1024 * 1024);
        log(free + " / " + total);
    }

    private static String get(String[] ips, int index) {
        if (ips.length > index) {
            return ips[index];
        }
        return "";
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

        String g = get(l, map);
        return g;
    }


    public static String get(long longIP, TreeMap<Long, Record> map) {
        ///Use ceilingEntry·½·¨£¬to get nearest item
        Map.Entry<Long, Record> e = map.ceilingEntry(longIP);
        if (e == null) {
            return "UNKNOWN";
        }
        Record r = e.getValue();
        if (r == null) {
            return UNKONWN;
        }
        return r.toString();
    }

    public static void main(String[] argu) {
        String ip = "223.104.5.195";
        String s = find(ip);
        System.out.println("\nResult for " + ip + " is " + s);
    }
}
