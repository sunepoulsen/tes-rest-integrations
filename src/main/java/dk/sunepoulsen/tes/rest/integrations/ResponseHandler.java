package dk.sunepoulsen.tes.rest.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sunepoulsen.tes.json.JsonMapper;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientBadRequestException;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientConflictException;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientInternalServerException;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientNotFoundException;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientNotImplementedException;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientResponseException;
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientUnauthorizedException;
import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;

@Slf4j
public class ResponseHandler {

    private final ObjectMapper objectMapper;

    public ResponseHandler() {
        this(new ObjectMapper().findAndRegisterModules());
    }

    public ResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String verifyResponseAndExtractBody(HttpResponse<String> response) {
        if( response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }

        switch(response.statusCode()) {
            case 400:
                throw new ClientBadRequestException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));

            case 401:
                throw new ClientUnauthorizedException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));

            case 404:
                throw new ClientNotFoundException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));

            case 409:
                throw new ClientConflictException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));

            case 500:
                throw new ClientInternalServerException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));

            case 501:
                throw new ClientNotImplementedException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));

            default:
                throw new ClientResponseException(response, JsonMapper.decodeJson(response.body(), ServiceErrorModel.class));
        }
    }

}
