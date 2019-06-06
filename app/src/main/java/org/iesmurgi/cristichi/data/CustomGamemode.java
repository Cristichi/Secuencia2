package org.iesmurgi.cristichi.data;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Modo de Juego creado por un Usuario, el cuál no dispone de traducción.
 */
public class CustomGamemode {
    private static final String DELIM = "\n";

    private int id;
    private String userEmail;
    private String name;
    private String valuesString;
    private int downloads;
    private String[] values;

    private Random rng;

    /**
     * Crea un objeto que representa un Modo de Juego creado por un Usuario.
     * @param id Id única representativa de este Modo de Juego en la Base de Datos. Este valor
     *           debe ser recogido desde la Base de Datos.
     * @param userEmail Email del Usuario que creó este Modo de Juego.
     * @param name Nombre de este Modo de Juego.
     * @param values Valores disponibles en este Modo de Juego, separados por saltos de línea.
     * @param downloads Número de veces que se ha descargado y jugado este Modo de Juego.
     */
    public CustomGamemode(int id, String userEmail, String name, String values, int downloads) {
        rng = new Random();
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
        this.downloads = downloads;
        this.valuesString = values;

        StringTokenizer st = new StringTokenizer(values, DELIM);
        this.values = new String[st.countTokens()];
        for (int i=0; st.hasMoreTokens(); i++){
            this.values[i] = st.nextToken();
        }
    }

    /**
     *
     * @return La id única representativa de este Modo de Juego en la Base de Datos. Este valor
     * debe ser recogido desde la Base de Datos.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return El email del Usuario que creó este Modo de Juego.
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     *
     * @return El nombre de este Modo de Juego.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return Los valores disponibles en este Modo de Juego, separados por saltos de línea.
     */
    public String getValuesString() {
        return valuesString;
    }

    /**
     *
     * @return El número de veces que se ha descargado y jugado este Modo de Juego.
     */
    public int getDownloads() {
        return downloads;
    }

    /**
     *
     * @param context Actividad donde se mostrarán los botones.
     * @return La lista de botones de orden aleatorizado, que guardan su valor de la secuencia
     * en su Tag. Contiene cada valor de este Modo de Juego sin repetir.
     */
    public ArrayList<Button> getButtons(Context context) {
        ArrayList<Button> sol = new ArrayList<>(values.length);
        for (String value : values) {
            Button uno = new Button(context);
            uno.setText(value);
            uno.setTag(value);
            sol.add(uno);
        }
        Collections.shuffle(sol);
        return sol;
    }

    /**
     *
     * @param difficulty Dificultad de la secuencia
     * @return Una lista aleatorizada cuyos valores son siempre valores de este Modo de Juego.
     */
    public List<String> generateRandomSentence(Difficulty difficulty) {
        int elementos = difficulty.getNumElementos();
        elementos += (rng.nextBoolean() ? 1 : -1) * rng.nextInt(Math.max(elementos / 4, 1));
        ArrayList<String> sol = new ArrayList<>(elementos);
        for (int i = 0; i < elementos; i++) {
            String word = values[rng.nextInt(values.length)];
            sol.add(word);
        }
        return sol;
    }
}