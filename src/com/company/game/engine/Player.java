package com.company.game.engine;

import java.util.ArrayList;
import java.util.List;

public class Player {

    static final String[] TECHNOLOGY = {"front-end", "backend", "baza danych", "mobile", "wordpress", "prestashop"};
    static final Double DEFAULT_STARTING_CASH = 2000.00;

    public String nickName;
    private Double cash;
    public List<Employee> myEmployee = new ArrayList<>();
    public List<GameProject> myProjects = new ArrayList<>();
    public int countFeePerMonth = 0;
    public int dayForLookingClient =0;

    public Player(String nickName) {
        this.nickName = nickName;
        this.cash = DEFAULT_STARTING_CASH;
    }


    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }


    public void showProject() {
        if (myProjects.size() == 0) System.out.println("Aktualnie nie masz żadnych projektów \n");
        else showMyProjects();

    }

    public boolean allProjectsReady() {
        for (GameProject project : myProjects) {
            if (!project.ready)return false;
        }
        return true;
    }

    public void showMyProjects(){
        for (int i = 0; i < myProjects.size(); i++) {
            GameProject project = myProjects.get(i);
            System.out.println(i+" Projekt '"+project.projectName+"' o złożoności  "+project.complexity+ ". Właściciel "+project.owner.firstName+" "+project.owner.lastName+"\n"+
                    "Termin wykonania: "+project.formatDate.format(project.deadLine)+" "+" Stan gotowości: "+project.ready+"\n"+
                    "Technologie i czas "+project.daysForTechnology+"\n"+
                    "Spodziewana nagroda "+project.reward+" po "+project.timeOfReward+ " dniach roboczych od ukończenia. Kara: "+project.penalty+"\n");
        }
    }

    public void programmingDay(int choice) {
        System.out.println("Spędzasz dzień na programowaniu");
        int time;
        for (String s : TECHNOLOGY) {
            if (myProjects.get(choice).daysForTechnology.get(s) > 0) {
                time = myProjects.get(choice).daysForTechnology.get(s);
                myProjects.get(choice).daysForTechnology.replace(s, time - 1);
                break;
            }

        }
        myProjects.get(choice).isReady();
    }

    public boolean hasProject() {
        return myProjects.size() > 0;
    }

    public void addProject(GameProject project) {
        this.myProjects.add(project);
        System.out.println("Pomyślnie dodałeś projekt " + project.projectName + " do swoich zleceń");

    }

}
