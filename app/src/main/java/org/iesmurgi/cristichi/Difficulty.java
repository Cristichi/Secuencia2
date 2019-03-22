package org.iesmurgi.cristichi;

public enum  Difficulty {
    NOOB(3), EASY(6), MEDIUM(10), HARD(14), EXTREME(20);

    private int numElementos;
    Difficulty(int numElementos){
        this.numElementos = numElementos;
    }

    public int getNumElementos() {
        return numElementos;
    }
}
