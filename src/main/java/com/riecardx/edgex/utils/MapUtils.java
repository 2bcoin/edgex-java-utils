package com.riecardx.edgex.utils;

import java.util.Map;

public class MapUtils {
    public static Boolean getBoolean(Map<String, Object> map, String key) {
        if (map != null && map.size()>0 && map.containsKey(key)) {
            try {
                Boolean boo = (Boolean) map.get(key);
                return boo;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    public static String getString(Map<String, Object> map, String key) {
        if (map != null && map.size()>0 && map.containsKey(key)) {
            try {
                String boo = map.get(key).toString();
                return boo;
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }
    public static Double getDouble(Map<String, Object> map, String key) {
        if (map != null && map.size()>0 && map.containsKey(key)) {
            try {
                Double boo = (Double) map.get(key);
                return boo;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    public static Integer getInt(Map<String, Object> map, String key) {
        if (map != null && map.size()>0 && map.containsKey(key)) {
            try {
                Integer boo = (Integer) map.get(key);
                return boo;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
