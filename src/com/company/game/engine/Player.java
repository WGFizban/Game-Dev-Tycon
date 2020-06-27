package com.company.game.engine;

import java.util.ArrayList;
import java.util.List;

public class Player {
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
        else {
            System.out.println(myProjects);
        }
    }

    public void programmingDay() {
        System.out.println("Spędzasz dzień na programowaniu");
    }

    public boolean hasProject() {
        return myProjects.size() > 0;
    }

    public void addProject(GameProject project) {
        this.myProjects.add(project);
        System.out.println("Pomyślnie dodałeś projekt " + project.projectName + " do swoich zleceń");

    }

}
