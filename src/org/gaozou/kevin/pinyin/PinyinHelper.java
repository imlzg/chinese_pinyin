package org.gaozou.kevin.pinyin;

import org.gaozou.kevin.utility.PropertiesUtil;
import org.gaozou.kevin.utility.SimpleException;
import org.gaozou.kevin.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author: george
 * Powered by GaoZou group.
 */
public class PinyinHelper {
    private static final Logger log = LoggerFactory.getLogger(PinyinHelper.class);
    private static final String configFile = "pinyin.properties";
    private static final String pinyinDBLocation = "pinyinDB.properties";

    public static Properties pinyinDB;
    public static Map<Integer, String> wordDB;
    static {
        log.debug("loading pinyin db...");
        pinyinDB = PropertiesUtil.load(pinyinDBLocation);

        log.debug("Reading config file...");
        Properties config = PropertiesUtil.load(configFile);

        String wordDBLocation = null == config ? null : config.getProperty("wordDB");
        if (! StringUtil.isEmpty(wordDBLocation)) {
            log.debug("loading word db...");
            wordDB = new HashMap<Integer, String> ();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(wordDBLocation)), "UTF-8"));
                String line;
                String[] w;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0 && line.charAt(0) != '#') {
                        w = line.split(" ");
                        wordDB.put(w[0].hashCode(), w[1]);
                    }
                }
            } catch (IOException e) {
                throw new SimpleException("problem loading " + wordDBLocation, e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    log.error("could not close stream on {}", wordDBLocation);
                }
            }
            log.debug("word db size: {}", wordDB.size());
        }
    }

    public static String simpleString(String str) {
        if (null == str) return null;
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            sb.append(StringUtil.toBigHanDigit(c));
        }
        return sb.toString();
    }
}
