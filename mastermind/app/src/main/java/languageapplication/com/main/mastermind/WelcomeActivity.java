package languageapplication.com.main.mastermind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.database.DatabaseHelper;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.WelcomeLayoutBinding;
import languageapplication.com.main.mastermind.models.SharePreference;

public class WelcomeActivity extends AppCompatActivity {

    private WelcomeLayoutBinding welcomeLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Life", "onCreate: Welcome");

        welcomeLayoutBinding = WelcomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(welcomeLayoutBinding.getRoot());

        //đặt chủ đề
        setTheme(SharePreference.getSaveTheme(this));
        Constains.setLogo(this, welcomeLayoutBinding.logo);

        DatabaseManager manager = new DatabaseManager(this);
        manager.open();

        //Chạy COUNT_DOWN_TIME để chờ chương trình
        CountDownTimer countDownTimer = new CountDownTimer(Constains.COUNT_DOWN_TIME, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: Welcome");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: Welcome");
    }
}