package org.iesmurgi.cristichi.ddbb;

/**
 * Objeto que engloba un Usuario o un Error producido al intentar iniciar sesión.
 */
public class ReturnLogin{
    public Throwable e = null;
    public User user = null;
}