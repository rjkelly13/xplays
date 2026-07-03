import java.util.List;

public class ReportService {
    public static void generateWeeklyReport(Integer year, Integer week) throws Exception  {
        EspnParser parser = new EspnParser();
        List<Play> plays = parser.parseWeekData(year, week);
        System.out.println("\n=== Explosive Plays for Week " + week + ", "
        + year + " ===");
        generateReport(plays);
    }

    public static void generateGameReport(Integer gameId) throws Exception  {
        EspnParser parser = new EspnParser();
        List<Play> plays = parser.parseGameData(gameId);
        generateReport(plays);
    }

    public static void generateReport(List<Play> plays) throws Exception {
        PlayService service = new PlayService();

        List<Play> explosives = service.getExplosivePlays(plays);

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
        service.printRanking(service.countExplosives(explosives, "RUSHING"));
    }
}
