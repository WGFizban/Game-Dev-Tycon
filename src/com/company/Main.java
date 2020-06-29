package com.company;

import com.company.game.engine.*;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {



        Scanner input = new Scanner(System.in);
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());

        Game myGame = new Game();
        myGame.startNewGame(player1);
        //testy
        //myGame.availableProject.get(1).isReady();

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        while (myGame.gameIsOn) {
            myGame.newDay();
        }

    }
}
