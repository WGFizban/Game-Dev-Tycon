package com.company;

import com.company.game.engine.*;
import com.company.game.engine.npc.*;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

   /*     Double mycost = 100.00;
        Player player1 = new Player("koko");
        Tester tester1 = new Tester("Franek", "Testujący", mycost, mycost, mycost);
        Tester tester2 = new Tester("Jenusz", "Prprogramowany", mycost, mycost, mycost);
        Programmer programista1 = new Programmer("Janusz", "Programista", mycost, mycost, mycost);
        Dealer sprzedawca = new Dealer("sprzedawca", "s1", mycost, mycost, mycost);

        System.out.println(player1.getCash() + "Obecna pensja");
        tester1.hire(player1);
        programista1.hire(player1);
        programista1.hire(player1);
        programista1.hire(player1);
        programista1.hire(player1);
        System.out.println("po zatrudnieniach " + player1.getCash() + "\n");

        System.out.println("Moi pracownicy \n" + player1.myEmployee);

        for (int i = 0; i <= 10; i++) {
            System.out.println(sprzedawca.doYourWorkForPlayer(player1));
        }*/




        Scanner input = new Scanner(System.in);
        System.out.println("Podaj Nick dla Twojego gracza");
        Player player1 = new Player(input.nextLine());

        Game myGame = new Game(player1);

        while (myGame.gameIsOn) {
            myGame.startDay();
        }
    }
}
