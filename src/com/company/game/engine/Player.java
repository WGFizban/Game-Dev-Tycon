package com.company.game.engine;

import java.util.ArrayList;
import java.util.List;

public class Player {

    static final String[] TECHNOLOGY = {"front-end", "backend", "baza danych", "mobile", "wordpress", "prestashop"};
    static final Double DEFAULT_STARTING_CASH = 2000.00;

    public String nickName;
    private Double cash;
    public List<Employee> myEmployee = new ArrayList<>();
    public List<GameProject> myProjects = new ArrayList<>();

    public Player(String nickName) {
        this.nickName = nickName;
        this.cash = DEFAULT_STARTING_CASH;
    }


    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }


    public void showProject() {
        if (myProjects.size() == 0) System.out.println("Aktualnie nie masz żadnych projektów \n");
        else System.out.println(myProjects);

    }

    public boolean allProjectsReady() {
        for (GameProject project : myProjects) {
            if (project.ready)return true;
        }
        return false;
    }

    public void programmingDay() {
        System.out.println("Spędzasz dzień na programowaniu");
        int time;
        for (String s : TECHNOLOGY) {
            if (myProjects.get(0).daysForTechnology.get(s) > 0) {
                time = myProjects.get(0).daysForTechnology.get(s);
                myProjects.get(0).daysForTechnology.replace(s, time - 1);
                break;
            }

        }
        myProjects.get(0).isReady();
    }

    public boolean hasProject() {
        return myProjects.size() > 0;
    }

    public void addProject(GameProject project) {
        this.myProjects.add(project);
        System.out.println("Pomyślnie dodałeś projekt " + project.projectName + " do swoich zleceń");

    }

}
