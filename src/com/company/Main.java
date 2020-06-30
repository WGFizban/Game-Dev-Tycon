package com.company;

import com.company.game.engine.*;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

/*        Generator gen = new Generator();
        for (int i = 0; i < 100; i++) {
            System.out.println(gen.checkPercentegesChance(0));
        }*/

        Scanner input = new Scanner(System.in);
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());

        Game myGame = new Game(player1);

        while (myGame.gameIsOn) {
            myGame.newDay();
        }

    }
}
