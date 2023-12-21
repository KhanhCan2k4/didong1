package languageapplication.com.main.mastermind;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.QuizLayoutBinding;
import languageapplication.com.main.mastermind.models.Folder;
import languageapplication.com.main.mastermind.models.SharePreference;
import languageapplication.com.main.mastermind.models.Word;

public class QuizActivity extends AppCompatActivity {

    private int index = 0;
    private Button correctButton;
    private Drawable btn_default_color;
    private int correctAmount = 0;

    private int correctAnswerIndex = 0;

    private Folder folder = Constains.getFolders().get(0);
    private ArrayList<Integer> questions;

    private DatabaseManager manager;

    private QuizLayoutBinding quizLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("life", "onCreate: Quiz");

        //set chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        quizLayoutBinding = QuizLayoutBinding.inflate(getLayoutInflater());
        setContentView(quizLayoutBinding.getRoot());

        //lấy màu hệ thống
        btn_default_color = quizLayoutBinding.btnA.getBackground();

        manager = new DatabaseManager(this);
        manager.open();

        //lấy ra các từ vựng yêu thích
        folder.setWords(manager.getWordsByFolderId(0));

        //lưu các từ quiz
        ArrayList<Word> words = new ArrayList<>();

        for(int i = 1; i <= Constains.MAX_QUIZ; i++){
            Word temp = new Word();
            do {
                temp = folder.getWords().get((int)(Math.random()*folder.getWords().size()));
            } while (words.contains(temp));

            words.add(temp);
        }
        folder.setWords(words);

        updateQuestion();

        quizLayoutBinding.txtFolderName.setText(folder.getName());

        //chọn đáp án
        chooseAnswer(quizLayoutBinding.btnA);
        chooseAnswer(quizLayoutBinding.btnB);
        chooseAnswer(quizLayoutBinding.btnC);
        chooseAnswer(quizLayoutBinding.btnD);

        //nhấn nút bntNext để qua câu hỏi tiếp theo
        //nếu hết câu hỏi thì chuyển đến trang Result
        quizLayoutBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;

                //hoàn thành
                if(index == folder.getWords().size()) {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);

                    intent.putExtra("correct", correctAmount);
                    intent.putExtra("total", folder.getWords().size());

                    startActivity(intent);
                    finish();
                } else {
                    updateQuestion();
                }

            }
        });

        //nhấn nút back để quay lại amfn hình ViewFolders
        quizLayoutBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, ViewFolderActivity.class);

                startActivity(intent);
                finish();
            }
        });
        //nhấn nút back của hệ thống
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                quizLayoutBinding.btnBack.callOnClick();
            }
        });
    }

    /**
     * hiển thị câu hỏi
     */
    private void updateQuestion() {
        questions = new ArrayList<>();
        questions.add(0);
        questions.add(0);
        questions.add(0);
        questions.add(0);

        //lấy ra vị trí kết quả đúng
        correctAnswerIndex = (int) (Math.random()* 4);
        questions.set(correctAnswerIndex, index);

        //set Button đúng
        switch (correctAnswerIndex) {
            case 0:
                correctButton = quizLayoutBinding.btnA;
                break;
            case 1:
                correctButton = quizLayoutBinding.btnB;
                break;
            case 2:
                correctButton = quizLayoutBinding.btnC;
                break;
            case 3:
                correctButton = quizLayoutBinding.btnD;
                break;
        }

        //lấy ngẫu nhiên các đáp án sai
        int temp;
        for (int i = 0; i < 4; i++) {
            if (i != correctAnswerIndex) {
                do {
                    temp = (int)(Math.random()* folder.getWords().size());
                } while (questions.contains(temp));

                questions.set(i, temp);
            }
        }

        //hiển thị dữ liệu
        quizLayoutBinding.txtWord.setText(folder.getWords().get(index).getWord());

        //set màu cho các buttons
        quizLayoutBinding.btnA.setBackground(btn_default_color);
        quizLayoutBinding.btnB.setBackground(btn_default_color);
        quizLayoutBinding.btnC.setBackground(btn_default_color);
        quizLayoutBinding.btnD.setBackground(btn_default_color);

        //set trạng thái VISIBLE cho các buttons
        quizLayoutBinding.btnA.setVisibility(View.VISIBLE);
        quizLayoutBinding.btnB.setVisibility(View.VISIBLE);
        quizLayoutBinding.btnC.setVisibility(View.VISIBLE);
        quizLayoutBinding.btnD.setVisibility(View.VISIBLE);

        //set Enabled cho các butotns
        quizLayoutBinding.btnA.setEnabled(true);
        quizLayoutBinding.btnB.setEnabled(true);
        quizLayoutBinding.btnC.setEnabled(true);
        quizLayoutBinding.btnD.setEnabled(true);
        correctButton.setEnabled(true);

        //ẩn nút next khi chưa chọn đáp án
        quizLayoutBinding.btnNext.setVisibility(View.INVISIBLE);

        //hiển thị ra các câu hỏi tương ứng
        quizLayoutBinding.btnA.setText(folder.getWords().get(questions.get(0)).getMeaning());
        quizLayoutBinding.btnB.setText(folder.getWords().get(questions.get(1)).getMeaning());
        quizLayoutBinding.btnC.setText(folder.getWords().get(questions.get(2)).getMeaning());
        quizLayoutBinding.btnD.setText(folder.getWords().get(questions.get(3)).getMeaning());
    }

    private void chooseAnswer(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hiện nút next để đi tới câu tiếp theo
                quizLayoutBinding.btnNext.setVisibility(View.VISIBLE);

                //ẩn các buttons
                quizLayoutBinding.btnA.setVisibility(View.INVISIBLE);
                quizLayoutBinding.btnB.setVisibility(View.INVISIBLE);
                quizLayoutBinding.btnC.setVisibility(View.INVISIBLE);
                quizLayoutBinding.btnD.setVisibility(View.INVISIBLE);

                //chọn đúng
                if(button == correctButton) {
                    correctAmount++;
                } else {
                    //chọn sai
                    //hiển thị câu sai
                    button.setBackgroundColor(SharePreference.getSaveTheme(QuizActivity.this));
                    button.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                }

                //hiển thị câu đúng
                correctButton.setVisibility(View.VISIBLE);
                correctButton.setEnabled(false);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("life", "onResume: Quiz");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: Quiz");
    }
}