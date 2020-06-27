package com.company.game.engine;

import java.util.Scanner;

public class Menu {
    String[] menuOptions;
    Scanner input = new Scanner(System.in);

    public Menu(String[] myOptions) {
        menuOptions = myOptions;
    }

    public void showOnly() {
        for (int i = 0; i < menuOptions.length; i++) {
            String options = menuOptions[i];
            System.out.println(i + " " + options);
        }
    }

    public int selectOptions() {
        showOnly();
        System.out.println("Którą opcję wybierasz?");


        int choice = input.nextInt();
        while (choice > menuOptions.length || choice < 0) {
            System.out.println("Nie ma takiej opcji. Podaj poprawną opcje");
            choice=input.nextInt();
        }
        return choice;
    }


}
