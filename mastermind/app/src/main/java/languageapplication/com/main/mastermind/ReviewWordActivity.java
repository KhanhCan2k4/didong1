package languageapplication.com.main.mastermind;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.WordLayoutBinding;
import languageapplication.com.main.mastermind.models.Folder;
import languageapplication.com.main.mastermind.models.SharePreference;
import languageapplication.com.main.mastermind.models.Word;

public class ReviewWordActivity extends AppCompatActivity {

    private WordLayoutBinding wordLayoutBinding;
    private DatabaseManager manager;
    private Folder folder;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("life", "onCreate: review");

        //set chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        wordLayoutBinding = WordLayoutBinding.inflate(getLayoutInflater());
        setContentView(wordLayoutBinding.getRoot());

        manager = new DatabaseManager(this);
        manager.open();

        Intent intent = getIntent();
        int folderID = intent.getIntExtra("id",0);
        index = intent.getIntExtra("index",0);

        folder = Constains.getFolders().get(folderID);

        //hiển thị dữ liệu
        wordLayoutBinding.txtSearch.setText(folder.getName());
        wordLayoutBinding.Buttons.setVisibility(View.VISIBLE);
        updateWord();

        //lưu từ yêu thích
        wordLayoutBinding.btnChooseFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordLayoutBinding.btnChooseFav.getTag().equals("0")) {
                    wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_24);
                    wordLayoutBinding.btnChooseFav.setTag("1");

                    //lưu lên database
                    manager.setFavourite(folder.getWords().get(index).getId());

                } else {
                    wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_outline_24);
                    wordLayoutBinding.btnChooseFav.setTag("0");

                    //lưu lên database
                    manager.deleteFavourite(folder.getWords().get(index).getId());
                }
            }
        });

        //nhấn nút bntPrev để hiển thị từ vựng ở trí phía trước
        wordLayoutBinding.btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if(index == -1) {
                    index = folder.getWords().size() -1;
                }
                updateWord();
            }
        });

        //nhấn nút bntNext để hiển thị từ vựng ở trí phía sau
        wordLayoutBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if(index == folder.getWords().size()) {
                    index = 0;
                }
                updateWord();
            }
        });

        //nhấn nút btnBack đề quy lại ViewFolder
        wordLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewWordActivity.this, ViewFolderActivity.class);

                intent.putExtra("id", folderID);

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

    /**
     * Hiện thị dữ liệu của word thứ index
     */
    private void updateWord() {
        Word word = folder.getWords().get(index);

        if(word == null) {
            return;
        }

        //hiển thị nút yêu thích
        if(manager.isFavourite(word.getId())) {
            wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_24);
            wordLayoutBinding.btnChooseFav.setTag("1");
        } else {
            wordLayoutBinding.btnChooseFav.setImageResource(R.drawable.baseline_star_outline_24);
            wordLayoutBinding.btnChooseFav.setTag("0");
        }

        //hển thị dữ liệu
        wordLayoutBinding.txtWord.setText(word.getWord());
        wordLayoutBinding.txtFurigana.setText(word.getFurigana());
        wordLayoutBinding.txtRomaji.setText(word.getRomaji());
        wordLayoutBinding.txtLevel.setText("N"+word.getLevel());
        wordLayoutBinding.txtMeaning.setText(word.getMeaning());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: Review");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: Review");
    }
}