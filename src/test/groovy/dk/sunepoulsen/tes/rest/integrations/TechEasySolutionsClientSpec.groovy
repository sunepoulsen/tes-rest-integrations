package dk.sunepoulsen.tes.rest.integrations

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientBadRequestException
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientConflictException
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealth
import dk.sunepoulsen.tes.rest.models.monitoring.ServiceHealthStatusCode
import groovy.json.JsonBuilder
import org.junit.Test
import spock.lang.Specification

import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

import static com.github.tomakehurst.wiremock.client.WireMock.*

class TechEasySolutionsClientSpec extends Specification {

    private static WireMockServer wireMockServer
    private TechEasySolutionsClient techEasySolutionsClient

    void setupSpec() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort())
        wireMockServer.start()
    }

    void cleanupSpec() {
        wireMockServer.stop()
    }

    void setup() {
        wireMockServer.resetAll()
        techEasySolutionsClient = new TechEasySolutionsClient(new URI("http://localhost:${wireMockServer.port()}"))
    }

    void "Call GET /actuator/health with OK body"() {
        given:
            wireMockServer.running

        and:
            wireMockServer.stubFor(get(urlEqualTo('/actuator/health'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(new JsonBuilder(
                        [
                            status: ServiceHealthStatusCode.UP.toString()
                        ]).toPrettyString())
                ))

        when:
            CompletableFuture<ServiceHealth> futureResponse = techEasySolutionsClient.get('/actuator/health', ServiceHealth)

        then:
            futureResponse.get().status == ServiceHealthStatusCode.UP
            noExceptionThrown()
    }

    void "Call GET /actuator/health with BadRequest error"() {
        given:
            wireMockServer.running

        and:
            wireMockServer.stubFor(get(urlEqualTo('/actuator/health'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .willReturn(aResponse()
                    .withStatus(400)
                    .withBody(new JsonBuilder(
                        [
                            code: 'code',
                            param: 'param',
                            message: 'message'
                        ]).toPrettyString())
                ))

        when:
            techEasySolutionsClient.get('/actuator/health', ServiceHealth).get()

        then:
            ExecutionException ex = thrown(ExecutionException)
            ex.cause instanceof ClientBadRequestException
            ex.cause.serviceError.code == 'code'
            ex.cause.serviceError.param == 'param'
            ex.cause.serviceError.message == 'message'
    }

    void "Call POST /actuator/health with OK body"() {
        given:
            wireMockServer.running

        and:
            String requestBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.UP.toString()
                ]).toPrettyString()
            String responseBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.DOWN.toString()
                ]).toPrettyString()

        and:
            wireMockServer.stubFor(post(urlEqualTo('/actuator/health'))
                .withHeader("Content-Type", equalTo('application/json'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(responseBody)
                ))

        when:
            CompletableFuture<ServiceHealth> futureResponse = techEasySolutionsClient.post('/actuator/health', new ServiceHealth(status: ServiceHealthStatusCode.UP), ServiceHealth)

        then:
            futureResponse.get().status == ServiceHealthStatusCode.DOWN
            noExceptionThrown()
    }

    void "Call POST /actuator/health with conflict response"() {
        given:
            wireMockServer.running

        and:
            String requestBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.UP.toString()
                ]).toPrettyString()
            String responseBody = new JsonBuilder(
                [
                    code: 'code',
                    param: 'param',
                    message: 'message'
                ]).toPrettyString()

        and:
            wireMockServer.stubFor(post(urlEqualTo('/actuator/health'))
                .withHeader("Content-Type", equalTo('application/json'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                    .withStatus(409)
                    .withBody(responseBody)
                ))

        when:
            techEasySolutionsClient.post('/actuator/health', new ServiceHealth(status: ServiceHealthStatusCode.UP), ServiceHealth).get()

        then:
            ExecutionException ex = thrown(ExecutionException)
            ex.cause instanceof ClientConflictException
            ex.cause.serviceError.code == 'code'
            ex.cause.serviceError.param == 'param'
            ex.cause.serviceError.message == 'message'
    }

    void "Call PUT /actuator/health with OK body"() {
        given:
            wireMockServer.running

        and:
            String requestBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.UP.toString()
                ]).toPrettyString()
            String responseBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.DOWN.toString()
                ]).toPrettyString()

        and:
            wireMockServer.stubFor(put(urlEqualTo('/actuator/health'))
                .withHeader("Content-Type", equalTo('application/json'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(responseBody)
                ))

        when:
            CompletableFuture<ServiceHealth> futureResponse = techEasySolutionsClient.put('/actuator/health', new ServiceHealth(status: ServiceHealthStatusCode.UP), ServiceHealth)

        then:
            futureResponse.get().status == ServiceHealthStatusCode.DOWN
            noExceptionThrown()
    }

    void "Call PUT /actuator/health with conflict response"() {
        given:
            wireMockServer.running

        and:
            String requestBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.UP.toString()
                ]).toPrettyString()
            String responseBody = new JsonBuilder(
                [
                    code: 'code',
                    param: 'param',
                    message: 'message'
                ]).toPrettyString()

        and:
            wireMockServer.stubFor(put(urlEqualTo('/actuator/health'))
                .withHeader("Content-Type", equalTo('application/json'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                    .withStatus(409)
                    .withBody(responseBody)
                ))

        when:
            techEasySolutionsClient.put('/actuator/health', new ServiceHealth(status: ServiceHealthStatusCode.UP), ServiceHealth).get()

        then:
            ExecutionException ex = thrown(ExecutionException)
            ex.cause instanceof ClientConflictException
            ex.cause.serviceError.code == 'code'
            ex.cause.serviceError.param == 'param'
            ex.cause.serviceError.message == 'message'
    }

    void "Call PATCH /actuator/health with OK body"() {
        given:
            wireMockServer.running

        and:
            String requestBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.UP.toString()
                ]).toPrettyString()
            String responseBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.DOWN.toString()
                ]).toPrettyString()

        and:
            wireMockServer.stubFor(patch(urlEqualTo('/actuator/health'))
                .withHeader("Content-Type", equalTo('application/json'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withBody(responseBody)
                ))

        when:
            CompletableFuture<ServiceHealth> futureResponse = techEasySolutionsClient.patch('/actuator/health', new ServiceHealth(status: ServiceHealthStatusCode.UP), ServiceHealth)

        then:
            futureResponse.get().status == ServiceHealthStatusCode.DOWN
            noExceptionThrown()
    }

    void "Call PATCH /actuator/health with conflict response"() {
        given:
            wireMockServer.running

        and:
            String requestBody = new JsonBuilder(
                [
                    status: ServiceHealthStatusCode.UP.toString()
                ]).toPrettyString()
            String responseBody = new JsonBuilder(
                [
                    code: 'code',
                    param: 'param',
                    message: 'message'
                ]).toPrettyString()

        and:
            wireMockServer.stubFor(patch(urlEqualTo('/actuator/health'))
                .withHeader("Content-Type", equalTo('application/json'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                    .withStatus(409)
                    .withBody(responseBody)
                ))

        when:
            techEasySolutionsClient.patch('/actuator/health', new ServiceHealth(status: ServiceHealthStatusCode.UP), ServiceHealth).get()

        then:
            ExecutionException ex = thrown(ExecutionException)
            ex.cause instanceof ClientConflictException
            ex.cause.serviceError.code == 'code'
            ex.cause.serviceError.param == 'param'
            ex.cause.serviceError.message == 'message'
    }

    void "Call DELETE /actuator/health with OK and no content"() {
        given:
            wireMockServer.running

        and:
            wireMockServer.stubFor(delete(urlEqualTo('/actuator/health'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .willReturn(aResponse()
                    .withStatus(204)
                ))

        when:
            CompletableFuture<String> futureResponse = techEasySolutionsClient.delete('/actuator/health')

        then:
            futureResponse.get().empty
            noExceptionThrown()
    }

    void "Call DELETE /actuator/health with conflict response"() {
        given:
            wireMockServer.running

        and:
            wireMockServer.stubFor(delete(urlEqualTo('/actuator/health'))
                .withHeader("X-Request-ID", new AnythingPattern())
                .willReturn(aResponse()
                    .withStatus(409)
                    .withBody(new JsonBuilder(
                        [
                            code: 'code',
                            param: 'param',
                            message: 'message'
                        ]).toPrettyString())
                ))

        when:
            techEasySolutionsClient.delete('/actuator/health').get()

        then:
            ExecutionException ex = thrown(ExecutionException)
            ex.cause instanceof ClientConflictException
            ex.cause.serviceError.code == 'code'
            ex.cause.serviceError.param == 'param'
            ex.cause.serviceError.message == 'message'
    }

}
