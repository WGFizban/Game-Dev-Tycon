package com.company.game.engine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    LocalDate currentDay;
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("E dd.MM.yyyy");
    public boolean gameIsOn;
    Scanner input = new Scanner(System.in);
    public List<Player> players = new ArrayList<>();
    public List<GameProject> availableProject = new ArrayList<>();

    //tworzenie menu
    String[] dayOptions = {"Programuj", "Zobacz dostępne projekty", "Zobacz moje projekty", "Szukaj klientów", "Testuj kody", "Oddaj projekt", "Zarządzaj pracownikami", "Rozlicz urzędy", "Wyjdź z programu"};
    String[] testOptions = {"Testuj swój kod", "Testuj kod pracowników", "testuj kod współpracowników"};
    String[] projOptions = {"Wybierz projekt", "Wróć do menu"};
    String[] emplOptions = {"Zatrudnij pracownika", "Zwolnij pracownika"};
    Menu dayMenu = new Menu(dayOptions);
    Menu testMenu = new Menu(testOptions);
    Menu emplMenu = new Menu(emplOptions);
    Menu projMenu = new Menu(projOptions);


    //testowe projekty
    LocalDate allegroTermin = LocalDate.of(2020, 1, 20);
    Integer[] days = new Integer[]{2, 5, 3, 7, 8, 0};
    Integer[] testdays = new Integer[]{1, 1, 1, 0, 1, 0};
    Integer[] readydays = new Integer[]{0, 0, 0, 0, 0, 0};

    GameProject allegro1 = new GameProject("Nowe allegro", ProjectComplexity.EASY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);
    GameProject allegro2 = new GameProject("Nowe allegro", ProjectComplexity.EASY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, readydays);
    GameProject allegro3 = new GameProject("Nowe allegro", ProjectComplexity.MIDDLE, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);
    GameProject allegro4 = new GameProject("Nowe allegro", ProjectComplexity.MIDDLE, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);
    GameProject proj1 = new GameProject("Mini rozszerzenie sklepu", ProjectComplexity.EASY, LocalDate.of(2020, 2, 20), 100.00, 1000.00, 2, testdays);


    //testowi klienci
    Client client1 = new Client("Jan", "Luzacki", ClientCharacter.LUZAK);
    Client client2 = new Client("Alex", "Prosty", ClientCharacter.SKRWL);
    Client client3 = new Client("Mark", "Wymiata", ClientCharacter.WYMAGAJACY);
    Client client4 = new Client("Artur", "Gzyms", ClientCharacter.LUZAK);


    public void startNewGame() {
        gameIsOn = true;
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());
        currentDay = START_DATE;
        players.add(player1);
        //poczatkowe ustawienia - później moze odpowiedni generator
        setAvailableProject();
        setProjectToClient();

    }

    private void setAvailableProject() {
        availableProject.add(allegro1);
        availableProject.add(allegro2);
        availableProject.add(proj1);
    }

    public void checkCash() {
        if (players.get(0).getCash() <= 0) {
            gameIsOn = false;
            System.out.println("Właśnie zbankrotowałeś. Koniec Gry");
        }
    }

    public void newDay() throws InterruptedException {
        System.out.println("Jest " + formatDate.format(currentDay));
        Thread.sleep(1);
        mainAction(dayMenu.selectOptions());
    }

    public void setProjectToClient() {
        //możliwe dodanie z listy dowolnych projektów
        client1.addProject(availableProject.get(0));
        client1.addProject(proj1);
        client2.addProject(allegro2);
        client3.addProject(allegro3);
        client4.addProject(allegro4);
    }

    public void mainAction(int action) throws InterruptedException {
        switch (action) {
            case 0:
                goProgramming();
                break;
            case 1:
                showAvailableProject();
                break;
            case 2:
                showPlayerProject();
                break;
            case 3:

                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                gameIsOn = false;
        }
    }

    private void showAvailableProject() throws InterruptedException {
        for (int i = 0; i < availableProject.size(); i++) {
            GameProject project = availableProject.get(i);
            System.out.println(i + " " + project);
        }
        if (projMenu.selectOptions() == 0) {

            System.out.print("Podaj numer projektu który chcesz wybrać: ");
            int choice = input.nextInt();

            if (canAddProject(availableProject.get(choice), players.get(0))) {
                players.get(0).addProject(availableProject.get(choice));
                availableProject.remove(choice);
                System.out.println("Do końca dnia cieszysz się z podpisanej umowy i nic nie robisz! \n");
                endDay();
            } else {
                System.out.println("Nie możesz podjąć się tego projektu");
                showAvailableProject();
            }

        } else mainAction(dayMenu.selectOptions());
    }

    //logika czy można dodać projekt
    private boolean canAddProject(GameProject project, Player player) {
        return !project.complexity.toString().equals("Skomplikowany") || player.myEmployee.size() != 0;
    }


    private void showPlayerProject() throws InterruptedException {
        players.get(0).showProject();
        mainAction(dayMenu.selectOptions());
    }

    private void goProgramming() throws InterruptedException {
        if (players.get(0).allProjectsReady()) {
            System.out.println("Wszystkie Twoje projekty są gotowe. Pomyśl nad inną opcją.");
            mainAction(dayMenu.selectOptions());

        }
        else if (players.get(0).hasProject()) {
            players.get(0).programmingDay();
            endDay();
        } else {
            System.out.println("Nie masz żadnych projektów nad którymi możesz pracować. Zacznij pracę nad nowym projektem.");
            mainAction(dayMenu.selectOptions());
        }

    }

    private void endDay() {
        currentDay = currentDay.plusDays(1);
    }
}
