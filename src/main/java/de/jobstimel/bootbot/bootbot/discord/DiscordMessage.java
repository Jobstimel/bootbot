package de.jobstimel.bootbot.bootbot.discord;

public record DiscordMessage(String name, String altName, String description, String price, String size,
                             String sizeType, String productUrl, String shop) {

    public String format() {
        return String.format("""
                        **%s ist bei %s verfügbar**
                        %s %s • %s • %s • %s
                        %s""",
                name, shop, sizeType, size, altName, description, price, productUrl);
    }

}
