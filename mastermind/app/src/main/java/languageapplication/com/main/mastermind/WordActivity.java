package languageapplication.com.main.mastermind;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.FalseLayoutBinding;
import languageapplication.com.main.mastermind.databinding.WordLayoutBinding;
import languageapplication.com.main.mastermind.models.SharePreference;
import languageapplication.com.main.mastermind.models.Word;

public class WordActivity extends AppCompatActivity {

    private WordLayoutBinding wordLayoutBinding;
    private FalseLayoutBinding falseLayoutBinding;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("life", "onCreate: word");

        //set chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        DatabaseManager manager = new DatabaseManager(this);
        manager.open();

        Intent intent = getIntent();

        String key = intent.getStringExtra("key");
        int id = intent.getIntExtra("id", 0);
        Word word = manager.getWordById(id);

        if(id == 0) {
            falseLayoutBinding = FalseLayoutBinding.inflate(getLayoutInflater());
            setContentView(falseLayoutBinding.getRoot());

            //nhấn nút back để trở lại màn hình search
            falseLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WordActivity.this, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(intent);
                    finish();
                }
            });
        } else {
            wordLayoutBinding = WordLayoutBinding.inflate(getLayoutInflater());
            setContentView(wordLayoutBinding.getRoot());

            //gắn dữ liệu lên layout
            wordLayoutBinding.txtSearch.setText(key);

            //kiểm tra đây có là từ đã lưu yêu thích hay không
            if(manager.isFavourite(id)) {
                wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_24);
                wordLayoutBinding.btnChooseFav.setTag("1");
            }

            //chọn lưu từ yêu thích
            wordLayoutBinding.btnChooseFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //chọn
                    if (wordLayoutBinding.btnChooseFav.getTag().equals("0")) {
                        wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_24);
                        wordLayoutBinding.btnChooseFav.setTag("1");

                        //lưu trên database
                        manager.setFavourite(word.getId());

                    } else {
                        //bỏ chọn
                        wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_outline_24);
                        wordLayoutBinding.btnChooseFav.setTag("0");

                        //database
                        manager.deleteFavourite(word.getId());
                    }
                }
            });

            //hiển thị dữ liệu
            wordLayoutBinding.txtWord.setText(word.getWord());
            wordLayoutBinding.txtFurigana.setText(word.getFurigana());
            wordLayoutBinding.txtRomaji.setText( word.getRomaji());
            wordLayoutBinding.txtMeaning.setText( word.getMeaning());
            wordLayoutBinding.txtLevel.setText("N" + word.getLevel());

            //bỏ hiển thị 2 bút di chuyển giữa các từ
            wordLayoutBinding.Buttons.setVisibility(View.GONE);

            //nhấn nút btnBack để trở lại mần hình Search
            wordLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WordActivity.this, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    Log.d("TAG", "onClick: back");

                    startActivity(intent);
                    finish();
                }
            });

            //nhấn nút back của hệ thống
            getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    wordLayoutBinding.btnBack.callOnClick();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: Word");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: word");
    }
}