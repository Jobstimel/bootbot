package de.jobstimel.bootbot.bootbot.data;

public enum Gender {

    HERREN("Herren"),
    DAMEN("Damen"),
    KINDER("Kinder");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
