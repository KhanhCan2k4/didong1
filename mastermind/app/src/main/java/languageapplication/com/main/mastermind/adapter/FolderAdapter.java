package languageapplication.com.main.mastermind.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import languageapplication.com.main.mastermind.R;
import languageapplication.com.main.mastermind.databinding.FolderItemBinding;
import languageapplication.com.main.mastermind.models.Folder;

public class FolderAdapter extends ArrayAdapter<Folder> {
    //các trường dữ liệu
    private Activity context;
    private ArrayList<Folder> folders;
    //constructor
    public FolderAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Folder> objects) {
        super(context, resource, objects);
        this.context = context;
        this.folders = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FolderItemBinding folderItemBinding;

        if (convertView == null) {
            folderItemBinding = FolderItemBinding.inflate(context.getLayoutInflater(), parent, false);
            convertView = folderItemBinding.getRoot();

            convertView.setTag(folderItemBinding);
        } else {
            folderItemBinding = (FolderItemBinding) convertView.getTag();
        }

        Folder folder = folders.get(position);

        if(folder.getId() == 0) {
            folderItemBinding.txtLevel.setBackgroundResource(R.drawable.baseline_stars_24);
            folderItemBinding.txtLevel.setText(" ");
            folderItemBinding.txtFolderName.setBackgroundResource(R.drawable.item_radius);
        } else {
            folderItemBinding.txtLevel.setText("N"+folder.getId());
        }

        folderItemBinding.txtFolderName.setText(folder.getName());
        folderItemBinding.txtFolderName.setTextColor(folderItemBinding.txtLevel.getBackgroundTintList());

        return convertView;
    }
}
