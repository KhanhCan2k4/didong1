package languageapplication.com.main.mastermind;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import languageapplication.com.main.mastermind.adapter.WordAdapter;
import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.ViewFolderLayoutBinding;
import languageapplication.com.main.mastermind.models.Folder;
import languageapplication.com.main.mastermind.models.SharePreference;
import languageapplication.com.main.mastermind.models.Word;

public class ViewFolderActivity extends AppCompatActivity {

    private ViewFolderLayoutBinding viewFolderLayoutBinding;

    private WordAdapter wordArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("life", "onCreate: viewfolder");

        //set chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        viewFolderLayoutBinding = ViewFolderLayoutBinding.inflate(getLayoutInflater());
        setContentView(viewFolderLayoutBinding.getRoot());

        DatabaseManager manager = new DatabaseManager(this);
        manager.open();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        Folder folder = Constains.getFolders().get(id);
        folder.setWords( manager.getWordsByFolderId(id));

        //hiển thị dữ liệu
        viewFolderLayoutBinding.txtFolderName.setText(folder.getName());

        //hiển thị nút quiz
        if (id == 0 && folder.getWords().size() >= Constains.MAX_QUIZ) {
            viewFolderLayoutBinding.btnQuiz.setVisibility(View.VISIBLE);
        } else {
            viewFolderLayoutBinding.btnQuiz.setVisibility(View.GONE);
        }

        //set dữ liệu lên listview
        wordArrayAdapter = new WordAdapter(this, R.layout.list_word_item, folder.getWords());
        viewFolderLayoutBinding.lvFolders.setAdapter(wordArrayAdapter);

        //review word tương ứng
        viewFolderLayoutBinding.lvFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewFolderActivity.this, ReviewWordActivity.class);
                intent.putExtra("id", folder.getId());
                intent.putExtra("index", i);

                startActivity(intent);
                finish();
            }
        });

        //thực hiện quiz
        viewFolderLayoutBinding.btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFolderActivity.this, QuizActivity.class);

                startActivity(intent);
                finish();
            }
        });

        //nhấn nút back để quay lại màn hình Main
        viewFolderLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewFolderActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
                finish();
            }
        });

        //nhấn nút back hệ thống
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewFolderLayoutBinding.btnBack.callOnClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: ViewFolder");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: ViewFolder");
    }
}