package com.company.game.engine;

import com.company.game.engine.npc.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Player implements ProgrammingInterface,SearchingProjectInterface {

    static final String[] TECHNOLOGY = {"front-end", "backend", "baza danych", "mobile", "wordpress", "prestashop"};
    public static final Double DEFAULT_STARTING_CASH = 2000.00;

    public String nickName;
    private Double cash;
    public List<Employee> myEmployee = new ArrayList<>();



    public List<GameProject> myProjects = new ArrayList<>();
    public int countFeePerMonth = 0;

    public int getDayForLookingClient() {
        return dayForLookingClient;
    }

    private int dayForLookingClient = 0;

    public Player(String nickName) {
        this.nickName = nickName;
        this.cash = DEFAULT_STARTING_CASH;
    }


    public Double getCash() {
        //dodanie precyzji do pola cash pomocne przy pracy z procentami
        return BigDecimal.valueOf(cash).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }


    public void checkAndShowProject() {
        if (myProjects.size() == 0) System.out.println("Aktualnie nie masz żadnych projektów \n");
        else showMyProjects();

    }

    public boolean anyoneProjectIsReady() {
        boolean isReady = true;
        for (GameProject project : myProjects) {
            if (project.ready) {
                break;
            } else {
                isReady = false;
                break;
            }
        }
        return isReady;
    }

    public boolean allProjectAreReady() {
        boolean isReady = true;
        for (GameProject project : myProjects) {
            if (!project.ready) {
                isReady = false;
                break;
            }
        }
        return isReady;
    }

    public boolean hasEmployee(Occupation occupation) {
        if (myEmployee.size() > 0) {
            for (Employee worker : myEmployee) {
                if (worker.mainOccupation.equals(occupation)) return true;
            }
        }
        return false;
    }

    public void showMyProjects() {
        System.out.println();
        for (int i = 0; i < myProjects.size(); i++) {
            GameProject project = myProjects.get(i);
            System.out.println(i + " Projekt '" + project.projectName + "' o złożoności  " + project.complexity + ". Właściciel " + project.owner.firstName + " " + project.owner.lastName + "\n" +
                    "Termin wykonania: " + project.formatDate.format(project.deadLine) + " " + " Stan gotowości: " + project.ready + "\n" +
                    "Technologie i czas " + project.daysForTechnology + "\n" +
                    "Spodziewana nagroda " + project.reward + " po " + project.timeOfReward + " dniach roboczych od ukończenia. Kara: " + project.penalty + "\n" +
                    "Błędy w kodzie: " + project.coderError + "\n");
        }
    }

    public boolean isOnlyMobile(GameProject project) {
        int mobile = 0;
        int otherTech = 0;
        for (String technology : TECHNOLOGY) {
            if (!technology.equals("mobile")) {
                if (otherTech < project.daysForTechnology.get(technology))
                    otherTech = project.daysForTechnology.get(technology);
            } else {
                if (mobile < project.daysForTechnology.get("mobile")) mobile = project.daysForTechnology.get("mobile");
            }
        }
        return otherTech == 0 && !(mobile == 0);
    }


    public boolean hasProject() {
        return myProjects.size() > 0;
    }

    public void addProject(GameProject project) {
        this.myProjects.add(project);
        System.out.println("\nPomyślnie dodałeś projekt " + project.projectName + " do swoich zleceń");
    }

    public boolean areProjectsToTest() {
        int isTested = 0;
        if (myProjects.size() > 0) {
            for (GameProject project : myProjects) {
                if (project.coderError == 0) isTested = 1;
                else {
                    isTested = 0;
                    break;
                }
            }
        }
        return isTested == 1;
    }

    @Override
    public void programmingDay(GameProject selectedProject) {
        System.out.println("Spędzasz dzień na programowaniu");
        int time;
        for (String s : TECHNOLOGY) {
            if (selectedProject.daysForTechnology.get(s) > 0) {
                time = selectedProject.daysForTechnology.get(s);
                //gracz nie umie prgramować mobilnie
                if (!s.equals("mobile")) {
                    selectedProject.daysForTechnology.replace(s, time - 1);
                    //gracz ma 10% szans na wygenerowanie błedu w projekcie - błedy się sumują i mają wplyw przy oddaniu projektu
                    if (Generator.checkPercentegesChance(10)) selectedProject.coderError++;
                    break;
                }
            }
        }
        selectedProject.isReady();
    }

    @Override
    public boolean searchProject() {
        if (dayForLookingClient < MIN_SEARCHING_DAYS) {
            dayForLookingClient++;
            if (dayForLookingClient == MIN_SEARCHING_DAYS) {
                dayForLookingClient = 0;
                return true;
            } else return false;
        }
        return false;
    }
}
