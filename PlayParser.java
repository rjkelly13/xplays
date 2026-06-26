import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PlayParser {
    List<Play> parse(String filename) throws Exception {
        List<Play> plays = new ArrayList<>();

        // file reading goes here
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line;

        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");

            //GAME1,Eagles,Hurts to Smith for 35 yards,35,pass,1,12:34
            String gameId = p[0];
            String team = p[1];
            String description = p[2];
            int yards = Integer.parseInt(p[3]);
            String playType = p[4];
            int quarter = Integer.parseInt(p[5]);
            String time = p[6];
            String[] players = playerParser(description, playType);

            //plays.add(new Play(gameId, team, description, players[0], players[1], yards, playType, quarter, time));
        }
        br.close();

        return plays;
    }

    String[] playerParser(String desc, String playType) {
        String[] players = new String[2];
        String[] p = desc.split(" ");

        players[0] = p[0];
        players[1] = playType.equals("pass") ? p[2] : null;

        return players;
    }
}


