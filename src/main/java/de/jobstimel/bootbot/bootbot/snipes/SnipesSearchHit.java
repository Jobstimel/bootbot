package de.jobstimel.bootbot.bootbot.snipes;

public record SnipesSearchHit(String name, String altName, String description, String price, String size,
                              String sizeType, String productLink) {

    @Override
    public String toString() {
        return String.format("SnipesSearchHit[name=%s, altName=%s, description=%s, price=%s, size=%s]",
                name, altName, price, description, size);
    }

}
