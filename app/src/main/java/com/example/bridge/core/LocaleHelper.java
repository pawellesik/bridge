package com.example.bridge.core;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

public class LocaleHelper {

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, "en");
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, "en");
    }

    public static Context setLocale(Context context, String language) {
        if (language == null || language.isEmpty()) {
            language = "en";
        }
        persist(context, language);

        try {
            LocaleListCompat appLocales = LocaleListCompat.forLanguageTags(language);
            AppCompatDelegate.setApplicationLocales(appLocales);
        } catch (Exception ignored) {}

        return updateResources(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        return SettingsManager.getInstance(context).getLanguage(defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SettingsManager.getInstance(context).setLanguage(language);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context.createConfigurationContext(configuration);
    }
}
