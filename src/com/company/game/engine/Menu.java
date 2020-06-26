package com.company.game.engine;

import java.util.Scanner;

public class Menu {
    String[] menuOptions;
    Scanner input = new Scanner(System.in);

    public Menu(String[] myOptions) {
        menuOptions = myOptions;
    }

    public void showOnly() {
        System.out.println("Twoje opcje do wyboru są następujące: \n");
        for (int i = 0; i < menuOptions.length; i++) {
            String options = menuOptions[i];
            System.out.println(i + " " + options);
        }
    }

    public int selectOptions() {
        showOnly();
        System.out.println("Co wybierasz?");

//        testInput:
//        while (input.nextInt() > menuOptions.length || input.nextInt() < 0) {
//            System.out.println("Nie ma takiej opcji. Podaj poprawną opcje");
//            continue testInput;
//        }
        return input.nextInt();
    }


}
