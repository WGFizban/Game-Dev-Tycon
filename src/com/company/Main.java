package com.company;

import com.company.game.engine.*;
import com.company.game.engine.npc.*;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {


 /*      // część testowa
        Player player1 = new Player("Gracz testowy");

        player1.myEmployee.add(Generator.getRandomEmployee(Occupation.PROGRAMMER));
        player1.myEmployee.add(Generator.getRandomEmployee(Occupation.PROGRAMMER));
        player1.myEmployee.add(Generator.getRandomEmployee(Occupation.TESTER));
        player1.myEmployee.add(Generator.getRandomEmployee(Occupation.DEALER));*/

        //właściwa gra
        Scanner input = new Scanner(System.in);
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());
        Game myGame = new Game(player1);

        while (myGame.gameIsOn) {
            myGame.startDay(player1);
        }
    }
}
