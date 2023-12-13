package de.jobstimel.bootbot.bootbot.footlocker;

public class FootlockerConstants {

    public static final String NAME = "Footlocker";
    public static final String SIZE_PLACEHOLDER = "$size$";
    public static final String SIZE_TYPE = "EU";
    public static final String QUERY_PLACEHOLDER = "$query$";
    public static final String BASE_URL = "https://www.footlocker.de";
    public static final String SEARCH_URL = BASE_URL + "/de/search?query=" + QUERY_PLACEHOLDER + ":relevance:gender:Herren:producttype:Schuhe:size:" + SIZE_PLACEHOLDER;
    public static final String FALLBACK_VALUE = "";
    public static final String PRODUCT_CONTAINER = ".product-container";
    public static final String PRODUCT_CARD_LINK = ".ProductCard-link";
    public static final String PRODUCT_NAME_PRIMARY = ".ProductName-primary";
    public static final String PRODUCT_NAME_ALT = ".ProductName-alt";
    public static final String PRODUCT_NAME_SEPERATOR = ".ProductName-separator";
    public static final String PRODUCT_PRICE = ".ProductPrice";
    public static final String COOKIE = "datadome=KmiZ60oB0giv~0KZxb1~eBg2soZ30M1SUlljB73v2epYrrl5s_5R5hdjgP0cRXS6CmYnkm3HUL1WkmU1kZ6AmfcNy17A1bCXOYIuN_3dk5hr0gjtBjfXUDS7cLegXdZH; AMCV_40A3741F578E26BA7F000101%40AdobeOrg=179643557%7CMCIDTS%7C19703%7CMCMID%7C17751981934474946400991328297058194587%7CMCAAMLH-1702967910%7C6%7CMCAAMB-1702967910%7C6G1ynYcLPuiQxYZrsz_pkqfLG9yMXBpb2zX5dvJdYQJzPXImdj0y%7CMCOPTOUT-1702370310s%7CNONE%7CvVersion%7C5.5.0; mbox=PC#8af71be0317647a6a7867c8b54b71b90.37_0#1765610560|session#6704aaa315a6419c9c6ea2340147a2af#17023676…nvalidateUrlCache_targeting=1702365397398; _rll_c_1039_sc=0; _rll_c_1039_sd=1702365633011; BVBRANDSID=ce3e220a-3b81-47d9-b293-086532651049; mp_footlocker_de_mixpanel=%7B%22distinct_id%22%3A%20%2218c57afb22529e-00907a8513fba68-e555620-1fa400-18c57afb226551%22%2C%22bc_persist_updated%22%3A%201702278181483%2C%22event_lang%22%3A%20%22de%22%7D; _scid_r=448b1759-a6e9-471e-ac83-f12fd0f95f33; _uetsid=52bf11e097f311ee896bcb176cdf1e64; _uetvid=52bf0ad097f311eebfef41be07273b76; _gat=1; _gali=productGalleryDescription";

    private FootlockerConstants() {}

}
