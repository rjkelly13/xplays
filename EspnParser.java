import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EspnParser {
    /**
     * parse
     * @param gameId
     * @return list of plays parsed from an ESPN json object of game data
     * @throws Exception
     */
    List<Play> parseGameData(Integer gameId) throws Exception {
        List<Play> plays = new ArrayList<>();
        String gameData = getGameData(gameId);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(gameData);

        Map<Integer, String> teams = teamParser(root);

        JsonNode drives = root.at("/drives/previous");

        for (JsonNode drive : drives) {
            JsonNode drive_plays = drive.path("plays");

            for (JsonNode p: drive_plays) {

                String team = teams.get(p.at("/start/team/id").asInt());
                String description = p.path("text").toString();
                int yards = p.path("statYardage").asInt();
                String playType = p.path("type").path("abbreviation").asText("");
                int quarter = p.path("period").path("number").asInt(0);
                String time = p.path("clock").path("displayValue").toString();
                String[] players = playerParser(description, playType);

                plays.add(new Play(gameId, team, description, players[0], players[1], yards, playType, quarter, time));

            }
        }

        return plays;
    }

    List<Play> parseWeekData(Integer year, Integer week) throws Exception {
        List<Play> plays = new ArrayList<>();
        String weekData = getWeekData(year, week);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(weekData);

        Map<Integer, String> teams = teamParser(root);

        JsonNode drives = root.at("/drives/previous");

        /*for (JsonNode drive : drives) {
            JsonNode drive_plays = drive.path("plays");

            for (JsonNode p: drive_plays) {

                String team = teams.get(p.at("/start/team/id").asInt());
                String description = p.path("text").toString();
                int yards = p.path("statYardage").asInt();
                String playType = p.path("type").path("abbreviation").asText("");
                int quarter = p.path("period").path("number").asInt(0);
                String time = p.path("clock").path("displayValue").toString();
                String[] players = playerParser(description, playType);

                plays.add(new Play(gameId, team, description, players[0], players[1], yards, playType, quarter, time));

            }
        }*/

        return plays;
    }

    /**
     * getGameData
     * @param gameId
     * @return a json object of game data from ESPN
     * @throws IOException
     * @throws InterruptedException
     */
    String getWeekData(Integer year, Integer week) throws IOException, InterruptedException {
        String response = EspnService.get("scoreboard?year=" + year + "&week=" + week);

        return response;
    }

    /**
     * getGameData
     * @param gameId
     * @return a json object of game data from ESPN
     * @throws IOException
     * @throws InterruptedException
     */
    String getGameData(Integer gameId) throws IOException, InterruptedException {
        String response = EspnService.get("summary?event=" + gameId);

        return response;
    }

    /**
     * teamParser
     * @param root
     * @return a mapping of the two teams involved in the game + ID to match up later with play data
     */
    Map<Integer, String> teamParser(JsonNode root) {
        Map<Integer, String> teams = new LinkedHashMap<>();
        JsonNode teaminfo = root.at("/boxscore/teams");

        for (JsonNode t : teaminfo) {
            teams.put(t.path("team").path("id").asInt(0), t.path("team").path("name").toString());
        }

        return teams;
    }

    /**
     * playerParser
     * @param desc
     * @param playType
     * @return an array of 1-2 players that were involved in the play. player 2 is null if it's a running play
     */
    String[] playerParser(String desc, String playType) {
        String[] players = new String[2];
        String p = desc;
        p = p.replaceAll("^\"?\\([^)]*\\)\\s*", "");
        String[] sentences = p.split("\\.\\s+");
        for (String s: sentences) {
            if (s.contains(" to ") || s.contains(" for ")) {
                p = s;
                break;
            }
        }

        if (playType.equals("RUSH")) {
            players[0] = p.split(" ")[0];
            players[1] = null;
        } else if (playType.equals("REC")) {
            players[0] = p.split(" ")[0];
            players[1] = p.split(" to ")[1];
        } else if (playType.equals("TD")) {
            if (!p.contains(" to ")) {
                players[0] = p.split(" ")[0];
                players[1] = null;
            } else {
                players[0] = p.split(" ")[0];
                players[1] = p.split(" to ")[1].split(" ")[0];
            }
        }

        return players;
    }
}
