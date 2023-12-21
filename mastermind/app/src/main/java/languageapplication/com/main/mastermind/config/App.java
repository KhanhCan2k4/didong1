package languageapplication.com.main.mastermind.config;

import android.app.Application;
import android.content.Context;

/**
 * lấy context của app
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
