package com.company.game.engine.npc;

public enum Occupation {
    TESTER("Tester"), DEALER("Sprzedawca"), PROGRAMMER("Programista");

    private String translateName;


    Occupation(String translateName) {
        this.translateName = translateName;
    }

    @Override
    public String toString() {
        return translateName;
    }
}
