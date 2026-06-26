import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

class TeamStats {
    String city;
    String coach;
    int wins;
    int yardage;
    double getWinPct() {
        return wins / 17.0;
    }

    TeamStats(String city, String coach, int wins, int yardage) {
        this.city = city;
        this.coach = coach;
        this.wins = wins;
        this.yardage = yardage;
    }
}

class StandingsService {
    List<Map.Entry<String, TeamStats>> sortTeams(Map<String, TeamStats> teams) {
        List<Map.Entry<String, TeamStats>> sorted = new ArrayList<>(teams.entrySet());

        sorted.sort((a, b) -> {
            TeamStats t1 = a.getValue();
            TeamStats t2 = b.getValue();

            if (t1.wins != t2.wins) {
                return t2.wins - t1.wins;
            }

            if (t1.yardage != t2.yardage) {
                return t2.yardage - t1.yardage;
            }

            return a.getKey().compareTo(b.getKey());
        });

        return sorted;
    }

    void printStandings(List<Map.Entry<String, TeamStats>> sortedTeams) {
        System.out.println("NFC Standings:\n");
        for (Map.Entry<String, TeamStats> t: sortedTeams) {
            TeamStats stats = t.getValue();
            System.out.println(formatRecord(t.getKey(), stats.wins) 
                + " | Win%: " + String.format("%.3f", stats.getWinPct())
                + " | Coach: " + stats.coach 
                + " | Yards: " + stats.yardage
            );
        }
    }

    static String formatRecord(String team, int wins) {
        int losses = 17 - wins;
        return team + " (" + wins + "-" + losses + ")";
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        EspnParser parser = new EspnParser();



        /*List<Play> plays = parser.parseGameData(401772612);

        PlayService service = new PlayService();

        List<Play> explosives = service.getExplosivePlays(plays);

        System.out.println("\n=== Explosive Plays ===");
        for (Play play : explosives) {
            System.out.println(play);
        }

        System.out.println("\n=== By Team ===");
        service.printRanking(service.countExplosivesByTeam(explosives));
        
        System.out.println("\n=== Passing ===");
        service.printRanking(service.countExplosives(explosives, "PASSING"));

        System.out.println("\n=== Receiving ===");
        service.printRanking(service.countExplosives(explosives, "RECEIVING"));

        System.out.println("\n=== Rushing ===");
        service.printRanking(service.countExplosives(explosives, "RUSHING"));*/
    }

    static Map<String, TeamStats> parseStats(String filename) throws Exception {
        Map<String, TeamStats> results = new LinkedHashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            results.put(parts[0], new TeamStats(parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
        }
        br.close();

        return results;
    }
}
