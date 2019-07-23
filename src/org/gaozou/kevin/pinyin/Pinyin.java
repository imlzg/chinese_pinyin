package org.gaozou.kevin.pinyin;

import org.gaozou.kevin.utility.SimpleException;
import org.gaozou.kevin.utility.StringUtil;

/**
 * Author: george
 * Powered by GaoZou group.
 */
public class Pinyin {
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";
    private static final String COMMA = ",";

    public static final String UPPERCASE = "UPPERCASE";
    public static final String LOWERCASE = "LOWERCASE";
    public static final String WITH_U_AND_COLON = "WITH_U_AND_COLON";
    public static final String WITH_V = "WITH_V";
    public static final String WITH_U_UNICODE = "WITH_U_UNICODE";
    public static final String WITH_TONE_NUMBER = "WITH_TONE_NUMBER";
    public static final String WITHOUT_TONE = "WITHOUT_TONE";
    public static final String WITH_TONE_MARK = "WITH_TONE_MARK";
    public static final String TO_ARABIC = "TO_ARABIC";

    private String vType = WITH_V;
    private String caseType = LOWERCASE;
    private String toneType = WITHOUT_TONE;
    private String numType;

    public Pinyin setVType(String vType) {
        this.vType = vType;
        return this;
    }
    public Pinyin setCaseType(String caseType) {
        this.caseType = caseType;
        return this;
    }
    public Pinyin setToneType(String toneType) {
        this.toneType = toneType;
        return this;
    }
    public Pinyin setNumType(String numType) {
        this.numType = numType;
        return this;
    }

    public String getFirstLetters(String str) {
        if (StringUtil.isEmpty(str)) return null;
        StringBuffer buf = new StringBuffer();
        char[] chars = str.toCharArray();
        for (char c : chars) {
            buf.append(getFirstLetter(c));
        }
        return buf.toString();
    }
    public char getFirstLetter(String str) {
        if (StringUtil.isEmpty(str)) return ' ';
        return output(str).charAt(0);
    }
    public char getFirstLetter(char c) {
        return get(c)[0].charAt(0);
    }

/*
    // segment it before outputting pinyin
    public String outputSegment(String str) throws IOException {
        StringBuffer buf = new StringBuffer();
        Segment seg = new Segmenter(new StringReader(str));
        List<Word> words = seg.segment();

        for (Word w : words) {
            String txt = PinyinHelper.simpleString(w.getText());
            if (StringUtil.isHan(txt.charAt(0))) buf.append(" ");

            String pinyin = PinyinHelper.wordDB.get(txt.hashCode());
            if (null != pinyin) {
                String[] ps = pinyin.replaceAll("\\(|\\)", "").split(",");
                for (String p : ps) {
                    buf.append(format(p)).append(" ");
                }
                buf.deleteCharAt(buf.length() - 1);
            } else {
                buf.append(output(txt));
            }
        }
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }
*/

