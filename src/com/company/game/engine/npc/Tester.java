package com.company.game.engine.npc;

import com.company.game.engine.GameProject;
import com.company.game.engine.Player;

public class Tester extends Employee {


    public Tester(String name, String surname, Double salary, Double employmentCost, Double costOfDismissal) {
        super(name, surname, salary, employmentCost, costOfDismissal);
        mainOccupation = Occupation.TESTER;
    }

    @Override
    public boolean doYourWorkForPlayer(Player player) {
        if ((countOfTesters(player) * 3) <= countOfProgrammers(player)&&player.myProjects.size()>0) {
            for (GameProject project : player.myProjects) {
                project.coderError = 0;
            }
            return true;
        }
        return false;
    }

    private int countOfProgrammers(Player player) {
        int programmersCount = 0;
        for (Employee employee : player.myEmployee) {
            if (employee.mainOccupation.toString().equals("Programista")) {
                programmersCount++;
            }
        }
        return programmersCount + 1;
    }

    private int countOfTesters(Player player) {
        int testersCount = 0;
        for (Employee employee : player.myEmployee) {
            if (employee.mainOccupation.toString().equals("Tester")) {
                testersCount++;
            }
        }
        return testersCount;
    }

}
