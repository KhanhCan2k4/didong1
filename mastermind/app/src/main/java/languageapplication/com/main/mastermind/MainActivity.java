package languageapplication.com.main.mastermind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import languageapplication.com.main.mastermind.adapter.FolderAdapter;
import languageapplication.com.main.mastermind.config.Constains;
import languageapplication.com.main.mastermind.database.DatabaseManager;
import languageapplication.com.main.mastermind.databinding.DrawableLayoutBinding;
import languageapplication.com.main.mastermind.databinding.HeaderSettingLayoutBinding;
import languageapplication.com.main.mastermind.databinding.MainLayoutBinding;
import languageapplication.com.main.mastermind.models.SharePreference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainLayoutBinding mainLayoutBinding;
    private DrawableLayoutBinding drawableLayoutBinding;
    private HeaderSettingLayoutBinding headerSettingLayoutBinding;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private FolderAdapter adapter;
    private DatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("life", "onCreate: Main");

        //đặt chủ đề
        setTheme(SharePreference.getSaveTheme(this));

        //setting
        headerSettingLayoutBinding = HeaderSettingLayoutBinding.inflate(getLayoutInflater());
        Constains.setLogo(this, headerSettingLayoutBinding.logo);
        drawableLayoutBinding = DrawableLayoutBinding.inflate(getLayoutInflater());

        mainLayoutBinding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(drawableLayoutBinding.getRoot());

        manager = new DatabaseManager(this);
        manager.open();

        ((TextView)findViewById(R.id.txtFavTotal)).setText("Total favourite words: " + manager.getFavouritesTotal());
        ((TextView)findViewById(R.id.txtRecentPoint)).setText("Recent point: " + SharePreference.getRecentPoint(MainActivity.this));

        // tao side menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.opendrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        //lay su kien cho cac button mau
        ImageButton btn_gre = findViewById(R.id.btn_color0);
        chooseTheme(btn_gre, R.style.AppTheme_green);
        ImageButton btn_red = findViewById(R.id.btn_color1);
        chooseTheme(btn_red, R.style.AppTheme_Red);
        ImageButton btn_yel = findViewById(R.id.btn_color2);
        chooseTheme(btn_yel, R.style.AppTheme_Yellow);
        ImageButton btn_pur = findViewById(R.id.btn_color3);
        chooseTheme(btn_pur, R.style.AppTheme_purple);
        ImageButton btn_nav = findViewById(R.id.btn_color4);
        chooseTheme(btn_nav, R.style.AppTheme_Navy);
        ImageButton btn_sky = findViewById(R.id.btn_color5);
        chooseTheme(btn_sky, R.style.AppTheme_skyblue);
        ImageButton btn_pin = findViewById(R.id.btn_color6);
        chooseTheme(btn_pin, R.style.AppTheme_pink);
        ImageButton btn_emr = findViewById(R.id.btn_color7);
        chooseTheme(btn_emr, R.style.AppTheme_Emerald);
        ImageButton btn_gay = findViewById(R.id.btn_color8);
        chooseTheme(btn_gay, R.style.AppTheme_gray);

        //đổ dữ liệu lên listvieew
        adapter = new FolderAdapter(this, R.layout.folder_item, Constains.getFolders());
        Log.d("TAG", "onCreate: " + Constains.getFolders().size());
        ((ListView)findViewById(R.id.lvFolders)).setAdapter(adapter);

        //chọn folders tương ứng để xem
        ((ListView)findViewById(R.id.lvFolders)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ViewFolderActivity.class);

                intent.putExtra("id", Constains.getFolders().get(i).getId());
                startActivity(intent);
            }
        });


        Button btnSearch = findViewById(R.id.btnSearch);

        //set màu chữ cho btnSearch
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        btnSearch.setTextColor(linearLayout.getBackgroundTintList());

        //nhấn nút tìm kiếm btnSearch để thực hiện tìm kiếm
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("life", "onResume: Main");

        ((TextView)findViewById(R.id.txtFavTotal)).setText("Total favourite words: " + manager.getFavouritesTotal());
        ((TextView)findViewById(R.id.txtRecentPoint)).setText("Recent point: " + SharePreference.getRecentPoint(MainActivity.this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("life", "onDestroy: Main");
    }

    private void chooseTheme(ImageButton button, int newTheme) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreference.saveTheme(MainActivity.this, newTheme);
                Constains.setLogo(MainActivity.this, headerSettingLayoutBinding.logo);
                recreate();
            }
        });
    }

    /**
     * Menu setting
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu_layout,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}