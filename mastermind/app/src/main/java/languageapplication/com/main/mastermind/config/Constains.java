package languageapplication.com.main.mastermind.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;

import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import languageapplication.com.main.mastermind.R;
import languageapplication.com.main.mastermind.models.Folder;
import languageapplication.com.main.mastermind.models.SharePreference;

/**
 * class chứa các tham số, các hàm dùng chung cho toàn bộ chương trình
 */
public class Constains {

    /**
     * Thời gian tối thiểu đề chờ vào màn hình Main
     */
    public static final int COUNT_DOWN_TIME = 3000; //3s

    /**
     * Key dùng để cách biệt các trường dữ liệu của Word
     */
    public static final String MEANING_CHAR_KEY = " * ";

    /**
     * Số lượng từ cần để thực hiện quiz
     * Yêu cầu >= 5
     */
    public static final int MAX_QUIZ = 12;

    /**
     * Lưu lại danh sách các folder của app
     */
    private static ArrayList<Folder> folders;

    /**
     * Lấy ra danh sách các folder của app
     * @return ArrayList<Folder> folders
     */
    public static ArrayList<Folder> getFolders() {
        //chưa có folders
        if(folders == null){
            //khởi tạo
            folders = new ArrayList<>();

            //set dữ liệu
            Folder folder0 = new Folder(0,"My favorite");
            Folder folder5 = new Folder(5,"Basic");
            Folder folder4 = new Folder(4,"Pre-Intermidiate");
            Folder folder3 = new Folder(3,"Intermidiate");
            Folder folder2 = new Folder(2,"Pre-Advanced");
            Folder folder1 = new Folder(1,"Advanced");

            folders.add(folder0);
            folders.add(folder1);
            folders.add(folder2);
            folders.add(folder3);
            folders.add(folder4);
            folders.add(folder5);
        }

        //có folders
        //tra về folders
        return folders;
    }

    /**
     * set lại logo cho ứng dụng
     * set theo theme tương ứng của ứng dụng cho ImageView
     * @param context
     * @param imageView
     */
    public static void setLogo(Context context, ImageView imageView){
        int theme = SharePreference.getSaveTheme(context);
        if(theme == R.style.AppTheme_Navy){
            imageView.setImageResource(R.drawable.logo_navy);
        } else if (theme == R.style.AppTheme_green) {
            imageView.setImageResource(R.drawable.logo_green);
        } else if (theme == R.style.AppTheme_Red){
            imageView.setImageResource(R.drawable.logo_red);
        } else if (theme == R.style.AppTheme_Yellow) {
            imageView.setImageResource(R.drawable.logo_yellow);
        } else if (theme == R.style.AppTheme_purple){
            imageView.setImageResource(R.drawable.logo_purple);
        } else if (theme == R.style.AppTheme_skyblue) {
            imageView.setImageResource(R.drawable.logo_blue);
        } else if (theme == R.style.AppTheme_pink){
            imageView.setImageResource(R.drawable.logo_pink);
        } else if (theme == R.style.AppTheme_gray){
            imageView.setImageResource(R.drawable.logo_gray);
        } else if (theme == R.style.AppTheme_Emerald){
            imageView.setImageResource(R.drawable.logo_dark_green);
        }
    }
}
