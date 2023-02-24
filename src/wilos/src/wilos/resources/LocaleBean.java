package wilos.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import wilos.presentation.web.utils.WebSessionService;

import com.icesoft.faces.context.effects.JavascriptContext;

public class LocaleBean {

   /* private static final String BASENAME = "wilos.resources.messages";

    static private Locale LOCALE;

    private String language;

    private HashMap<String, Locale> locales;

    public LocaleBean() {
	locales = new HashMap<String, Locale>();
	locales.put("EN", new Locale("en", "US"));
	locales.put("FR", new Locale("fr", "FR"));
	initLocale();
    }

    static public String get(String key) {
	return getBundle().getString(key);
    }

    public static ResourceBundle getBundle() {
	return ResourceBundle.getBundle(BASENAME, Locale.getDefault());
    }

    *//**
     * function getText
     * 
     * @param key
     * @return String : the key in the properties file
     *//*
    static public String getText(String key) {
	try {
	    return getBundle().getString(key);
	} catch (MissingResourceException e) {
	    return key;
	}
    }

    *//**
     * function getChar
     * 
     * @param key
     * @return char: the mnemonic characters
     *//*
    public char getChar(String key) {
	return LocaleBean.getText(key).charAt(0);
    }

    *//**
     * Change the locale
     * 
     * @param _actionEvent
     *//*
    public void chooseLocale(ActionEvent _evt) {
	String id = _evt.getComponent().getId();
	FacesContext.getCurrentInstance().getApplication().setDefaultLocale(
		(Locale) locales.get(id));

	LocaleBean.LOCALE = FacesContext.getCurrentInstance().getApplication()
		.getDefaultLocale();
    }

    *//**
     * Return the current locale
     * 
     * @return the locale
     *//*
    static public Locale getLOCALE() {
	initLocale();
	return LocaleBean.LOCALE;
    }

    *//**
     * Initialize the locale
     *//*
    private static void initLocale() {
	if (LocaleBean.LOCALE == null) {
	    FacesContext.getCurrentInstance().getApplication()
		    .setDefaultLocale(Locale.getDefault());
	}
	LocaleBean.LOCALE = FacesContext.getCurrentInstance().getApplication()
		.getDefaultLocale();
	
	LocaleBean.LOCALE = Locale.getDefault();
    }

    public String getLanguage() {
	language = LocaleBean.getLOCALE().getLanguage();
	return language;
    }*/
    
    static private final String BASENAME = "wilos/resources/messages";

	static private ResourceBundle bundle = ResourceBundle.getBundle(LocaleBean.BASENAME, Locale.getDefault());

	public LocaleBean() {
	    //LocaleBean.setCurrentLocale(FacesContext.getCurrentInstance().getViewRoot().getLocale());
	}
	/**
	 * procedure setCurrentLocale
	 * 
	 * @param locale
	 */
	static public void setCurrentLocale(Locale locale) {
		if (locale != null) {
			try {
				Locale.setDefault(locale);
				bundle = ResourceBundle.getBundle(LocaleBean.BASENAME, locale);
			} catch (Exception e) {
				Locale.setDefault(Locale.ENGLISH);
				bundle = ResourceBundle.getBundle(LocaleBean.BASENAME,
						Locale.ENGLISH);
			}
		}
	}

	/**
	 * function getText
	 * 
	 * @param key
	 * @return String : the key in the properties file
	 */
	static public String getText(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * function getChar
	 * 
	 * @param key
	 * @return char: the mnemonic characters
	 */
	static public char getChar(String key) {
		return LocaleBean.getText(key).charAt(0);
	}

}
