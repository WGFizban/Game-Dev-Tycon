package com.company;

import com.company.game.engine.*;
import com.company.game.engine.ClientCharacter;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {


        Game myGame = new Game();
        myGame.startNewGame();
        System.out.println("Witaj w Game dev Tycon " + myGame.players.get(0).nickName);
        System.out.println("Zaraz rozpocznie się Twoja przygoda. Zaczniesz 1 stycznia 2020r. Twoja początkowa pensja: " + myGame.players.get(0).getCash() + "\n" +
                "Podejmuj mądre decyzje i nie zbankrutuj. Powodzenia!");
        //test dla gracza
        //myGame.players.get(0).addProject(myGame.availableProject.get(1));


        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (myGame.gameIsOn) {
            myGame.checkCash();
            myGame.newDay();
        }


//        LocalDate startDate = LocalDate.of(2020, 1, 1);
//
//        DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("E dd.MM.yyyy");
//
//
//        for (int i = 0; i < 12; i++) {
//            if (startDate.getDayOfWeek().toString().equals("SUNDAY") || startDate.getDayOfWeek().toString().equals("SATURDAY")) {
//                System.out.println("masz dzień wolny " + myDateFormat.format(startDate));
//            } else System.out.println(myDateFormat.format(startDate));
//            startDate = startDate.plusDays(1);
//        }


    }


}
