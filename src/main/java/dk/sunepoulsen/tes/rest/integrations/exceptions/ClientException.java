package dk.sunepoulsen.tes.rest.integrations.exceptions;

import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;

import java.util.Objects;

public class ClientException extends RuntimeException {
    private ServiceErrorModel serviceError;

    public ClientException(ServiceErrorModel serviceError) {
        this(serviceError, null);
    }

    public ClientException(ServiceErrorModel serviceError, Throwable throwable) {
        super(serviceError.getMessage(), throwable);
        this.serviceError = serviceError;
    }

    public ServiceErrorModel getServiceError() {
        return serviceError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientException that = (ClientException) o;
        return Objects.equals(serviceError, that.serviceError);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceError);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "serviceError=" + serviceError +
                '}';
    }
}
