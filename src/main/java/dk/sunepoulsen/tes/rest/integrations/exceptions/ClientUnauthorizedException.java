package dk.sunepoulsen.tes.rest.integrations.exceptions;

import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;

import java.net.http.HttpResponse;

public class ClientUnauthorizedException extends ClientResponseException {
    public ClientUnauthorizedException(HttpResponse<String> response, ServiceErrorModel serviceError) {
        super(response, serviceError);
    }

    public ClientUnauthorizedException(HttpResponse<String> response, ServiceErrorModel serviceError, Throwable throwable) {
        super(response, serviceError, throwable);
    }

    @Override
    public String toString() {
        return "ClientUnauthorizedException{} " + super.toString();
    }
}
