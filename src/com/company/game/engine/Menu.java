package com.company.game.engine;

import java.util.Scanner;

public class Menu {
    String[] menuOptions;

    public Menu(String[] myOptions) {
        menuOptions = myOptions;
    }

    public void showOnly() {
        System.out.println();
        for (int i = 0; i < menuOptions.length; i++) {
            String options = menuOptions[i];
            System.out.println(i + " " + options);
        }
    }

    public int selectOptions() {
        showOnly();
        System.out.print("Którą opcję wybierasz? Opcja: ");

        int choice = checkChoose();
        while (choice >= menuOptions.length || choice < 0) {
            System.out.print("Nie ma takiej opcji. Podaj poprawną opcje. Opcja: ");
            choice = checkChoose();
        }
        return choice;
    }

    public int selectOptions(int optionsLimit, String message) {
        System.out.print(message + " ");

        int choice = checkChoose();
        while (choice >= optionsLimit || choice < 0) {
            System.out.print("Niedozwolony numer. Podaj poprawny: ");
            choice = checkChoose();
        }
        return choice;
    }
//zapobieganie wprowadzeniu znaków innych niż potrzebna liczba
    private static int checkChoose() {
        Scanner input = new Scanner(System.in);
        int goodChoice;
        try {
            return input.nextInt();
        } catch (Exception e) {
            System.out.print("Wykryto niedozwolone znaki. Musisz podać liczbę! Opcja: ");
            goodChoice = checkChoose();
        }
        return goodChoice;
    }


}
