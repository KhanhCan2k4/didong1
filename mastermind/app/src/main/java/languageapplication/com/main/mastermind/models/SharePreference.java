package languageapplication.com.main.mastermind.models;

import android.content.Context;
import android.content.SharedPreferences;

import languageapplication.com.main.mastermind.R;

public class SharePreference {
    //tham số keys
    private static final String PREFERENCE_THEME = "Preference_theme";
    private static final String THEME_KEY = "theme_key";
    private static final String RECENT_POINT = "recent_point";

    /**
     * Lưu lại theme của app
     * @param context
     * @param theme
     */
    public static void saveTheme(Context context, int theme){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(THEME_KEY, theme);
        editor.apply();
    }

    /**
     * Lấy ra chủ đề hiện tại của ứng dụng
     * @param context
     * @return
     */
    public static int getSaveTheme(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_THEME, context.MODE_PRIVATE);
        return preferences.getInt(THEME_KEY, R.style.AppTheme_Navy);
    }

    /**
     * Lưu lại điểm gần nhất
     * @param context
     * @return
     */
    public static int getRecentPoint(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(RECENT_POINT, context.MODE_PRIVATE);
        return preferences.getInt(RECENT_POINT, 0);
    }

    /**
     * Lấy ra điểm gần nhất
     * @param context
     * @param point
     */
    public static void setRecentPoint(Context context, int point) {
        SharedPreferences preferences = context.getSharedPreferences(RECENT_POINT, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(RECENT_POINT, point);
        editor.apply();
    }
}
