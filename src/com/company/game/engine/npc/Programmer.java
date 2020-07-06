package com.company.game.engine.npc;

import com.company.game.engine.Player;

public class Programmer extends Employee {


    public Programmer(String name, String surname, Double salary, Double employmentCost, Double costOfDismissal) {
        super(name, surname, salary, employmentCost, costOfDismissal);
        mainOccupation = Occupation.PROGRAMMER;
    }

    @Override
    public boolean doYourWorkForPlayer(Player player) {
        return true;
    }
}
