package es.cic.curso25.cic25_proyConjunto02.controller;

public class ModificacionSecurityException extends RuntimeException{

    public ModificacionSecurityException() {
        super("Intento de modificación en el create");
    }

    public ModificacionSecurityException(String mensaje) {
        super(mensaje);
    }

    public ModificacionSecurityException (String mensaje, Throwable throwable) {
        super(mensaje, throwable);
    }
}