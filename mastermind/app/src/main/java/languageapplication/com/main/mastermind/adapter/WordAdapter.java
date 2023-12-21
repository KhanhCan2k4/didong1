package languageapplication.com.main.mastermind.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import languageapplication.com.main.mastermind.databinding.ListWordItemBinding;
import languageapplication.com.main.mastermind.models.Word;

public class WordAdapter extends ArrayAdapter<Word> {
    //trường dữ liệu
    private Activity context;
    private ArrayList<Word> words;

    //constructor
    public WordAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Word> objects) {
        super(context, resource, objects);
        this.context = context;
        this.words = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ListWordItemBinding binding;

        if(convertView == null){
            binding = ListWordItemBinding.inflate(context.getLayoutInflater(), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        else{
            binding = (ListWordItemBinding) convertView.getTag();
        }

        Word word = words.get(position);
        binding.txtlevel.setText("N"+word.getLevel());
        binding.txtfuri.setText(word.getFurigana());
        binding.txtword.setText(word.getWord());
        binding.txtMeaning.setText(word.getMeaning());

        binding.txtfuri.setTextColor(binding.txtlevel.getBackgroundTintList());
        binding.txtword.setTextColor(binding.txtlevel.getBackgroundTintList());
        binding.txtMeaning.setTextColor(binding.txtlevel.getBackgroundTintList());

        return  convertView;
    }
}
