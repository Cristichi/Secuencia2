package org.iesmurgi.cristichi.data;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class CustomGamemode {
    private static final String DELIM = "\n";

    private int id;
    private String userEmail;
    private String name;
    private String valuesString;
    private String[] values;

    private Random rng;

    public CustomGamemode(int id, String userEmail, String name, String values) {
        rng = new Random();
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
        this.valuesString = values;

        StringTokenizer st = new StringTokenizer(values, DELIM);
        this.values = new String[st.countTokens()];
        for (int i=0; st.hasMoreTokens(); i++){
            this.values[i] = st.nextToken();
        }
    }

    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }

    public String getValuesString() {
        return valuesString;
    }

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