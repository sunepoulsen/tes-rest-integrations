package dk.sunepoulsen.tes.rest.integrations;

import dk.sunepoulsen.tes.json.JsonMapper;
import dk.sunepoulsen.tes.rest.integrations.config.DefaultClientConfig;
import dk.sunepoulsen.tes.rest.integrations.config.TechEasySolutionsClientConfig;
import dk.sunepoulsen.tes.rest.integrations.generators.RequestIdGenerator;
import dk.sunepoulsen.tes.rest.integrations.generators.UUIDRequestIdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Http Client to make calls to a Tech Enterprise Labs service.
 */
@Slf4j
public class TechEasySolutionsClient {
    private final URI uri;
    private final TechEasySolutionsClientConfig config;
    private final HttpClient client;

    private final ResponseHandler responseHandler;
    private final RequestIdGenerator requestIdGenerator;

    public TechEasySolutionsClient(URI uri) {
        this(uri, new UUIDRequestIdGenerator());
    }

    public TechEasySolutionsClient(URI uri, RequestIdGenerator requestIdGenerator) {
        this(uri, new DefaultClientConfig(), requestIdGenerator);
    }

    public TechEasySolutionsClient(URI uri, TechEasySolutionsClientConfig config) {
        this(uri, config, new UUIDRequestIdGenerator());
    }

    public TechEasySolutionsClient(URI uri, TechEasySolutionsClientConfig config, RequestIdGenerator requestIdGenerator) {
        this.uri = uri;
        this.config = config;
        this.requestIdGenerator = requestIdGenerator;

        this.client = buildHttpClient();
        this.responseHandler = new ResponseHandler(config.jsonMapper().getObjectMapper());
    }

    public <T> CompletableFuture<T> get(String url, Class<T> clazz) {
        return executeRequest("GET", url, clazz);
    }

    public <T, R> CompletableFuture<R> post(String url, T bodyValue, Class<R> clazzResult) {
        return executeRequest("POST", url, bodyValue, clazzResult);
    }

    public <T, R> CompletableFuture<R> put(String url, T bodyValue, Class<R> clazzResult) {
        return executeRequest("PUT", url, bodyValue, clazzResult);
    }

    public <T, R> CompletableFuture<R> patch(String url, T bodyValue, Class<R> clazzResult) {
        return executeRequest("PATCH", url, bodyValue, clazzResult);
    }

    public CompletableFuture<String> delete(String url) {
        return executeRequest("DELETE", url);
    }

    private CompletableFuture<String> executeRequest(String method, String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .method(method, HttpRequest.BodyPublishers.noBody())
            .uri(uri.resolve(url))
            .header("X-REQUEST-ID", requestIdGenerator.generateId())
            .timeout(config.httpClientRequestTimeout())
            .build();

        log.debug("Call {} {}", method, httpRequest.uri());
        return client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
            .thenApply(responseHandler::verifyResponseAndExtractBody);
    }

    private <T, R> CompletableFuture<R> executeRequest(String method, String url, Class<R> clazzResult) {
        return executeRequest(method, url)
            .thenApply(s -> JsonMapper.decodeJson(s, clazzResult));
    }

    private <T, R> CompletableFuture<R> executeRequest(String method, String url, T bodyValue, Class<R> clazzResult) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .method(method, HttpRequest.BodyPublishers.ofString(config.jsonMapper().encode(bodyValue)))
            .uri(uri.resolve(url))
            .header("Content-Type", "application/json")
            .header("X-REQUEST-ID", requestIdGenerator.generateId())
            .timeout(config.httpClientRequestTimeout())
            .build();

        log.debug("Call {} {}", method, httpRequest.uri());
        return client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
            .thenApply(responseHandler::verifyResponseAndExtractBody)
            .thenApply(s -> JsonMapper.decodeJson(s, clazzResult));
    }

    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
            .version(config.httpClientVersion())
            .followRedirects(config.httpClientFollowRedirects())
            .connectTimeout(config.httpClientConnectTimeout())
            .build();
    }
}
