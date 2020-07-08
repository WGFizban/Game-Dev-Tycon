package com.company.game.engine;

import com.company.game.engine.npc.*;
import com.company.game.engine.npc.ClientCharacter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//Generator jest zasymulowany statycznie
public final class Generator {

    static final String[] NAMES = {"Jan", "Marek", "Anna", "Krzysztof", "Karol", "Tomasz", "Paweł", "Piotr", "Wojciech", "Zofia", "Magda", "Jakub", "Alicja", "Weronika", "Kamil", "Kamila"};
    static final String[] SURNAMES = {"Poważny", "Luzacki", "Wyrozumiały", "Java", "Press", "Kosa"};
    static final String[] WORKER_SURNAMES = {"Dokładny", "Wymyślny", "Pospieszny", "Coder", "Pospolity", "Dron"};
    static final String[] CLIENT_CHARACTER = {"LUZAK", "WYMAGAJACY", "SKRWL"};
    static final String[] PROJECT_COMPLEXITY = {"EASY", "MIDDLE", "HARD"};

    static final String[] PROJECT_FIRST_SEGMENT_NAME = {"Rozszerzenie ", "Nowy ", "Wydajniejszy ", "Sequel "};
    static final String[] PROJECT_LAST_SEGMENT_NAME = {"Allegro", "Sklep", "Matrix", "Fallout", "Simis City", "CITY SKYLINES"};

    static final Double TESTER_MAIN_COST = 2000.00;
    static final Double PROGRAMMER_MAIN_COST = 2400.00;
    static final Double DEALER_MAIN_COST = 1200.00;


    //static final String [] TECHNOLOGY = {"front-end","backend","baza danych","mobile","wordpress","prestashop"};
    private int min;
    private int max;

    private Generator() {
    }

    //Ganerator projektów
    public static GameProject getRandomGameProject(LocalDate now) {


        int randFirstSegmentName = ThreadLocalRandom.current().nextInt(0, PROJECT_FIRST_SEGMENT_NAME.length);
        int randLastSegmentName = ThreadLocalRandom.current().nextInt(0, PROJECT_LAST_SEGMENT_NAME.length);
        int randProjectComplexity = ThreadLocalRandom.current().nextInt(0, PROJECT_COMPLEXITY.length);

        int minDay = 1, maxDay = 2, rowModyficator;
        List<Integer> randTechTime = new ArrayList<>();
        int[][] percentagesForTechnologyTime = {{50, 100, 50, 0, 40, 40}, {100, 100, 100, 40, 70, 100}, {100, 100, 100, 50, 100, 100},};
        //{timeModyficatorTechnology,dayModyficatorDeadline} w 3 odmianach dla konkretcnych poziomów złożoności projektu}
        int[][] tableOfIntModyficator = {{0, 16}, {2, 8}, {4, 4},};
        // { penaltyModyficator, rewardModyficator }
        Double[][] tableOfDoubleModyficator = {{50.00, 200.00}, {100.00, 400.00}, {150.00, 600.00},};

        switch (PROJECT_COMPLEXITY[randProjectComplexity]) {
            case "MIDDLE":
                rowModyficator = 1;
                break;
            case "HARD":
                rowModyficator = 2;
                break;
            default:
                rowModyficator = 0;
        }

        //generacja czasów poszczególnych technologii procentowo z modyfikatorem
        for (int i = 0; i < 6; i++) {
            int randTime = (ThreadLocalRandom.current().nextInt(minDay, maxDay + 1)) + tableOfIntModyficator[rowModyficator][0];
            int randTimePercent = (ThreadLocalRandom.current().nextInt(1, 101));
            if (randTimePercent <= percentagesForTechnologyTime[rowModyficator][i]) randTechTime.add(randTime);
            else randTechTime.add(0);
        }
        //ilość dni na projekt od dnia generacji = min czas projektu + modyfikator
        int dayDeadline = 0;
        for (Integer time : randTechTime) dayDeadline += time;
        dayDeadline += tableOfIntModyficator[rowModyficator][1];
        int forMoney = ThreadLocalRandom.current().nextInt(6, 13);

        Integer[] randTechDuration = new Integer[randTechTime.size()];
        for (int i = 0; i < randTechTime.size(); i++) {
            Integer time = randTechTime.get(i);
            randTechDuration[i] = time;
        }

        return new GameProject(PROJECT_FIRST_SEGMENT_NAME[randFirstSegmentName] + PROJECT_LAST_SEGMENT_NAME[randLastSegmentName],
                ProjectComplexity.valueOf(PROJECT_COMPLEXITY[randProjectComplexity]),
                now.plusDays(dayDeadline),
                (tableOfDoubleModyficator[rowModyficator][0] * forMoney),
                (tableOfDoubleModyficator[rowModyficator][1] * forMoney),
                (ThreadLocalRandom.current().nextInt(minDay, maxDay + 1)) + tableOfIntModyficator[rowModyficator][0],
                randTechDuration
        );
    }

    public static Client getRandomClient() {

        int randCharacter = ThreadLocalRandom.current().nextInt(0, CLIENT_CHARACTER.length);
        int randName = ThreadLocalRandom.current().nextInt(0, NAMES.length);
        int randSurname = ThreadLocalRandom.current().nextInt(0, SURNAMES.length);

        return new Client(NAMES[randName], SURNAMES[randSurname], ClientCharacter.valueOf(CLIENT_CHARACTER[randCharacter]));
    }


    public static Employee getRandomEmployee(Occupation occupation) {
        int randName = ThreadLocalRandom.current().nextInt(0, NAMES.length);
        int randSurname = ThreadLocalRandom.current().nextInt(0, WORKER_SURNAMES.length);
        double cost;

        switch (occupation) {
            case DEALER:
                cost = DEALER_MAIN_COST;
                return new Dealer(NAMES[randName], WORKER_SURNAMES[randSurname], cost, cost / 4, cost / 2);
            case TESTER:
                cost = TESTER_MAIN_COST;
                return new Tester(NAMES[randName], WORKER_SURNAMES[randSurname], cost, cost / 4, cost / 2);
            default:
                cost = PROGRAMMER_MAIN_COST; //w późniejszej fazie koszty programistów zostaną zmienione (brakuje tu jeszcze parametrów losowych technologii które programisci umieją(będą to 3 losowe technologie))
                return new Programmer(NAMES[randName], WORKER_SURNAMES[randSurname], cost, cost / 4, cost / 2);
        }

    }


    //szansa w procentach że coś się uda
    public static boolean checkPercentegesChance(int chance) {
        int randPercent = (ThreadLocalRandom.current().nextInt(1, 101));
        return chance >= randPercent;
    }


    public static void randomAddProjectToClient(List<GameProject> projects, List<Client> clients) {
        for (GameProject project : projects) {
            int ranIndexClient = ThreadLocalRandom.current().nextInt(0, clients.size());
            clients.get(ranIndexClient).addProject(project);
        }
    }
}



