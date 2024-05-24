package http.client.base;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequestWithoutBody(path, parameters);
    }

    protected <T> ResponseEntity<Object> post(T body) {
        return makeAndSendRequest(body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> serverResponse;

        try {
            serverResponse = rest.exchange("/hit", HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }


    private ResponseEntity<Object> makeAndSendRequestWithoutBody(String path, Map<String, Object> parameters) {
        ResponseEntity<Object> serverResponse;
        try {
            serverResponse = rest.exchange(path, HttpMethod.GET, null, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(serverResponse);
    }

    private ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> serverResponse) {
        if (serverResponse.getStatusCode().is2xxSuccessful()) {
            return serverResponse;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());

        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }

        return responseBuilder.build();
    }
}
