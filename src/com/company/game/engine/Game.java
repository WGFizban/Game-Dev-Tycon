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
    Scanner scan = new Scanner(System.in);
    public List<Player> players = new ArrayList<>();
    public List<GameProject> availableProject = new ArrayList<>();

    //tworzenie menu
    String[] dayOptions = {"Programuj","Zobacz dostępne projekty","Zobacz moje projekty", "Szukaj klientów",  "Testuj kody", "Oddaj projekt", "Zarządzaj pracownikami", "Rozlicz urzędy"};
    String[] testOptions = {"Testuj swój kod", "Testuj kod pracowników", "testuj kod współpracowników"};
    String[] emploptions = {"Zatrudnij pracownika", "Zwolnij pracownika"};
    Menu dayMenu = new Menu(dayOptions);
    Menu testMenu = new Menu(testOptions);
    Menu emplMenu = new Menu(emploptions);


    //testowe projekty
    LocalDate allegroTermin = LocalDate.of(2020, 1, 20);
    Integer[] days = new Integer[]{2, 5, 3, 7, 8, 0};
    GameProject allegro1 = new GameProject("Nowe allegro", ProjectComplexity.LATWY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);
    GameProject allegro2 = new GameProject("Nowe allegro", ProjectComplexity.LATWY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);
    GameProject allegro3 = new GameProject("Nowe allegro", ProjectComplexity.LATWY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);
    GameProject allegro4 = new GameProject("Nowe allegro", ProjectComplexity.LATWY, LocalDate.of(2020, 1, 20), 100.00, 500.00, 7, days);

    //testowi klienci
    Client client1 = new Client("Jan", "Luzacki", ClientCharacter.LUZAK);
    Client client2 = new Client("Alex", "Prosty", ClientCharacter.SKRWL);
    Client client3 = new Client("Mark", "Wymiata", ClientCharacter.WYMAGAJACY);
    Client client4 = new Client("Artur", "Gzyms", ClientCharacter.LUZAK);







    public void startNewGame() {
        gameIsOn = true;
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(scan.nextLine());
        currentDay = START_DATE;
        players.add(player1);
        //poczatkowe ustawienia - później moze odpowiedni generator
        setProjectToClient();
        setAvailableProject();
    }

    private void setAvailableProject() {
        availableProject.add(allegro1);
        availableProject.add(allegro2);
        availableProject.add(allegro3);
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
        client1.addProject(allegro1);
        client2.addProject(allegro2);
        client3.addProject(allegro3);
        client4.addProject(allegro4);
    }
    public void mainAction(int action) throws InterruptedException {
        switch (action){
            case 0:
                players.get(0).programmingDay();
                endDay();
                break;
            case 1:
                showAvailableProject();
                break;
            case 2:
                players.get(0).showProject();
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

        }


    }

    private void endDay() throws InterruptedException {
        currentDay=currentDay.plusDays(1);
        newDay();
    }

    private void showAvailableProject() {
        for (GameProject project : availableProject) {
            System.out.println(project);
        }
    }

}
