package org.lff.ip2geo.pre;

import org.apache.commons.net.util.SubnetUtils;

/**
 * Created by liuff on 2015/11/22 16:44
 */
public class TestCIDR {
    public static void main(String[] argu) {
        SubnetUtils s = new SubnetUtils("192.168.23.35/21");
        System.out.println(s.getInfo().getLowAddress() + " " + s.getInfo().getHighAddress());
    }
}
