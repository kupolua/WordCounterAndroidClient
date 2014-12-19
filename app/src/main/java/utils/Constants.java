package utils;


public class Constants {

    public static final int PORT = 8008;

    public static final String PARAM_TEXT_COUNT = "textCount";
    public static final String PARAM_SORTING_ORDER = "sortingOrder";
    public static final String KEY_ASCENDING = "KEY_ASCENDING";
    public static final String KEY_DESCENDING = "KEY_DESCENDING";
    public static final String VALUE_ASCENDING = "VALUE_ASCENDING";
    public static final String VALUE_DESCENDING = "VALUE_DESCENDING";

    public static final String PARAM_IS_FILTER_WORDS = "isFilterWords";
    public static final String FILTER_ON = "true";
    public static final String FILTER_OFF = "false";

    public static final String PARAM_LANGUAGE = "Accept-Language";

    public static final String SERVER_NAME = "http://95.158.60.148:";
    public static final String CONTEXT = "/WordCounter/";
    public static final String COUNT_REQUEST = "countWordsWithParams";
    public static final String COUNT_URL = SERVER_NAME + PORT + CONTEXT + COUNT_REQUEST;
}
