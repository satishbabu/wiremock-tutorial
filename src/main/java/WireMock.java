import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMock {
    public static void main(String[] args) {
        WireMockServer wireMockServer = new WireMockServer(options().port(8089));
        wireMockServer.start();
        wireMockServer.stop();
    }
}
