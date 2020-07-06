package com.company.game.engine.npc;

import com.company.game.engine.Player;

public abstract class Employee {
    String name;
    String surname;
    Double salary;
    Double employmentCost;
    Double costOfDismissal;
    public Occupation mainOccupation;

    public Employee(String name, String surname, Double salary, Double employmentCost, Double costOfDismissal) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.employmentCost = employmentCost;
        this.costOfDismissal = costOfDismissal;
    }

    public void hire(Player player) {
        if (player.getCash() < employmentCost) System.out.println("Masz za mało pieniędzy by zatrudnić tą osobe!");
        else {
            System.out.println(this.mainOccupation + " " + this.name + " " + this.surname + " został pomyślnie zatrudniony.");
            player.myEmployee.add(this);
            player.setCash(player.getCash() - employmentCost);
        }
    }

    public void dismiss(Player player) {
        System.out.println("Zwolniłeś pracownika " + this.name + " " + this.surname + " pracującego jako " + this.mainOccupation);
        player.myEmployee.remove(this);
        player.setCash(player.getCash() - costOfDismissal);
    }

    public void getSalaryFromPlayer(Player player) {
        if (player.getCash() < salary) {
            System.out.println("Nie masz wystarczającej ilości gotówki by zapłacić pracownikowi " + this.name + " " + this.surname + " Twój " + this.mainOccupation + " odchodzi z Twojej firmy :( ");
            player.myEmployee.remove(this);
        } else {
            System.out.println("Wypłacono pensje miesięczna dla pracownika " + this.mainOccupation + " " + this.name + " " + this.surname);
            player.setCash(player.getCash() - salary);
        }
    }

    abstract public boolean doYourWorkForPlayer(Player player);

    @Override
    public String toString() {
        return this.mainOccupation.toString() + " " + this.name + " " + this.surname + "\n";
    }
}
