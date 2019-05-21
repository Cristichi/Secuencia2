package org.iesmurgi.cristichi.data;

public class StylePackFinder {
    public static StylePack byCode(String code){
        for (CharacterStylePack csp : CharacterStylePack.values()) {
            if (csp.getCode().equals(code)){
                return csp;
            }
        }
        for (ImageStylePack isp : ImageStylePack.values()) {
            if (isp.getCode().equals(code)){
                return isp;
            }
        }
        for (WordStylePack wsp : WordStylePack.values()) {
            if (wsp.getCode().equals(code)){
                return wsp;
            }
        }
        throw new IllegalArgumentException("Unknown style pack with code \""+code+"\"");
    }
}
