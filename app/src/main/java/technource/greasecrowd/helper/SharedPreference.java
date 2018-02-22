package technource.greasecrowd.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import technource.greasecrowd.model.SafetyReport_DBO;

/**
 * Created by technource on 14/2/18.
 */

public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<SafetyReport_DBO> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, SafetyReport_DBO product) {
        List<SafetyReport_DBO> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<SafetyReport_DBO>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, SafetyReport_DBO product) {
        ArrayList<SafetyReport_DBO> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<SafetyReport_DBO> getFavorites(Context context) {
        SharedPreferences settings;
        List<SafetyReport_DBO> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            SafetyReport_DBO[] favoriteItems = gson.fromJson(jsonFavorites,
                    SafetyReport_DBO[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<SafetyReport_DBO>(favorites);
        } else
            return null;

        return (ArrayList<SafetyReport_DBO>) favorites;
    }
}
