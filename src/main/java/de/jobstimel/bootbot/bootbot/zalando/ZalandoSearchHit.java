package de.jobstimel.bootbot.bootbot.zalando;

public record ZalandoSearchHit(String name, String altName, String description, String price, String size,
        String sizeType, String productLink) {

    @Override
    public String toString() {
        return String.format("ZalandoSearchHit[name=%s, altName=%s, description=%s, price=%s, size=%s]",
                name, altName, price, description, size);
    }

}
