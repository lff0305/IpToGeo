package org.lff.ip2geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuff on 2015/11/22 17:11
 */
final class Record {

    private static Map<String, Record> cache = new HashMap<>();

    private Record() {
        this.network = new ArrayList<>();
    }

    public static final Record create(String network, String country, String province, String city) {
        String key = country + ":" + province + ":" + city;
        Record r = cache.get(key);
        if (r != null) {
            return r;
        }
        r = new Record();
        r.network.add(network);
        r.country = country;
        r.province = province;
        r.city = city;
        cache.put(key, r);
        return r;
    }

    List<String> network;
    String province;
    public String city;
    public String country;

    public String toString() {
        return "ip = " + network + " country = " + country +
                ", province = " + province + " city = " + city;
    }
}
