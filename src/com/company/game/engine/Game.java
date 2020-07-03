package com.company.game.engine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Game {

    //data rozpoczęcia gry i ilosc dostepnych projektów
    static final LocalDate START_DATE = LocalDate.of(2020, 1, 1);
    static final int START_AVAILABLE_PROJECTS = 3;


    LocalDate currentDay;
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("E dd.MM.yyyy");

    public boolean gameIsOn;

    public List<Player> players = new ArrayList<>();
    public List<GameProject> availableProject = new ArrayList<>();
    public List<Client> allClient = new ArrayList<>();

    //listy pamiętające daty i wielkości wypłat za gotowe projekty
    public List<LocalDate> dateOfPrize = new ArrayList<>();
    private List<GameProject> finishedProjects = new ArrayList<>();

    public Game(Player player) {
        startNewGame(player);
    }

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
        System.out.println("Zaraz rozpocznie się Twoja przygoda. Zaczniesz 1 stycznia 2020r. \nTwoja początkowa pensja: " + players.get(0).getCash() +
                "\nPodejmuj mądre decyzje i nie zbankrutuj. Powodzenia!");
    }

    private void setStartProporties() {

        for (int i = 0; i < START_AVAILABLE_PROJECTS; i++) {
            availableProject.add(Generator.getRandomGameProject(START_DATE));
            allClient.add(Generator.getRandomClient());
        }
        Generator.randomAddProjectToClient(availableProject, allClient);

    }


    public void startDay() throws InterruptedException {
        System.out.println("\nJest " + formatDate.format(currentDay));
        checkIsTimeForReward(currentDay);
        System.out.println("Twój stan konta: " + players.get(0).getCash());

        mainAction(dayMenu.selectOptions());
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
                searchNewProject();
                break;
            case 4:
                testProject();
                break;
            case 5:
                uploadReadyProject(players.get(0));
                break;
            case 6:
                System.out.println("Opcja nie została zaimplementowana");
                break;
            case 7:
                payOfficialFees();
                break;
            default:
                gameIsOn = false;
        }
    }

    private void uploadReadyProject(Player player) throws InterruptedException {
        //sprawdzanie posiadania projektów
        if (player.myProjects.size() == 0) {
            System.out.println("Musisz POSIADAĆ Jakikolwiek projekt");
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
                    endDay();
                }
            }
        }

    }

    private void addDateAndFinishedProject(LocalDate dateReward, GameProject project) {
        dateOfPrize.add(dateReward);
        finishedProjects.add(project);
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
                if (project.deadLine.plusDays(7).isAfter(currentDay)||project.deadLine.plusDays(7).isEqual(currentDay)) {
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
                testPlayerProject(players.get(0));
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
                endDay();
            }
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

                if (canAddProject(availableProject.get(choice), players.get(0))) {
                    players.get(0).addProject(availableProject.get(choice));
                    availableProject.remove(choice);
                    System.out.println("Do końca dnia cieszysz się z podpisanej umowy i nic nie robisz!");
                    endDay();
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
        players.get(0).checkAndShowProject();
        mainAction(dayMenu.selectOptions());
    }

    private void goProgrammingPlayer() throws InterruptedException {
        if (players.get(0).hasProject() && players.get(0).allProjectAreReady()) {
            System.out.println("Wszystkie Twoje projekty są gotowe. Pomyśl nad inną opcją.");
            mainAction(dayMenu.selectOptions());

        } else if (players.get(0).hasProject()) {

            players.get(0).checkAndShowProject();

            int choice = dayMenu.selectOptions(players.get(0).myProjects.size(), "Wybierz projekt nad którym chcesz pracować:");
            if (!players.get(0).myProjects.get(choice).ready && !(players.get(0).isOnlyMobile(players.get(0).myProjects.get(choice)))) {
                players.get(0).programmingDay(players.get(0).myProjects.get(choice));
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
            GameProject anotherOne = Generator.getRandomGameProject(currentDay);
            Generator.getRandomClient().addProject(anotherOne);
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
        if (lastTermin == 2&&!(players.get(0).countFeePerMonth==2)) {
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
        } else if (players.get(0).getCash() >= (10 * Player.DEFAULT_STARTING_CASH)) {
            gameIsOn = false;
            System.out.println("Zarobiłeś 10x więcej pieniędzy niż miałeś na początku gry. WYGRYWASZ!!! GRATULUJĘ!");
        }
    }

    private void endDay() {
        checkGameDuration(currentDay.plusDays(1));
        checkNewProjeckt();
        currentDay = currentDay.plusDays(1);
    }

    private void checkIsTimeForReward(LocalDate currentDay) {
        for (int i = 0; i < dateOfPrize.size(); i++) {
            LocalDate dateNow = dateOfPrize.get(i);
            //poprawka w porównywaniu dat (wartości a nie obiekty )
            if (dateNow.isEqual(currentDay)) {
                System.out.println("Dostałeś przelew na konto w wysokości " + finishedProjects.get(i).reward + " za zakończony projekt "+finishedProjects.get(i).projectName);
                players.get(0).setCash(players.get(0).getCash() + finishedProjects.get(i).reward);
            }
        }
        //zwalnianie pamięci w zmiennych listowych
        for (int i = 0; i < dateOfPrize.size(); i++) {
            LocalDate oldDate = dateOfPrize.get(i);
            if(currentDay.isAfter(oldDate)){
                finishedProjects.remove(i);
                dateOfPrize.remove(i);
                break;
            }
        }
    }

}