    public String output(String str) {
        char[] chars = str.toCharArray();

        StringBuffer buf = new StringBuffer();
        char d = ' ';
        for (char c : chars) {
            if (StringUtil.isHan(d)) buf.append(" ");
            if (! StringUtil.isHan(d) && StringUtil.isHan(c)) buf.append(" ");

            String[] pinyin = get(c);
            if (null != pinyin) {
                buf.append(format(pinyin[0]));
            } else {
                buf.append(c);
            }
            d = c;
        }
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    public String format(String pinyinStr) {
        if (WITH_TONE_MARK.equals(toneType) && (WITH_V.equals(vType) || WITH_U_AND_COLON.equals(vType))) {
            throw new SimpleException("tone marks cannot be added to v or u:");
        }

        if (WITHOUT_TONE.equals(toneType)) {
            pinyinStr = pinyinStr.replaceAll("[1-5]", "");
        } else if (WITH_TONE_MARK.equals(toneType)) {
            pinyinStr = pinyinStr.replaceAll("u:", "v");
            pinyinStr = tuneTone(pinyinStr);
        }

        if (WITH_V.equals(vType)) {
            pinyinStr = pinyinStr.replaceAll("u:", "v");
        } else if (WITH_U_UNICODE.equals(vType)) {
            pinyinStr = pinyinStr.replaceAll("u:", "ü");
        }

        if (UPPERCASE.equals(caseType)) {
            pinyinStr = pinyinStr.toUpperCase();
        }
        return pinyinStr;
    }

    public String tuneTone(final String pinyinWithTone) {
        String lowerCasePinyinStr = pinyinWithTone.toLowerCase();

        if (lowerCasePinyinStr.matches("[a-z]*[1-5]?")) {
            final char defautlCharValue = '$';
            final int defautlIndexValue = -1;

            char unmarkedVowel = defautlCharValue;
            int indexOfUnmarkedVowel = defautlIndexValue;

            final char charA = 'a';
            final char charE = 'e';
            final String ouStr = "ou";
            final String allUnmarkedVowelStr = "aeiouv";
            final String allMarkedVowelStr = "āáăàaēéĕèeīíĭìiōóŏòoūúŭùuǖǘǚǜü";

            if (lowerCasePinyinStr.matches("[a-z]*[1-5]")) {

                int tuneNumber = Character.getNumericValue(lowerCasePinyinStr.charAt(lowerCasePinyinStr.length() - 1));

                int indexOfA = lowerCasePinyinStr.indexOf(charA);
                int indexOfE = lowerCasePinyinStr.indexOf(charE);
                int ouIndex = lowerCasePinyinStr.indexOf(ouStr);

                if (-1 != indexOfA) {
                    indexOfUnmarkedVowel = indexOfA;
                    unmarkedVowel = charA;
                } else if (-1 != indexOfE) {
                    indexOfUnmarkedVowel = indexOfE;
                    unmarkedVowel = charE;
                } else if (-1 != ouIndex) {
                    indexOfUnmarkedVowel = ouIndex;
                    unmarkedVowel = ouStr.charAt(0);
                } else {
                    for (int i = lowerCasePinyinStr.length() - 1; i >= 0; i--) {
                        if (String.valueOf(lowerCasePinyinStr.charAt(i)).matches("[" + allUnmarkedVowelStr + "]")) {
                            indexOfUnmarkedVowel = i;
                            unmarkedVowel = lowerCasePinyinStr.charAt(i);
                            break;
                        }
                    }
                }

                if ((defautlCharValue != unmarkedVowel) && (defautlIndexValue != indexOfUnmarkedVowel)) {
                    int rowIndex = allUnmarkedVowelStr.indexOf(unmarkedVowel);
                    int columnIndex = tuneNumber - 1;

                    int vowelLocation = rowIndex * 5 + columnIndex;

                    char markedVowel = allMarkedVowelStr.charAt(vowelLocation);

                    StringBuffer resultBuffer = new StringBuffer();

                    resultBuffer.append(lowerCasePinyinStr.substring(0, indexOfUnmarkedVowel).replaceAll("v", "ü"));
                    resultBuffer.append(markedVowel);
                    resultBuffer.append(lowerCasePinyinStr.substring(indexOfUnmarkedVowel + 1, lowerCasePinyinStr.length() - 1).replaceAll("v", "ü"));

                    return resultBuffer.toString();

                } else {// error happens in the procedure of locating vowel
                    return lowerCasePinyinStr;
                }
            } else {// input string has no any tune number, only replace v with ü (umlat) character
                return lowerCasePinyinStr.replaceAll("v", "ü");
            }
        } else {// bad format
            return lowerCasePinyinStr;
        }
    }

    public String getToneNum(String pinyinWithTone) {
        return pinyinWithTone.substring(pinyinWithTone.length() - 1);
    }
    public String getPinyin(String pinyinWithTone) {
        return pinyinWithTone.substring(0, pinyinWithTone.length() - 1);
    }
    public String[] get(char c) {
        if (TO_ARABIC.equals(numType) && StringUtil.isHanDigit(c)) {
            return new String[] {Character.toString(StringUtil.toArabicDigit(c))};
        }
        String record = getRecord(c);
        if (null != record) {
            return record.substring(record.indexOf(LEFT_BRACKET) + LEFT_BRACKET.length(), record.lastIndexOf(RIGHT_BRACKET)).split(COMMA);
        }
        return null;
    }


    private String getRecord(char c) {
        String record = PinyinHelper.pinyinDB.getProperty(getCodePoint(c));
        return isValidRecord(record) ? record : null;
    }
    private boolean isValidRecord(String record) {
        final String noneStr = "(none0)";
        return (null != record) && ! record.equals(noneStr) && record.startsWith(LEFT_BRACKET) && record.endsWith(RIGHT_BRACKET);
    }
    private String getCodePoint(char c) {
        return Integer.toHexString(c).toUpperCase();
    }
}