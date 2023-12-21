package languageapplication.com.main.mastermind;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import languageapplication.com.main.mastermind.adapter.WordAdapter;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.SearchLayoutBinding;
import languageapplication.com.main.mastermind.models.SharePreference;
import languageapplication.com.main.mastermind.models.Word;

public class SearchActivity extends AppCompatActivity {

    private SearchLayoutBinding searchLayoutBinding;
    private ArrayList<Word> words;
    private DatabaseManager manager;
    private WordAdapter wordArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        Log.d("life", "onCreate: search");

        searchLayoutBinding = SearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(searchLayoutBinding.getRoot());

        manager = new DatabaseManager(this);
        manager.open();

        //lấy ra từ vựng yêu thích
        //mạc đỉnh lần đâầu tiên chưa search sẽ đổ ra các từ vựng yêu thích
        words = manager.getWordsByFolderId(0);

        wordArrayAdapter = new WordAdapter(this, R.layout.list_word_item, words);
        searchLayoutBinding.lvSearchWords.setAdapter(wordArrayAdapter);

        //nhấn nút btnBack để quay lại màn hình chính
        searchLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
            }
        });

        //nhấn nút back của hệ thống
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                searchLayoutBinding.btnBack.callOnClick();
            }
        });



        //tìm kiếm từ vựng theo từ khoá đang nhập
        searchLayoutBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence enteredKey, int i, int i1, int i2) {

                words.clear();
                words.addAll(manager.getWordsByKey(enteredKey.toString()));

                wordArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //nhấn Enter để hoàn thành tìm kiếm
        searchLayoutBinding.edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                String enteredKey = searchLayoutBinding.edtSearch.getText().toString();
                Log.d("TAG", "onKey: "+enteredKey);
                int id = 0;
                if(words.size() > 0){
                    id = words.get(0).getId();
                }

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    Intent intent = new Intent(SearchActivity.this, WordActivity.class);

                    intent.putExtra("key", enteredKey);
                    intent.putExtra("id", id);

                    startActivity(intent);

                    return true;
                }

                return false;
            }
        });

        //chọn 1 từ trong listview để hiển thị
        searchLayoutBinding.lvSearchWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, WordActivity.class);

                intent.putExtra("key", words.get(i).getWord());
                intent.putExtra("id", words.get(i).getId());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: Search");
        searchLayoutBinding.edtSearch.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: Search");
    }

}