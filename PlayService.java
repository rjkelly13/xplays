import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayService {
    static int EXPLOSIVE_THRESHOLD = 20;

    /**
     * isExplosive
     * @param play
     * @return a true/false bool whether or not the play counts as an explosive play
     */
    boolean isExplosive(Play play) {
        return play.yards >= EXPLOSIVE_THRESHOLD && (play.playType.equals("REC") || play.playType.equals("RUSH")|| play.playType.equals("TD"));
    }

    /**
     * getExplosivePlays
     * @param plays
     * @return a list of explosive plays
     */
    List<Play> getExplosivePlays(List<Play> plays) {
        List<Play> result = new ArrayList<>();

        for (Play p : plays) {
            if (isExplosive(p))
                result.add(p);
        }

        return result;
    }

    /**
     * countExplosives
     * @param plays
     * @param category
     * @return a mapping of explosive plays based on the categories of PASSING, RECEIVING or RUSHING
     * @throws Exception
     */
    Map<String, Integer> countExplosives(List<Play> plays, String category) throws Exception {
        Map<String, Integer> results = new LinkedHashMap<>();

        for (Play p : plays) {
            if (category.equals("PASSING")) {
                if (p.playType.equals("REC") || (p.playType.equals("TD") && p.player2 != null))
                    results.put(p.player1, results.getOrDefault(p.player1, 0) + 1);
            } else if (category.equals("RECEIVING")) {
                if (p.playType.equals("REC") || (p.playType.equals("TD") && p.player2 != null))
                    results.put(p.player2, results.getOrDefault(p.player2, 0) + 1);
            } else if (category.equals("RUSHING")) {
                if (p.playType.equals("RUSH") || (p.playType.equals("TD") && p.player2 == null))
                    results.put(p.player1, results.getOrDefault(p.player1, 0) + 1);
            }
        }
        
        return results;
    }

    /**
     * countExplosivesByTeam
     * @param plays
     * @return counts the amount of explosive plays by team
     */
    Map<String, Integer> countExplosivesByTeam(List<Play> plays) {
        Map<String, Integer> map = new LinkedHashMap<>();

        for (Play p: plays) {
            if (isExplosive(p)) {
                map.put(p.team, map.getOrDefault(p.team, 0) + 1);
            }
        }

        return map;
    }

    /**
     * countExplosivesByPlayer
     * @param plays
     * @return counts the amount of explosive players by player
     */
    Map<String, Integer> countExplosivesByPlayer(List<Play> plays) {
        Map<String, Integer> map = new LinkedHashMap<>();

        for (Play p: plays) {
            if (!isExplosive(p)) continue;

            // player1 always included
            map.put(p.player1, map.getOrDefault(p.player1, 0) + 1);

            // player2 only exists for passes
            if (p.player2 != null) {
                map.put(p.player2, map.getOrDefault(p.player2, 0) + 1);
            } 

        }
        
        return map;
    }

    /**
     * printRanking
     * @param map
     */
    void printRanking(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        for (Map.Entry<String, Integer> e: list) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}
