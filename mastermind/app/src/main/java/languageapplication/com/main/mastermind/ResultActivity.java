package languageapplication.com.main.mastermind;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import languageapplication.com.main.mastermind.databinding.ResultLayoutBinding;
import languageapplication.com.main.mastermind.models.SharePreference;

public class ResultActivity extends AppCompatActivity {

    private ResultLayoutBinding resultLayoutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("life", "onCreate: result");

        //set chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        resultLayoutBinding = ResultLayoutBinding.inflate(getLayoutInflater());
        setContentView(resultLayoutBinding.getRoot());

        Intent intent = getIntent();
        int corrects = intent.getIntExtra("correct", 0);
        int total = intent.getIntExtra("total", 0);

        //tính điểm và set dữ liệu
        int point = corrects*100/total;
        String status = "Correct: " + corrects +"/" + total;

        //lưu lại điểm
        SharePreference.setRecentPoint(this, point);

        resultLayoutBinding.txtPoint.setText(point+"");
        resultLayoutBinding.txtStatus.setText(status);

        //nhấn nút bntBack để quay về ViewFolder
        resultLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, ViewFolderActivity.class);

                //save point into file

                startActivity(intent);
                finish();
            }
        });

        //nhấn nút back của hệ thống
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                resultLayoutBinding.btnBack.callOnClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: Result");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: Result");
    }
}