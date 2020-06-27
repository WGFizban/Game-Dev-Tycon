package com.company.game.engine;

import java.util.ArrayList;
import java.util.List;

public class Client {
    String firstName;
    String lastName;
    ClientCharacter character;


    public List<GameProject> myProjects = new ArrayList<>();


    public Client(String firstName, String lastName, ClientCharacter character) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.character = character;
    }

    public void addProject(GameProject project) {

        if (project != null) project.setOwner(this);
        this.myProjects.add(project);
    }

    @Override
    public String toString() {
        return "Klient {" +
                "imię='" + firstName + '\'' +
                ", nazwisko='" + lastName + '\'' +
                '}';
    }
}
