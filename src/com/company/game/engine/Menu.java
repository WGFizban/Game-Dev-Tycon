package com.company.game.engine;

import java.util.Scanner;

public class Menu {
    String[] menuOptions;
    Scanner input = new Scanner(System.in);

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


        int choice = input.nextInt();
        while (choice >= menuOptions.length || choice < 0) {
            System.out.print("Nie ma takiej opcji. Podaj poprawną opcje. Opcja: ");
            choice=input.nextInt();
        }
        return choice;
    }


}
