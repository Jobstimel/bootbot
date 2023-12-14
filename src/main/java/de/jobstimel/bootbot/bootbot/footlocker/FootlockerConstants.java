package de.jobstimel.bootbot.bootbot.footlocker;

public class FootlockerConstants {

    public static final String SHOP_NAME = "Footlocker";
    public static final String SIZE_PLACEHOLDER = "$size$";
    public static final String SIZE_TYPE = "EU";
    public static final String QUERY_PLACEHOLDER = "$query$";
    public static final String GENDER_PLACEHOLDER = "$gender$";
    public static final String BASE_URL = "https://www.footlocker.de";
    public static final String SEARCH_URL = BASE_URL + "/de/search?query=" + QUERY_PLACEHOLDER + ":relevance:gender:" + GENDER_PLACEHOLDER + ":producttype:Schuhe:size:" + SIZE_PLACEHOLDER;
    public static final String PRODUCT_CONTAINER = ".product-container";
    public static final String PRODUCT_CARD_LINK = ".ProductCard-link";
    public static final String PRODUCT_NAME_PRIMARY = ".ProductName-primary";
    public static final String PRODUCT_NAME_ALT = ".ProductName-alt";
    public static final String PRODUCT_NAME_SEPARATOR = ".ProductName-separator";
    public static final String PRODUCT_PRICE = ".ProductPrice";

    private FootlockerConstants() {}

}
