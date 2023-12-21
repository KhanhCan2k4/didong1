package languageapplication.com.main.mastermind.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.models.Word;

/**
 * class quản lý DatabaseHelper
 * quản lý các câu truy vấn dữ liệu
 */

public class DatabaseManager {
    //các trường dữ liệu
    public DatabaseHelper dbHelper;
    private Context context;

    public SQLiteDatabase database;

    //constructor
    public DatabaseManager(Context c) {
        context = c;
    }

    /**
     * Mở database để thực thi truy vấn
     * (Lấy getWritableDatabase)
     * @return
     * @throws SQLException
     */
    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    //đóng database
    public void close() {
        dbHelper.close();
    }

    /**
     * Lấy ra các từ vựng theo trong folders
     * @param id
     * @return
     */
    public ArrayList<Word> getWordsByFolderId(int id) {
        ArrayList<Word> words = new ArrayList<>();

        //lấy ra từ vựng trong thư mục yêu thích
        if (id == 0) {
            String sql = "SELECT * FROM " + DatabaseHelper.TB_2_NAME + "";

            Cursor cursor = database.rawQuery(sql, null);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Word word = getWordById(cursor.getInt(0));

                    words.add(word);
                    cursor.moveToNext();
                }
            }

            return  words;
        }

        //lấy từ vựng trong những thư mục còn lại
        String sql = "SELECT * FROM " + DatabaseHelper.TB_1_NAME + " WHERE " + DatabaseHelper.COL_6 +" = " + id;

        Cursor cursor = database.rawQuery(sql, null);

        Log.d("TAG", "getWordsByFolderId: query completed");

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Word word = new Word();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(1));
                word.setMeaning(cursor.getString(2));
                word.setFurigana(cursor.getString(3));
                word.setRomaji(cursor.getString(4));
                word.setLevel(cursor.getInt(5));

                words.add(word);
                cursor.moveToNext();
            }
        }

        return words;
    }

    /**
     * lấy từ vựng theo ID tương ứng
     * @param id
     * @return
     */
    public Word getWordById(int id) {
        Word word = new Word();
        String sql = "SELECT * FROM " + DatabaseHelper.TB_1_NAME + " WHERE id = " + id;

        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(1));
                word.setMeaning(cursor.getString(2));
                word.setFurigana(cursor.getString(3));
                word.setRomaji(cursor.getString(4));
                word.setLevel(cursor.getInt(5));
            }
        }

        return  word;
    }

    /**
     * Lấy danh sách từ vựng theo từ khoá gần đúng
     * @param key
     * @return
     */
    public ArrayList<Word> getWordsByKey(String key){
        ArrayList<Word> words = new ArrayList<>();
        String sql = "SELECT * FROM "+DatabaseHelper.TB_1_NAME+" WHERE "+DatabaseHelper.COL_2+" LIKE '%"+key+"%' OR "+DatabaseHelper.COL_3+" LIKE '%"+key+"%' OR "+DatabaseHelper.COL_4+" LIKE '%"+key+"%' OR "+DatabaseHelper.COL_5+" LIKE '%"+key+"%'";

        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Word word = new Word();
                word.setId(cursor.getInt(0));
                word.setWord(cursor.getString(1));
                word.setMeaning(cursor.getString(2));
                word.setFurigana(cursor.getString(3));
                word.setRomaji(cursor.getString(4));
                word.setLevel(cursor.getInt(5));

                words.add(word);
                cursor.moveToNext();
            }
        }

        return  words;
    }

    /**
     * Kiểm tra từ vựng đã được lưu vào thư mục yêu thích chưa
     * @param id
     * @return
     */
    public boolean isFavourite(int id){
        String sql = "SELECT * FROM "+DatabaseHelper.TB_2_NAME+" WHERE "+DatabaseHelper.COL_1+" = " +id;

        Cursor cursor = database.rawQuery(sql,null);
        return cursor != null && cursor.moveToFirst();
    }

    /**
     * Lưu lại từ vựng yêu thích
     * @param id
     */
    public void setFavourite(int id) {
        String sql = "INSERT INTO " + DatabaseHelper.TB_2_NAME + " VALUES (" + id + ")";

        try {
            database.execSQL(sql);
        } catch (SQLException e) {
            //ignore
        }
    }

    /**
     * bỏ yêu thích từ vựng
     * @param id
     */
    public void deleteFavourite(int id) {
        String sql = "DELETE FROM " + DatabaseHelper.TB_2_NAME + " WHERE " + DatabaseHelper.COL_1 + " = " + id;

        try {
            database.execSQL(sql);
        } catch (SQLException e) {
            //ignore
        }
    }

    /**
     * lấy ra tổng số lượng từ vựng yêu thích
     * @return
     */
    public int getFavouritesTotal() {
        try {
            String sql = "SELECT COUNT(*) FROM " + DatabaseHelper.TB_2_NAME;

            Cursor cursor = database.rawQuery(sql, null);

            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            }
        }catch (SQLException e) {
            return 0;
        }
        return 0;
    }
}
