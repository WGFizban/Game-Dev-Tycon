package com.company.game.engine;

import java.util.List;

public class Player {
    static final Double DEFAULT_STARTING_CASH = 2000.00;

    public String nickName;
    private Double cash;
    public List<Employee> myEmployee;
    List<GameProject> myProjects;

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
        if (myProjects == null) System.out.println("Aktualnie nie masz żadnych projektów");
        else {
            System.out.println(myProjects);
        }
    }

    public void programmingDay() {
    }
}
