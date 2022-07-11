package eu.apenet.commons.utils;

import org.apache.commons.lang.StringEscapeUtils;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.persistence.vo.Country;

public final class DisplayUtils {

    private static final String OR = "|";
    private static final int ZERO = 0;
    private static final int MIN_SIZE = 10;
    private static final String EM_END_REPLACED = "&lt;/em&gt;";
    private static final String EM_START_REPLACED = "&lt;em&gt;";
    public static final String EM_END = "</em>";
    public static final String EM_START = "<em>";
    private static final int EM_END_LENGTH = EM_END.length();
    private static final int EM_START_LENGTH = EM_START.length();
    private static final String DOTS = "...";
    private static final int DOTS_LENGTH = DOTS.length();

    public static String encodeHtmlWithHighlighting(String source, int max) {
        if (source != null) {
            String result = "";
            String[] splitted = source.split(EM_START + OR + EM_END);
            //boolean emStart = true;
            boolean emOpen = false;
            boolean exceedLimit = false;
            int length = 0;
            int i = 0;
            for (; !exceedLimit && i < splitted.length; i++) {
                if (i == 0 && splitted[i].length() == 0) {
                    //result = EM_START;
                    //emStart = false;
                } else {
                    if (i > 0) {
                        if (!emOpen) {
                            result += EM_START;
                            emOpen = true;
                        } else {
                            result += EM_END;
                            emOpen = false;
                        }
                    }
                    result += splitted[i];
                    length += splitted[i].length();
                    if (((length + DOTS_LENGTH) > max) && (max - DOTS_LENGTH > 0)) {
                        int min = (length + DOTS_LENGTH - max);
                        int lengthWithHighlighting = result.length();
                        int cutIndex = lengthWithHighlighting - min;
                        int lastEmStartIndex = result.lastIndexOf(EM_START);
                        int lastEmEndIndex = result.lastIndexOf(EM_END);
                        if (lastEmStartIndex > -1 && lastEmStartIndex + EM_START_LENGTH >= cutIndex) {
                            result = result.substring(0, lastEmStartIndex);
                            result += DOTS;
                        } else if (lastEmEndIndex > -1 && lastEmEndIndex + EM_END_LENGTH >= cutIndex) {
                            result = result.substring(0, lastEmEndIndex);
                            result += DOTS + EM_END;
                            emOpen = false;
                        } else {
                            result = result.substring(0, cutIndex);
                            result += DOTS;
                            if (emOpen) {
                                result += EM_END;
                                emOpen = false;
                            }
                        }

                        exceedLimit = true;
                    }
                }
            }
            if (i > 0 && !exceedLimit && emOpen) {
                result += EM_END;
            }
            result = StringEscapeUtils.escapeHtml(result);
            result = result.replaceAll(EM_START_REPLACED, EM_START).replaceAll(EM_END_REPLACED, EM_END);
            return result;
        } else {
            return source;
        }
    }

    public static String encodeHtmlWithHighlighting(String source) {
        if (source != null) {
            return StringEscapeUtils.escapeHtml(source).replaceAll(EM_START_REPLACED, EM_START).replaceAll(EM_END_REPLACED, EM_END);
        }
        return null;
    }

    public static String substring(String string, int max) {
        if (string != null && (string.length() > max) && (max - DOTS_LENGTH > MIN_SIZE)) {
            return string.substring(ZERO, max - DOTS_LENGTH) + DOTS;
        }
        return string;
    }

    public static String encodeHtml(String string, int max) {
        if (string != null && (string.length() > max) && (max - DOTS_LENGTH > MIN_SIZE)) {
            String result = string.substring(ZERO, max - DOTS_LENGTH) + DOTS;
            return StringEscapeUtils.escapeHtml(result);
        } else {
            return StringEscapeUtils.escapeHtml(string);
        }
    }

    public static String encodeHtml(String string) {
        return StringEscapeUtils.escapeHtml(string);

    }

    public static String escapeJavascript(String string) {
        if (string != null) {
            return StringEscapeUtils.escapeHtml(string).replaceAll("'", "\\\\'");
        }
        return null;
    }

    public static String getLocalizedCountryName(ResourceBundleSource resourceBundleSource, Country country) {
        return getLocalizedCountryName(resourceBundleSource, country.getCname());
    }

    public static String getLocalizedCountryName(ResourceBundleSource resourceBundleSource, String countryName) {
        String lowerCaseCountryName = countryName.toLowerCase();
        String key = "country." + lowerCaseCountryName.replaceAll(" ", "_");
        String defaultValue = lowerCaseCountryName.replaceAll("_", " ");
        return resourceBundleSource.getString(key, defaultValue);
    }

    public static String getLocalizedCountryNameFixed(ResourceBundleSource resourceBundleSource, Country country) {
        String lowerCaseCountryName = country.getCname().toLowerCase();
        String key = "country." + lowerCaseCountryName.replaceAll(" ", "_");
        String defaultValue = key;//lowerCaseCountryName.replaceAll("_", " ");
        return resourceBundleSource.getString(key, defaultValue);
    }
}
