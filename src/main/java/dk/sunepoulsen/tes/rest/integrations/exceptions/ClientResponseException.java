package dk.sunepoulsen.tes.rest.integrations.exceptions;

import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;

import java.net.http.HttpResponse;
import java.util.Objects;

public class ClientResponseException extends ClientException {
    private HttpResponse<String> response;

    public ClientResponseException(HttpResponse<String> response, ServiceErrorModel serviceError) {
        this(response, serviceError, null);
    }

    public ClientResponseException(HttpResponse<String> response, ServiceErrorModel serviceError, Throwable throwable) {
        super(serviceError, throwable);
        this.response = response;
    }

    public HttpResponse<String> getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClientResponseException that = (ClientResponseException) o;
        return Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), response);
    }

    @Override
    public String toString() {
        return "ClientResponseException{" +
                "response=" + response +
                "} " + super.toString();
    }
}
