package com.company.game.engine;

public enum ProjectComplexity {
    EASY("Łatwy"), MIDDLE("Średni"), HARD("Skomplikowany");

    private String translateName;


    ProjectComplexity(String translateName) {
        this.translateName = translateName;
    }

    @Override
    public String toString() {
        return translateName;
    }
}
