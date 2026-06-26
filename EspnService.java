import java.io.IOException;
import java.time.Duration;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EspnService {
    /**
     * getGameData
     * @param gameId
     * @return a json object of game data from ESPN
     * @throws IOException
     * @throws InterruptedException
     */
    static String get(String endpoint) throws IOException, InterruptedException {
        HttpClient client = HttpClient
            .newBuilder()
            .connectTimeout(Duration.ofMillis(2000))
            .build();
        
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create("https://site.api.espn.com/apis/site/v2/sports/football/nfl/" + endpoint))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
