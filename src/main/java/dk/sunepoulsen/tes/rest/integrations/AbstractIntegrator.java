package dk.sunepoulsen.tes.rest.integrations;

import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AbstractIntegrator {
    protected final TechEasySolutionsClient httpClient;

    public AbstractIntegrator(TechEasySolutionsClient httpClient) {
        this.httpClient = httpClient;
    }

    protected <T> Single<T> mapClientExceptions(Throwable throwable) {
        if( throwable instanceof ClientException) {
            return Single.error(throwable);
        }

        if( throwable.getCause() != null ) {
            return mapClientExceptions(throwable.getCause());
        }

        return Single.error(throwable);
    }

    protected Completable mapClientExceptionsAsCompletable(Throwable throwable) {
        if( throwable instanceof ClientException) {
            return Completable.error(throwable);
        }

        if( throwable.getCause() != null ) {
            return mapClientExceptionsAsCompletable(throwable.getCause());
        }

        return Completable.error(throwable);
    }

    protected String createHttpQuery(Map<String, String> params) {
        if( params.isEmpty()) {
            return "";
        }

        StringBuffer result = new StringBuffer();

        result.append('?');
        params.forEach((key, value) -> {
            result.append(key);
            result.append('=');
            result.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            result.append('&');
        });
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }
}
