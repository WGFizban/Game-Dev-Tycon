package com.company.game.engine;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameProject {
    public String projectName;
    public ProjectComplexity complexity;
    static final String [] TECHNOLOGY = {"front-end","backend","baza danych","mobile","wordpress","prestashop"};
    Map<String,Integer> daysForTechnology = new LinkedHashMap<>();

    public boolean ready;
    DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("E dd.MM.yyyy");


    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Client owner;
    public LocalDate deadLine;
    public Double penalty;
    public Double reward;
    public int timeOfReward;

    public GameProject(String projectName, ProjectComplexity complexity, LocalDate deadLine, Double penalty, Double reward, int timeOfReward,Integer[]techDuration) {
        this.projectName = projectName;
        this.complexity = complexity;
        this.deadLine = deadLine;
        this.penalty = penalty;
        this.reward = reward;
        this.timeOfReward = timeOfReward;
        for (int i = 0; i < TECHNOLOGY.length; i++) {
            daysForTechnology.put(TECHNOLOGY[i],techDuration[i]);
        }
    }

    public void isReady() {
        for (String s : TECHNOLOGY) {
            if (daysForTechnology.get(s) > 0) {
                ready = false;
                break;
            } else ready = true;
        }

    }

    @Override
    public String toString() {
        return "Projekt "+projectName+" o złożoności  "+complexity+ ". Właściciel "+owner.firstName+" "+owner.lastName+"\n"+
                "Termin wykonania: "+formatDate.format(deadLine)+" "+" Stan gotowości: "+ready+"\n"+
                "Technologie i czas "+daysForTechnology+"\n"+
                "Spodziewana nagroda "+reward+" po "+timeOfReward+ " dniach roboczych od ukończenia. Kara: "+penalty+"\n";
    }
}
