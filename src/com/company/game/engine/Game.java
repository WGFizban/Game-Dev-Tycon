package com.company.game.engine;

import com.company.game.engine.npc.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    //data rozpoczęcia gry i ilosc dostepnych projektów
    static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    static final int START_AVAILABLE_PROJECTS = 3;
    //początkowa liczba dostępnych pracowników
    static final int START_AVAILABLE_TESTERS = 1;
    static final int START_AVAILABLE_PROGRAMMERS = 2;
    static final int START_AVAILABLE_DEALER = 1;


    LocalDate currentDay;
    Player playerOnTurn;
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

    public boolean gameIsOn;

    public List<Player> players = new ArrayList<>();
    public List<GameProject> availableProject = new ArrayList<>();
    public List<Employee> availableEmployee = new ArrayList<>(); //lista do przechowywania dostępnych pracowników
    public List<Client> allClient = new ArrayList<>();

    public Game() throws InterruptedException {
        startNewGame();
    }

    //tworzenie menu
    Menu dayMenu = new Menu("Programuj", "Zobacz dostępne projekty", "Zobacz moje projekty", "Szukaj klientów/projektów", "Testuj kody", "Oddaj projekt", "Zarządzaj pracownikami", "Rozlicz urzędy", "Wyjdź z programu");
    Menu testMenu = new Menu("Testuj swój kod", "Testuj kod pracowników", "testuj kod współpracowników");
    Menu hireMenu = new Menu("Zatrudnij pracownika", "Zwolnij pracownika", "Zainwestuj w reklamy by szukać pracowników (300.00)", "Wróć do menu");
    Menu projMenu = new Menu("Wybierz projekt", "Wróć do menu");


    public void startNewGame() throws InterruptedException {
        //ekran powitalny
        System.out.println("Witaj w Game dev Tycon symulatorze firmy IT. Przygoda rozpoczyna się 1 stycznia 2020r\nPodejmuj mądre decyzje i nie zbankrutuj. Powodzenia!\n");

        //wprowadzenie graczy
        int isMorePlayer;
        Scanner input = new Scanner(System.in);
        do {

            System.out.print("Podaj Nick dla gracza: ");
            Player player = new Player(input.nextLine());
            isMorePlayer = dayMenu.selectOptions(2, "Czy będzie więcej graczy?  0 - nie   1 - tak    wybierasz: ");
            players.add(player);
        } while (isMorePlayer == 1);

        //poczatkowe ustawienia
        setStartProporties();

        //start gry dla kazdego z graczy
        while (gameIsOn) {
            for (Player player : players) {
                playerOnTurn = player;
                startDay(playerOnTurn);
            }
        }
    }

    private void setStartProporties() {

        for (int i = 0; i < START_AVAILABLE_PROJECTS; i++) {
            availableProject.add(Generator.getRandomGameProject(START_DATE));
            allClient.add(Generator.getRandomClient());
        }
        Generator.randomAddProjectToClient(availableProject, allClient);
        //ustawienie początkowych pracowników
        for (int i = 0; i < START_AVAILABLE_DEALER; i++) {
            availableEmployee.add(Generator.getRandomEmployee(Occupation.DEALER));
        }
        for (int i = 0; i < START_AVAILABLE_TESTERS; i++) {
            availableEmployee.add(Generator.getRandomEmployee(Occupation.TESTER));
        }
        for (int i = 0; i < START_AVAILABLE_PROGRAMMERS; i++) {
            availableEmployee.add(Generator.getRandomEmployee(Occupation.PROGRAMMER));
        }
        gameIsOn = true;
        currentDay = START_DATE;

        // część testowa

        players.get(0).myEmployee.add(Generator.getRandomEmployee(Occupation.PROGRAMMER));
        players.get(0).myEmployee.add(Generator.getRandomEmployee(Occupation.PROGRAMMER));
        players.get(0).myEmployee.add(Generator.getRandomEmployee(Occupation.TESTER));
        players.get(0).myEmployee.add(Generator.getRandomEmployee(Occupation.DEALER));

    }


    public void startDay(Player player) throws InterruptedException {
        if (!gameIsOn) return;

        System.out.println("\nTura gracza " + player.nickName + "\nJest " + formatDate.format(currentDay));
        checkIsTimeForReward(currentDay);

        //kod by prcowali pracownicy
        employeeToWork(player);

        System.out.println("Twój stan konta: " + player.getCash());


        mainAction(dayMenu.selectOptions());
    }

    private void employeeToWork(Player player) {
        if (!(currentDay.getDayOfWeek().toString().equals("SUNDAY")) && player.myEmployee.size() > 0) {
            System.out.println("Twoi pracownicy pracują");
            for (Employee employee : player.myEmployee) {
                if (employee.mainOccupation.equals(Occupation.DEALER)) {
                    if (employee.doYourWorkForPlayer(player)) {
                        GameProject project = Generator.getRandomGameProject(currentDay);
                        project.owner = Generator.getRandomClient();
                        availableProject.add(project);
                        System.out.println("Sprzedawca znalazł nowy projekt! sprawdź go w dostępnych zleceniach.");
                    }
                } else employee.doYourWorkForPlayer(player);
            }
        }
    }


    public void mainAction(int action) throws InterruptedException {
        System.out.println();
        switch (action) {
            case 0:
                goProgrammingPlayer();
                break;
            case 1:
                chooseFromAvailableProject();
                break;
            case 2:
                showPlayerProject();
                break;
            case 3:
                searchNewProject(playerOnTurn);
                break;
            case 4:
                testProject();
                break;
            case 5:
                uploadReadyProject(playerOnTurn);
                break;
            case 6:
                //System.out.println("Opcja nie została zaimplementowana");
                manageEmployee(playerOnTurn);
                break;
            case 7:
                payOfficialFees();
                break;
            default:
                gameIsOn = false;
        }
    }

    private void manageEmployee(Player player) throws InterruptedException {
        switch (hireMenu.selectOptions()) {
            case 0:
                //zatrudnianie pracowników
                showAvailableEmployee();
                if (dayMenu.selectOptions(2, "Czy chcesz teraz zatrudnić pracowników?\n0 - Tak lub 1 - Inna opcja     Twój wybór:") == 1)
                    manageEmployee(player);
                else {
                    int toHire = dayMenu.selectOptions(availableEmployee.size(), "Wybierz pracownika którego chcesz zatrudnić: ");
                    availableEmployee.get(toHire).hire(player);
                }
                endDay(playerOnTurn);
                break;
            case 1: //zwalnianie pracowników
                if (player.myEmployee.size() == 0) {
                    System.out.println("Nie masz jeszcze żadnych pracowników by ich zwalniać! Wybierz inną opcję");
                    manageEmployee(player);
                } else {
                    player.showMyEmployee();
                    int toDismiss = dayMenu.selectOptions(player.myEmployee.size(), "Wybierz pracownika którego chcesz zwolnić");
                    player.myEmployee.get(toDismiss).dismiss(player);
                }
                endDay(playerOnTurn);
                break;
            case 2:
                //kampania reklamowa;
                break;
            case 3:
                mainAction(dayMenu.selectOptions());
                break;
        }
    }

    private void showAvailableEmployee() {
        System.out.println();
        for (int i = 0; i < availableEmployee.size(); i++) {
            Employee emp = availableEmployee.get(i);
            System.out.println(i + " " + emp);
        }
    }

    private void uploadReadyProject(Player player) throws InterruptedException {
        //sprawdzanie posiadania projektów
        if (player.myProjects.size() == 0) {
            System.out.println("Musisz POSIADAĆ jakikolwiek projekt");
            mainAction(dayMenu.selectOptions());
            //sprawdzenie posiadania gotowego projektu
        } else if (!player.anyoneProjectIsReady()) {
            System.out.println("Musisz posiadać gotowy projekt żeby go oddać");
            mainAction(dayMenu.selectOptions());
        } else {
            player.showMyProjects();
            int choice = dayMenu.selectOptions(player.myProjects.size(), "Możesz oddać tylko jeden projekt dziennie, który wybierasz: ");
            if (!player.myProjects.get(choice).ready) {
                System.out.println("Musisz wybrać gotowy projekt!\n");
                uploadReadyProject(player);
            } else {
                //sprawdzenie przedawnienia
                Double penalty = checkPeanalty(player, player.myProjects.get(choice), currentDay);
                //błedy w projekcie a kontract
                boolean isContract = impactErrorOnContract(player.myProjects.get(choice));
                System.out.println("\nPomyślnie oddałeś projekt " + player.myProjects.get(choice).projectName + " do klienta " + player.myProjects.get(choice).owner.firstName + " " + player.myProjects.get(choice).owner.lastName +
                        "\nJeśli wszystko dobrze pójdzie zapłatę dostaniesz w " + formatDate.format(currentDay.plusDays(player.myProjects.get(choice).timeOfReward)));

                if (penalty > 0) {
                    System.out.println("Nie wykonałeś projektu w terminie i zapłacisz karę: " + penalty + " która w tym momencie jest odejmowana z Twojego konta.");
                    player.setCash(player.getCash() - penalty);
                }
                if (!isContract) {
                    System.out.println("Straciłeś kontrakt z klientem przez oddanie projektu z błędami! Nie otrzymasz żadnej zapłaty za projekt.");
                } else {
                    int daysForClient = additionalTimeForClient(player.myProjects.get(choice));

                    if ("SKRWL".equals(player.myProjects.get(choice).owner.character.toString())) {
                        if (!Generator.checkPercentegesChance(1)) {
                            addDateAndFinishedProject(currentDay.plusDays(daysForClient + player.myProjects.get(choice).timeOfReward), player.myProjects.get(choice));
                        }
                    } else {
                        addDateAndFinishedProject(currentDay.plusDays(daysForClient + player.myProjects.get(choice).timeOfReward), player.myProjects.get(choice));
                    }
                    player.myProjects.remove(choice);
                    endDay(playerOnTurn);
                }
            }
        }

    }

    private void addDateAndFinishedProject(LocalDate dateReward, GameProject project) {
        playerOnTurn.dateOfPrizeForProject.add(dateReward);
        playerOnTurn.finishedProjects.add(project);
    }

    private int additionalTimeForClient(GameProject project) {

        switch (project.owner.character.toString()) {
            case "LUZAK":
                if (Generator.checkPercentegesChance(30)) return 7;
                else return 0;
            case "SKRWL":
                if (Generator.checkPercentegesChance(30)) return 7;
                else if (Generator.checkPercentegesChance(5)) return 31;
                else return 0;
            default:
                return 0;
        }

    }

    private boolean impactErrorOnContract(GameProject project) {

        if (project.coderError > 0) {
            switch (project.owner.character.toString()) {
                case "WYMAGAJACY":
                    return Generator.checkPercentegesChance(50);
                case "SKRWL":
                    return false;
                default:
                    return true;
            }
        }
        return true;
    }

    private Double checkPeanalty(Player player, GameProject project, LocalDate currentDay) {
        double penalty = 0.0;
        if (currentDay.isAfter(project.deadLine)) {

            if ("LUZAK".equals(project.owner.character.toString())) {
                //poprawka w porównywania dat (opóznienie nie większe niż tydzień czyli równe mniejsze tydzień)
                if (project.deadLine.plusDays(7).isAfter(currentDay) || project.deadLine.plusDays(7).isEqual(currentDay)) {
                    if (!Generator.checkPercentegesChance(20)) penalty = project.penalty;
                } else penalty = project.penalty;
            } else penalty = project.penalty;
            //naliczenie kar za przedawnienie projektu w zależności od charakteru kleinta
        }
        return penalty;
    }

    //poświęcenie dnia na testowanie daje gwarancję oddania sprawnego kodu
    private void testProject() throws InterruptedException {
        switch (testMenu.selectOptions()) {
            case 0:
                testPlayerProject(playerOnTurn);
                break;
            case 1:
                System.out.println("Opcja nie została zaimplementowana");
                break;
            case 2:
                System.out.println("Opcja nie została zaimplementowana");
                break;
        }
    }

    private void testPlayerProject(Player player) throws InterruptedException {
        if (player.myProjects.size() == 0 || player.areProjectsToTest()) {
            System.out.println("\nNie masz projektów lub nie wymagają one na razie testowania");
            mainAction(dayMenu.selectOptions());
        } else {
            player.showMyProjects();
            int choice = dayMenu.selectOptions(player.myProjects.size(), "Wybierz projekt który chcesz testować");
            if (player.myProjects.get(choice).coderError == 0) {
                System.out.println("Ten projekt nie miał błędów. wybierz inny projekt!\n");
                testPlayerProject(player);
            } else {
                player.myProjects.get(choice).coderError = 0;
                System.out.println("Cały dzień testujesz ale masz pewność że już napisany kod jest 100% poprawny");
                endDay(playerOnTurn);
            }
        }
    }

    private void searchNewProject(Player player) {
        if (!player.searchProject()) {
            System.out.println("Dzisiaj cały dzień poszukujesz nowych zleceń w internecie. Do znalezienia projektu potrzeba jeszcze " + (5 - player.getDayForLookingClient()) + " wywołania.");
        } else {
            System.out.println("\nZnalezłeś nowy projekt. Sprawdź go jutro w dostępnych zleceniach!");
            addNewAvailableProjeckt();
        }
        endDay(playerOnTurn);
    }

    private void payOfficialFees() throws InterruptedException {
        if (playerOnTurn.countFeePerMonth == 2) {
            System.out.println("W tym miesiącu opłaciłeś już urzędy wymaganą ilość razy. ZUS jest Ci wdzięczny.\nWybierz inną opcję.");
            mainAction(dayMenu.selectOptions());
        } else {
            //opłata urzędów jako 5 % dwa razy w msc
            playerOnTurn.setCash(playerOnTurn.getCash() - (playerOnTurn.getCash() * 0.05));
            checkCash();
            playerOnTurn.countFeePerMonth++;
            System.out.println("Dzień papierkowej roboty. Dokonujesz opłat " + playerOnTurn.countFeePerMonth + " raz w tym miesiącu. Pobraliśmy wymagane opłaty.");
            endDay(playerOnTurn);
        }


    }

    private void chooseFromAvailableProject() throws InterruptedException {
        if (availableProject.size() == 0) {
            System.out.println("Na razie nie ma tu żadnych nowych zleceń którymi mógłbyś się zająć");
            mainAction(dayMenu.selectOptions());
        } else {
            for (int i = 0; i < availableProject.size(); i++) {
                GameProject project = availableProject.get(i);
                System.out.println(i + " " + project);
            }
            if (projMenu.selectOptions() == 0) {


                int choice = dayMenu.selectOptions(availableProject.size(), "Podaj numer projektu który chcesz wybrać: ");

                if (canAddProject(availableProject.get(choice), playerOnTurn)) {
                    playerOnTurn.addProject(availableProject.get(choice));
                    availableProject.remove(choice);
                    System.out.println("Do końca dnia cieszysz się z podpisanej umowy i nic nie robisz!");
                    endDay(playerOnTurn);
                } else {
                    System.out.println("Nie możesz podjąć się tego projektu ze względu na jego złożoność\n");
                    chooseFromAvailableProject();
                }
            } else mainAction(dayMenu.selectOptions());

        }
    }

    //logika czy można dodać projekt - odrzucenie skomplikowanych projektów przy braku pracowników
    private boolean canAddProject(GameProject project, Player player) {
        return !project.complexity.toString().equals("Skomplikowany") || player.myEmployee.size() != 0;
    }


    private void showPlayerProject() throws InterruptedException {
        playerOnTurn.checkAndShowProject();
        mainAction(dayMenu.selectOptions());
    }

    private void goProgrammingPlayer() throws InterruptedException {
        if (playerOnTurn.hasProject() && playerOnTurn.allProjectAreReady()) {
            System.out.println("Wszystkie Twoje projekty są gotowe. Pomyśl nad inną opcją.");
            mainAction(dayMenu.selectOptions());

        } else if (playerOnTurn.hasProject()) {

            playerOnTurn.checkAndShowProject();

            int choice = dayMenu.selectOptions(playerOnTurn.myProjects.size(), "Wybierz projekt nad którym chcesz pracować:");
            if (!playerOnTurn.myProjects.get(choice).ready && !(playerOnTurn.isOnlyMobile(playerOnTurn.myProjects.get(choice)))) {
                playerOnTurn.programmingDay(playerOnTurn.myProjects.get(choice));
                endDay(playerOnTurn);
            } else if (playerOnTurn.isOnlyMobile(playerOnTurn.myProjects.get(choice))) {
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


    private void addNewAvailableProjeckt() {

        GameProject anotherOne = Generator.getRandomGameProject(currentDay);
        Generator.getRandomClient().addProject(anotherOne);
        addProjectToAvailable(anotherOne);

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
        if (lastTermin == 2 && !(playerOnTurn.countFeePerMonth == 2)) {
            System.out.println("\nOSTRZEŻENIE! Dwa dni do końca miesiąca. Jeśli nie poświęcisz wymaganych dni: " + (2 - playerOnTurn.countFeePerMonth) + " na rozliczenia z urzędami przegrasz!");
        }
        if (!(nextMonth == currentDay.getMonthValue()) && playerOnTurn.countFeePerMonth < 2) {
            System.out.println("\nW tym miesiącu zapomniałeś dwukrotnie rozliczyś się z urzędami. Wpada kontrola i zamykasz firmę z długami.");
            playerOnTurn.setCash(0.0);
        } else if ((!(nextMonth == currentDay.getMonthValue())) && playerOnTurn.countFeePerMonth == 2) {
            playerOnTurn.countFeePerMonth = 0;
            System.out.println("\nRozpoczyna się nowy miesiąc. Dobrze że pamiętałeś o Zus :) ");
        }
    }

    public void checkCash() {
        if (playerOnTurn.getCash() <= 0) {
            gameIsOn = false;
            System.out.println("Gracz " + playerOnTurn + " właśnie zbankrutował. Koniec Gry!!!");
        } else if (playerOnTurn.getCash() >= (10 * Player.DEFAULT_STARTING_CASH)) {
            gameIsOn = false;
            System.out.println("Gracz " + playerOnTurn + " zarobił 10x więcej pieniędzy niż miał na początku gry. WYGRYWA!!! GRATULUJĘ!");
        }
    }

    private void endDay(Player actuallyPlayer) {
        checkGameDuration(currentDay.plusDays(1));
        if (actuallyPlayer.equals(players.get(players.size() - 1))) {
            currentDay = currentDay.plusDays(1);
        }
    }

    private void checkIsTimeForReward(LocalDate currentDay) {
        for (int i = 0; i < playerOnTurn.dateOfPrizeForProject.size(); i++) {
            LocalDate dateNow = playerOnTurn.dateOfPrizeForProject.get(i);
            //poprawka w porównywaniu dat (wartości a nie obiekty )
            if (dateNow.isEqual(currentDay)) {
                System.out.println("Dostałeś przelew na konto w wysokości " + playerOnTurn.finishedProjects.get(i).reward + " za zakończony projekt " + playerOnTurn.finishedProjects.get(i).projectName);
                playerOnTurn.setCash(playerOnTurn.getCash() + playerOnTurn.finishedProjects.get(i).reward);
            }
        }
        //zwalnianie pamięci w zmiennych listowych
        for (int i = 0; i < playerOnTurn.dateOfPrizeForProject.size(); i++) {
            LocalDate oldDate = playerOnTurn.dateOfPrizeForProject.get(i);
            if (currentDay.isAfter(oldDate)) {
                playerOnTurn.finishedProjects.remove(i);
                playerOnTurn.dateOfPrizeForProject.remove(i);
                break;
            }
        }
    }

}
