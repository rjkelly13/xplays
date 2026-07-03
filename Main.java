public class Main {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("game"))
            ReportService.generateGameReport(Integer.parseInt(args[1]));
        else if (args[0].equals("week"))
            ReportService.generateWeeklyReport(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }
}
