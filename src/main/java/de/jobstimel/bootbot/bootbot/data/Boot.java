package de.jobstimel.bootbot.bootbot.data;

public record Boot(String name, String size, Gender gender) {

    public String buildKey() {
        return name + size + gender.name();
    }

}
