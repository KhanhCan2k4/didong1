package languageapplication.com.main.mastermind.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.room.Dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import languageapplication.com.main.mastermind.R;
import languageapplication.com.main.mastermind.config.App;
import languageapplication.com.main.mastermind.models.Word;

/**
 * Kết nối, quản lý database
 */
@Dao
public class DatabaseHelper extends SQLiteOpenHelper {
    //tham số của database
    public static final String DB_NAME = "mastermind_1.sql";
    public static final int DB_VERSION = 1;

    //tham số của bảng 'words'
    public static final String TB_1_NAME = "words";
    public static final String COL_1 = "id";
    public static final String COL_2 = "word";
    public static final String COL_3 = "meaning";
    public static final String COL_4 = "furigana";
    public static final String COL_5 = "romaji";
    public static final String COL_6 = "level";

    //tham số của bảng 'favourites'
    public static final String TB_2_NAME = "favourites";
    //sử dụng lại COL_1

    /**
     * câu truy vấn khởi tạo bảng 'words'
     */
    private final static String CREATE_TB_1 =
            "CREATE TABLE IF NOT EXISTS "+ TB_1_NAME+ " (" +
                    COL_1 +" INTEGER, " +
                    COL_2 +" TEXT, " +
                    COL_3 +" TEXT, " +
                    COL_4 +" TEXT, " +
                    COL_5 +" TEXT, " +
                    COL_6 +" INTEGER, PRIMARY KEY("+COL_1+") );";

    /**
     * câu truy vấn khởi tạo bảng 'favourites'
     */
    public final static String CREATE_TB_2 =
                "CREATE TABLE IF NOT EXISTS "+TB_2_NAME+" ( "+
                        COL_1 +" INTEGER, " +
                        "PRIMARY KEY("+COL_1+")," +
                        " FOREIGN KEY("+COL_1+") REFERENCES "+ TB_1_NAME +"("+ DatabaseHelper.COL_1+") );";
    //constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * hàm được gọi khi không tồn tại database
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TB_1);
        sqLiteDatabase.execSQL(CREATE_TB_2);
        initDataInsertExcute(sqLiteDatabase);
    }

    /**
     * hàm được gọi khi nâng cấp database
     * @param sqLiteDatabase
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        initData();
    }

    /**
     * Thêm dữ liệu từ database cũ
     * Database cũ được lưu trong thư mục raw (res/raw)
     * @param database
     */
    public static void initDataInsertExcute(SQLiteDatabase database) {

        try {
            String sql = "";
            InputStream fis = App.getContext().getResources().openRawResource(R.raw.words);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

            while ((sql = bufferedReader.readLine()) != null) {
                database.execSQL(sql);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Đọc lại dữ liệu cũ
     */
    public static void initData() {
       try {
            InputStream inputStream = App.getContext().getResources().openRawResource(R.raw.mastermind);
            OutputStream outputStream = new FileOutputStream(App.getContext().getDatabasePath(DB_NAME));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer))>0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
