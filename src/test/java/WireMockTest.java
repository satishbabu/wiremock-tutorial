import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Note that it doesn't test any code
 */

public class WireMockTest {
    WireMockServer wireMockServer;
    private String wireMockHostAddress = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @lombok.SneakyThrows
    @Test
    public void test() {
        String url = "/satish";
        String response = "Hello Satish!!";

        mapRequestToResponseOnMockServer(url, response);
        verifyResponse(url, response);
        verifyMockServerReceivedCalls(url);
    }

    private void verifyMockServerReceivedCalls(String url) {
        verify(getRequestedFor(urlEqualTo(url)));
    }

    private void verifyResponse(String url, String expectedResponse) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(wireMockHostAddress + url);
        CloseableHttpResponse response = httpClient.execute(request);
        String actualResponse = convertResponseToString(response);
        assertEquals(expectedResponse, actualResponse);
    }

    private void mapRequestToResponseOnMockServer(String url, String response) {
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse().withBody(response)));
    }

    private String convertResponseToString(CloseableHttpResponse response) throws IOException {
        InputStream stream = response.getEntity().getContent();
        Scanner scanner = new Scanner(stream, "UTF-8");
        String responseStr = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseStr;
    }

}
