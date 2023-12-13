package de.jobstimel.bootbot.bootbot.footlocker;

public record FootlockerSearchHit(String name, String altName, String description, String price, String size,
                                  String sizeType, String productUrl) {

    @Override
    public String toString() {
        return String.format("FootlockerSearchHit[name=%s, altName=%s, description=%s, price=%s, size=%s]",
                name, altName, price, description, size);
    }

}
