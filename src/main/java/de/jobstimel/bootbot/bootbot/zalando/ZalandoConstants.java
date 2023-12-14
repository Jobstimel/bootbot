package de.jobstimel.bootbot.bootbot.zalando;

public class ZalandoConstants {

    public static final String BASE_URL = "https://www.zalando.de";
    public static final String SHOP_NAME = "Zalando";
    public static final String SIZE_PLACEHOLDER = "$size$";
    public static final String SIZE_TYPE = "EU";
    public static final String SIZE_DELIMITER = "~";
    public static final String QUERY_PLACEHOLDER = "$query$";
    public static final String QUERY_DELIMITER = "+";
    public static final String SEARCH_URL = BASE_URL + "/katalog/__groesse-" + SIZE_PLACEHOLDER + "/?q=" + QUERY_PLACEHOLDER;
    public static final String PRODUCT_CLASSES = "._0xLoFW._78xIQ-.EJ4MLB.f4ql6o.JT3_zV";
    public static final String INFORMATION_DELIMITER = "-";
    public static final String INFORMATION_CLASS = ".Zhr-fS";

    private ZalandoConstants() {}

}
