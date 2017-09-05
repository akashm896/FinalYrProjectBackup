package exceptions;

/**
 * Created by K. Venkatesh Emani on 4/30/2017.
 */
public class HQLTranslationException extends Exception {
    public HQLTranslationException(String message) {
        super("HQLTranslationException: " + message);
    }
}
