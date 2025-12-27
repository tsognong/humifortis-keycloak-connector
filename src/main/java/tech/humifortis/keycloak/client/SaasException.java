package tech.humifortis.keycloak.client;

public class SaasException extends Exception {
    public SaasException(String message) {
        super(message);
    }

    public SaasException(String message, Throwable cause) {
        super(message, cause);
    }
}
