package com.company;

import com.company.game.engine.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());

        Game myGame = new Game(player1);

        while (myGame.gameIsOn) {
            myGame.startDay();
        }
    }
}
