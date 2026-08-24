package com.example.bridge.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);
        return updateResources(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        try {
            return DataStoreManager.getInstance(context)
                    .getPreference(DataStoreManager.SELECTED_LANGUAGE, defaultLanguage)
                    .firstOrError()
                    .onErrorReturnItem(defaultLanguage)
                    .blockingGet();
        } catch (Exception e) {
            return defaultLanguage;
        }
    }

    private static void persist(Context context, String language) {
        DataStoreManager.getInstance(context)
                .setPreference(DataStoreManager.SELECTED_LANGUAGE, language)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .subscribe();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }
}
