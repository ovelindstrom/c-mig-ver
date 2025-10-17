package se.csn.notmotor.admin.faces;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Taget fran Core JavaServer Faces, Geary och Horstmann, s. 232  
 */
public class Messages {

    public static final String BUNDLENAME = "se.csn.notmotor.admin.resources.ApplicationResources";

    public static FacesMessage getMessage(String messageKey, Object[] params) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        String bundleName = app.getMessageBundle();
        if (bundleName == null) {
            bundleName = BUNDLENAME;
        }

        Locale locale = context.getViewRoot().getLocale();
        if (locale == null) {
            locale = Locale.getDefault();
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, loader);
        String summary = getString(bundle, messageKey, params);
        String detail = getString(bundle, messageKey + "_detail", params);
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    public static String getString(ResourceBundle bundle, String messageKey, Object[] params) {
        if (bundle == null) {
            return "NO BUNDLE";
        }
        String msg = bundle.getString(messageKey);
        if (msg == null) {
            return "";
        }
        return new MessageFormat(msg).format(params);
    }

    private Messages() {
    }

}
