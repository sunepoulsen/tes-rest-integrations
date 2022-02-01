package dk.sunepoulsen.tes.rest.integrations.exceptions;

import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;

import java.net.http.HttpResponse;

public class ClientConflictException extends ClientResponseException {
    public ClientConflictException(HttpResponse<String> response, ServiceErrorModel serviceError) {
        super(response, serviceError);
    }

    public ClientConflictException(HttpResponse<String> response, ServiceErrorModel serviceError, Throwable throwable) {
        super(response, serviceError, throwable);
    }

    @Override
    public String toString() {
        return "ClientConflictException{} " + super.toString();
    }
}
