package dk.sunepoulsen.tes.rest.integrations.exceptions;

import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;

import java.net.http.HttpResponse;

public class ClientNotImplementedException extends ClientResponseException {
    public ClientNotImplementedException(HttpResponse<String> response, ServiceErrorModel serviceError) {
        super(response, serviceError);
    }

    public ClientNotImplementedException(HttpResponse<String> response, ServiceErrorModel serviceError, Throwable throwable) {
        super(response, serviceError, throwable);
    }

    @Override
    public String toString() {
        return "ClientNotImplementedException{} " + super.toString();
    }
}
