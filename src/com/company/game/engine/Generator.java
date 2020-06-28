package com.company.game.engine;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    static final String[] NAMES = {"Jan", "Marek", "Anna", "Krzysztof", "Karol", "Tomasz"};
    static final String[] SURNAMES = {"Poważny", "Luzacki", "Wyrozumiały", "Java", "Press", "Kosa"};
    static final String[] CLIENT_CHARACTER = {"LUZAK", "WYMAGAJACY", "SKRWL"};
    static final String[] PROJECT_COMPLEXITY = {"EASY","MIDDLE","HARD"};

    static final String[] PROJECT_FIRST_SEGMENT_NAME = {"Rozszerzenie ", "Nowy ", "Wydajniejszy ", "Sequel "};
    static final String[] PROJECT_LAST_SEGMENT_NAME = {"Allegro", "Sklep", "Matrix", "Fallout", "Simis City", "CITY SKYLINES"};


    //static final String [] TECHNOLOGY = {"front-end","backend","baza danych","mobile","wordpress","prestashop"};
    private int min;
    private int max;



    Client client1 = new Client("Jan", "Luzacki", ClientCharacter.LUZAK);
    Integer[] days = new Integer[]{2, 5, 3, 7, 8, 0};

    GameProject allegro1 = new GameProject("Nowe allegro", ProjectComplexity.EASY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);

    //Ganerator projektów w fazie przygotowywania
//    public GameProject getRandomGameProject(LocalDate now) {
//
//        int randFirstSegmentName = ThreadLocalRandom.current().nextInt(0, PROJECT_FIRST_SEGMENT_NAME.length + 1);
//        int randLastSegmentName = ThreadLocalRandom.current().nextInt(0, PROJECT_LAST_SEGMENT_NAME.length + 1);
//        int randProjComplex = ThreadLocalRandom.current().nextInt(0, PROJECT_COMPLEXITY.length + 1);
//
//
//
//        GameProject randProject = new GameProject(PROJECT_FIRST_SEGMENT_NAME[randFirstSegmentName]+PROJECT_LAST_SEGMENT_NAME[randLastSegmentName],
//                ProjectComplexity.valueOf(PROJECT_COMPLEXITY[randProjComplex]),
//                LocalDate.of
//
//                )
//
//
//        return randProject;
//    }

    public Client getRandomClient() {

        int randCharacter = ThreadLocalRandom.current().nextInt(0, CLIENT_CHARACTER.length );
        int randName = ThreadLocalRandom.current().nextInt(0, NAMES.length );
        int randSurname = ThreadLocalRandom.current().nextInt(0, SURNAMES.length );

        return new Client(NAMES[randName], SURNAMES[randSurname], ClientCharacter.valueOf(CLIENT_CHARACTER[randCharacter]));
    }


}
