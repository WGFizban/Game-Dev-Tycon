package com.company.game.engine.npc;

import com.company.game.engine.*;


public class Dealer extends Employee implements SearchingProjectInterface {

    private int searchingDayes = 0;

    public Dealer(String name, String surname, Double salary, Double employmentCost, Double costOfDismissal) {
        super(name, surname, salary, employmentCost, costOfDismissal);
        mainOccupation = Occupation.DEALER;
    }

    @Override
    public boolean doYourWorkForPlayer(Player player) {
        return searchProject();
    }

    @Override
    public boolean searchProject() {
        if (searchingDayes < MIN_SEARCHING_DAYS) {
            searchingDayes++;
            System.out.println("Sprzedawca szuka klientów dla Ciebie");
            if (searchingDayes == MIN_SEARCHING_DAYS) {
                searchingDayes = 0;
                return true;
            } else return false;
        }
        return false;
    }
}
