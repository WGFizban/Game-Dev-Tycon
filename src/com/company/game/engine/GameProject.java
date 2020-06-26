package com.company.game.engine;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class GameProject {
    public String projectName;
    public ProjectComplexity complexity;
    static final String [] TECHNOLOGY = {"front-end","backend","baza danych","mobile","wordpress","prestashop"};
    Map<String,Integer> daysForTechnology = new HashMap<>();

    public boolean ready;


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

    @Override
    public String toString() {
        return "Gameproject{" +
                "Nazwa" + projectName + '\'' +
                ", złożoność" + complexity + '\'' +
                ", Czas poszczególnych tehnologi"+daysForTechnology+
                ", właściciel " + owner.firstName + " " +owner.lastName+
                ", data zakończenia " + deadLine +
                ", kara za niewykonanie" + penalty +
                ", spodziewany przychód " + reward +
                ", opóźnienie wysłania pieniedzy " + timeOfReward +
                '}';
    }
}
