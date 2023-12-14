package de.jobstimel.bootbot.bootbot.snipes;

public class SnipesConstants {

    public static final String SHOP_NAME = "Snipes";
    public static final String BASE_URL = "https://www.snipes.com";
    public static final String QUERY_PLACEHOLDER = "$query$";
    public static final String SIZE_PLACEHOLDER = "$size$";
    public static final String GENDER_PLACEHOLDER = "$gender$";
    public static final String SEARCH_URL = BASE_URL + "/search?q=" + QUERY_PLACEHOLDER + "&srule=Standard&prefn1=size&prefv1=" + SIZE_PLACEHOLDER +
            "&prefn2=gender&prefv2=" + GENDER_PLACEHOLDER + "&openCategory=true&sz=48";
    public static final String SIZE_TYPE = "EU";
    public static final String PRODUCT_CLASS = ".b-product-tile-body ";
    public static final String PRODUCT_LINK_CLASS = ".b-product-tile-link";
    public static final String PRODUCT_BRAND_CLASS = ".b-product-tile-brand";
    public static final String PRODUCT_PRICE_CLASS = ".b-product-tile-price-item";

    private SnipesConstants() {}

}
