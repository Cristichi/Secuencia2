package org.iesmurgi.cristichi.data;

/**
 * Representa todos los datos de un Modo de Juego creado por un Usuario, excepto los valores del
 * Modo de Juego. De esta forma, se reduce considerablemente la carga de datos al mostrar una
 * lista de Modos de Juego de la Comunidad.
 */
public class InfoCustomGamemode{
    private int id;
    private String userEmail;
    private String name;

    InfoCustomGamemode(int id, String userEmail, String name){
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
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
    public String getName() {
        return name;
    }

    /**
     *
     * @return El nombre de este Modo de Juego.
     */
    public String getUserEmail() {
        return userEmail;
    }
}
