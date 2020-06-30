package com.company;

import com.company.game.engine.*;


import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {


        Scanner input = new Scanner(System.in);
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());

        GameProject test = Generator.getRandomGameProject(LocalDate.now());
        test.ready=true;
        test.owner=Generator.getRandomClient();
        player1.addProject(test);

        Game myGame = new Game(player1);

        while (myGame.gameIsOn) {
            myGame.newDay();
        }

    }
}
