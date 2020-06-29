package com.company.game.engine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    //data rozpoczęcia gry i ilosc dostepnych projektów
    static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    static final int START_AVAILABLE_PROJECTS=3;

    LocalDate currentDay;
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

    Generator gen = new Generator();
    public boolean gameIsOn;

    Scanner input = new Scanner(System.in);
    public List<Player> players = new ArrayList<>();
    public List<GameProject> availableProject = new ArrayList<>();
    public List<Client> allClient = new ArrayList<>();

    //tworzenie menu
    String[] dayOptions = {"Programuj", "Zobacz dostępne projekty", "Zobacz moje projekty", "Szukaj klientów/projektów", "Testuj kody", "Oddaj projekt", "Zarządzaj pracownikami", "Rozlicz urzędy", "Wyjdź z programu"};
    String[] testOptions = {"Testuj swój kod", "Testuj kod pracowników", "testuj kod współpracowników"};
    String[] projOptions = {"Wybierz projekt", "Wróć do menu"};
    String[] emplOptions = {"Zatrudnij pracownika", "Zwolnij pracownika"};
    Menu dayMenu = new Menu(dayOptions);
    Menu testMenu = new Menu(testOptions);
    Menu emplMenu = new Menu(emplOptions);
    Menu projMenu = new Menu(projOptions);




    public void startNewGame(Player player) {
        players.add(player);

        gameIsOn = true;
        currentDay = START_DATE;

        //poczatkowe ustawienia
        setStartProporties();
        //ekran powitalny
        System.out.println("Witaj w Game dev Tycon " + players.get(0).nickName);
        System.out.println("Zaraz rozpocznie się Twoja przygoda. Zaczniesz 1 stycznia 2020r. Twoja początkowa pensja: " + players.get(0).getCash() +
                "\nPodejmuj mądre decyzje i nie zbankrutuj. Powodzenia!");
    }

    private void setStartProporties() {

        for (int i = 0; i <START_AVAILABLE_PROJECTS ; i++) {
            availableProject.add(gen.getRandomGameProject(START_DATE));
            allClient.add(gen.getRandomClient());
        }
        gen.randomAddProjectToClient(availableProject,allClient);

    }


    public void newDay() throws InterruptedException {
        System.out.println("\nJest " + formatDate.format(currentDay) + "\n" + "Twój stan konta: " + players.get(0).getCash());
        mainAction(dayMenu.selectOptions());
    }



    public void mainAction(int action) throws InterruptedException {
        System.out.println();
        switch (action) {
            case 0:
                goProgrammingPlayer();
                break;
            case 1:
                showAvailableProject();
                break;
            case 2:
                showPlayerProject();
                break;
            case 3:
                searchNewProject();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                payOfficialFees();
                break;
            default:
                gameIsOn = false;
        }
    }

    private void searchNewProject() {
        if (players.get(0).dayForLookingClient < 5) {
            players.get(0).dayForLookingClient++;
            System.out.println("Dzisiaj cały dzień poszukujesz nowych zleceń w internecie. Do znalezienia projektu potrzeba jeszcze " + (5 - players.get(0).dayForLookingClient) + " wywołania.");
            endDay();
        }

    }

    private void payOfficialFees() throws InterruptedException {
        if (players.get(0).countFeePerMonth == 2) {
            System.out.println("W tym miesiącu opłaciłeś już urzędy wymaganą ilość razy. ZUS jest Ci wdzięczny.\nWybierz inną opcję.");
            mainAction(dayMenu.selectOptions());
        } else {
            //opłata urzędów jako 5 % dwa razy w msc
            players.get(0).setCash(players.get(0).getCash() - (players.get(0).getCash() * 0.05));
            checkCash();
            players.get(0).countFeePerMonth++;
            System.out.println("Dzień papierkowej roboty. Dokonujesz opłat " + players.get(0).countFeePerMonth + " raz w tym miesiącu. Pobraliśmy wymagane opłaty.");
            endDay();
        }


    }

    private void showAvailableProject() throws InterruptedException {
        if (availableProject.size() == 0) {
            System.out.println("Na razie nie ma tu żadnych nowych zleceń którymi mógłbyś się zająć");
            mainAction(dayMenu.selectOptions());
        } else {
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
                    System.out.println("Do końca dnia cieszysz się z podpisanej umowy i nic nie robisz!");
                    endDay();
                } else {
                    System.out.println("Nie możesz podjąć się tego projektu ze względu na jego złożoność");
                    showAvailableProject();
                }
            } else mainAction(dayMenu.selectOptions());

        }
    }

    //logika czy można dodać projekt - odrzucenie skomplikowanych projektów przy braku pracowników
    private boolean canAddProject(GameProject project, Player player) {
        return !project.complexity.toString().equals("Skomplikowany") || player.myEmployee.size() != 0;
    }


    private void showPlayerProject() throws InterruptedException {
        players.get(0).showProject();
        mainAction(dayMenu.selectOptions());
    }

    private void goProgrammingPlayer() throws InterruptedException {
        if (players.get(0).hasProject() && players.get(0).allProjectsReady()) {
            System.out.println("Wszystkie Twoje projekty są gotowe. Pomyśl nad inną opcją.");
            mainAction(dayMenu.selectOptions());

        } else if (players.get(0).hasProject()) {
            players.get(0).showProject();

            System.out.println("Wybierz projekt nad którym chcesz pracować");
            int choice = input.nextInt();
            if (!players.get(0).myProjects.get(choice).ready && !(players.get(0).isOnlyMobile(players.get(0).myProjects.get(choice)))) {
                players.get(0).programmingDay(choice);
                endDay();
            } else if (players.get(0).isOnlyMobile(players.get(0).myProjects.get(choice))) {
                System.out.println("Ten projekt wymaga jeszcze technologii mobilnej której nie potrafisz. Wracasz do menu. \nPomyśl nad zleceniem tej częsci komuś lub zatrudnij odpowiedniego pracownika.");
                mainAction(dayMenu.selectOptions());
            } else {
                System.out.println("Ten projekt jest już gotowy. Wracasz do menu. Następnym razem wybierz niedokończony projekt.");
                mainAction(dayMenu.selectOptions());
            }
        } else {
            System.out.println("Nie masz projektów nad którymi możesz pracować. Zacznij pracę nad nowym projektem.");
            mainAction(dayMenu.selectOptions());
        }
    }


    private void checkNewProjeckt() {
        if (players.get(0).dayForLookingClient == 5) {
            players.get(0).dayForLookingClient = 0;
            GameProject anotherOne = gen.getRandomGameProject(currentDay);
            gen.getRandomClient().addProject(anotherOne);
            addProjectToAvailable(anotherOne);
            System.out.println("\nZnalezłeś nowy projekt. Sprawdź go jutro w dostępnych zleceniach!");
        }
    }

    //przygotowanie do dodawania projektów
    private void addProjectToAvailable(GameProject proj) {
        availableProject.add(proj);
    }


    public void checkGameDuration(LocalDate nextDate) {
        checkZus(nextDate.getMonthValue());
        checkCash();
    }

    private void checkZus(int nextMonth) {
        int lastTermin = currentDay.lengthOfMonth() - currentDay.getDayOfMonth();
        //Awaryjna przypominajka dla gracza
        if (lastTermin == 2) {
            System.out.println("\nOSTRZEŻENIE! Dwa dni do końca miesiąca. Jeśli nie poświęcisz wymaganych dni: " + (2 - players.get(0).countFeePerMonth) + " na rozliczenia z urzędami przegrasz!");
        }
        if (!(nextMonth == currentDay.getMonthValue()) && players.get(0).countFeePerMonth < 2) {
            System.out.println("\nW tym miesiącu zapomniałeś dwukrotnie rozliczyś się z urzędami. Wpada kontrola i zamykasz firmę z długami.");
            players.get(0).setCash(0.0);
        } else if ((!(nextMonth == currentDay.getMonthValue())) && players.get(0).countFeePerMonth == 2) {
            players.get(0).countFeePerMonth = 0;
            System.out.println("\nRozpoczyna się nowy miesiąc. Dobrze że pamiętałeś o Zus :) ");
        }
    }

    public void checkCash() {
        if (players.get(0).getCash() <= 0) {
            gameIsOn = false;
            System.out.println("Właśnie zbankrutowałeś. Koniec Gry!!!");
        }
    }

    private void endDay() {
        checkGameDuration(currentDay.plusDays(1));
        checkNewProjeckt();
        currentDay = currentDay.plusDays(1);
    }

}
