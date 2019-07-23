package org.gaozou.kevin.pinyin.test;

import junit.framework.TestCase;
import org.gaozou.kevin.pinyin.Pinyin;
import org.gaozou.kevin.utility.SimpleException;

import java.io.*;

/**
 * Author: george
 * Powered by GaoZou group.
 */
public class PinyinTest extends TestCase {

    public void testPinyin() throws IOException {
//        String str = "公元前一世纪，初步走向繁荣的汉帝国，面临内外的双重威胁；国内诸侯已成尾大不掉之势，妄图脱离中央，实行地方割据。";
        String str = "82家房客";

        Pinyin pin = new Pinyin();

        pin.setToneType(Pinyin.WITH_TONE_MARK);
        pin.setNumType(Pinyin.TO_ARABIC);
        System.out.println(pin.output(str));


//        pin.setToneType(Pinyin.WITHOUT_TONE);
//        System.out.println(pin.outputSegment(str));

//        pin.setToneType(Pinyin.WITH_TONE_MARK);
//        System.out.println(pin.outputSegment(str));
    }



    public void testMove() {
        String pa = "/home/george/develop/pinyin/unicode_to_hanyu_pinyin.txt";
        String pb = "/home/george/develop/pinyin/code_point.txt";

        BufferedReader reader = null;
        OutputStream out = null;

        String line;
        try {
            out = new FileOutputStream(pb);
            PrintStream p = new PrintStream(out);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pa)), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                p.println(new String(line.getBytes("UTF-8"), "UTF-8"));
            }
        } catch (IOException e) {
            throw new SimpleException("problem reading pinyin list.", e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public void testM() {
        String pa = "/home/george/develop/pinyin/mm.txt";
        String pb = "/home/george/develop/pinyin/mm1.txt";

        BufferedReader reader = null;
        OutputStream out = null;

        String s;
        String[] s1, s2, s3;
        int l1, l2, l3;
        char c;
        char[] cs;
        String p1;
        Pinyin pin = new Pinyin();

        String ss1;
        String line;
        try {
            out = new FileOutputStream(pb);
            PrintStream p = new PrintStream(out);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pa)), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                s = new String(line.getBytes("UTF-8"), "UTF-8");
                
                s1 = s.split(",");
                l1 = s1.length;

                c = s1[0].toCharArray()[0];
                p1 = pin.get(c)[0];

                for (int i = 1; i < l1; i++) {
                    s2 = s1[i].split("\\|");

                    if (! s2[0].equalsIgnoreCase(p1)) {
                        ss1 = s2[1];
                        s3 = ss1.split(" ");
                        for (int j = 0; j < s3.length; j++) {
                            cs = s3[j].toCharArray();
                            if (! (cs.length > 0)) continue;


                            StringBuffer bu = new StringBuffer();
                            bu.append("(");
                            String[] pp;
                            for (int k = 0; k < cs.length; k++) {
                                pp = pin.get(cs[k]);

                                if (null != pp) {
                                    bu.append(pp[0]);
                                } else {
                                    bu.append(s2[0]);
                                }
                                if (k != cs.length -1) bu.append(",");
                            }
                            bu.append(")");

                            bu.insert(0, " ").insert(0, s3[j]);
                            String dd = bu.toString();
                            dd = dd.replace('～', c);
                            p.println(dd);
                        }

                    }

                }

            }
        } catch (IOException e) {
            throw new SimpleException("problem reading pinyin list.", e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }        
    }


    public void testWordDB() {
        String pa = "/home/george/develop/pinyin/mm1.txt";
        String pb = "/home/george/develop/pinyin/mm3.txt";

        BufferedReader reader = null;
        OutputStream out = null;

        String line;
        try {
            out = new FileOutputStream(pb);
            PrintStream p = new PrintStream(out);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pa)), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                line = new String(line.getBytes("UTF-8"), "UTF-8");
                String[] w = line.split(" ");

//                StringBuffer b = new StringBuffer();
//                char[] chars = w[0].toCharArray();
//                for (char c : chars) {
//                    b.append(getCodePoint(c));
//                }
//                p.println(getCodePoints(w[0]) + " "+ w[1]);


                p.println(w[0] + " "+ w[1]);



            }
        } catch (IOException e) {
            throw new SimpleException("problem reading pinyin list.", e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }        
    }


    private String getCodePoints(String word) {
        if (null == word) return null;
        StringBuffer b = new StringBuffer();
        char[] chars = word.toCharArray();
        for (char c : chars) {
            b.append(getCodePoint(c));
        }
        return b.toString();
    }
    private String getCodePoint(char c) {
        return Integer.toHexString(c).toUpperCase();
    }




}
