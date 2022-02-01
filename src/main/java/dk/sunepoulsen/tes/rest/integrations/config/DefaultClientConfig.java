package dk.sunepoulsen.tes.rest.integrations.config;

import dk.sunepoulsen.tes.json.JsonMapper;
import java.net.http.HttpClient;
import java.time.Duration;

public class DefaultClientConfig implements TechEasySolutionsClientConfig {
    private final JsonMapper jsonMapper;

    public DefaultClientConfig() {
        this.jsonMapper = new JsonMapper();
    }

    @Override
    public HttpClient.Version httpClientVersion() {
        return HttpClient.Version.HTTP_1_1;
    }

    @Override
    public HttpClient.Redirect httpClientFollowRedirects() {
        return HttpClient.Redirect.NORMAL;
    }

    @Override
    public Duration httpClientConnectTimeout() {
        return Duration.ofSeconds(30);
    }

    @Override
    public Duration httpClientRequestTimeout() {
        return Duration.ofSeconds(30);
    }

    @Override
    public JsonMapper jsonMapper() {
        return this.jsonMapper;
    }
}
