package org.iesmurgi.cristichi.data;

public class GamemodeFinder {
    /**
     *
     * @param code Código del Modo de Juego
     * @return El Modo de Juego dado por ese código
     * @throws IllegalArgumentException Si el código no es encontrado
     */
    public static Gamemode byCode(String code){
        for (CharacterGamemode csp : CharacterGamemode.values()) {
            if (csp.getCode().equals(code)){
                return csp;
            }
        }
        for (ImageGamemode isp : ImageGamemode.values()) {
            if (isp.getCode().equals(code)){
                return isp;
            }
        }
        for (WordGamemode wsp : WordGamemode.values()) {
            if (wsp.getCode().equals(code)){
                return wsp;
            }
        }
        throw new IllegalArgumentException("Unknown gamemode with code \""+code+"\"");
    }
}
