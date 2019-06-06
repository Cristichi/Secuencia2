package org.iesmurgi.cristichi.ddbb;

/**
 * Almacena los datos de un Usuario
 */
public class User {
    public String nick;
    public String email;

    public User(String nick, String email){
        this.nick = nick;
        this.email = email;
    }
}
