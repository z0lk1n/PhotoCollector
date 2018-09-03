package online.z0lk1n.android.instagram_lite.util;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import online.z0lk1n.android.instagram_lite.R;
import online.z0lk1n.android.instagram_lite.activity.Navigator;
import online.z0lk1n.android.instagram_lite.model.PhotoItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private List<PhotoItem> photoItemList;
    private int dimens;

    public RecyclerViewAdapter(List<PhotoItem> photoItemList, int dimens) {
        this.photoItemList = photoItemList;
        this.dimens = dimens;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        bindView(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return photoItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgViewPhoto;
        ImageView imgFavorites;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imgViewPhoto = itemView.findViewById(R.id.imgView_picture);
            imgFavorites = itemView.findViewById(R.id.imgView_favorites);

            imgViewPhoto.setOnClickListener(view ->
                    new Navigator().showPhotoFragment(
                            (AppCompatActivity) itemView.getContext(),
                            photoItemList.get(getAdapterPosition()).getPhotoPath())
            );
            imgViewPhoto.setOnLongClickListener(view -> {
                showDeletePhotoDialog(view, getAdapterPosition());
                return true;
            });
            imgFavorites.setOnClickListener(view -> addOrRemoveFavorites(getAdapterPosition()));
        }
    }

    private void bindView(RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        PhotoManager.setPhoto(viewHolder.imgViewPhoto, getFile(position), dimens, dimens);
        if (photoItemList.get(position).isFavorites()) {
            viewHolder.imgFavorites.setImageResource(R.drawable.ic_star);
        } else {
            viewHolder.imgFavorites.setImageResource(R.drawable.ic_star_border);
        }
    }

    private File getFile(int position) {
        return new File(photoItemList.get(position).getPhotoPath());
    }

    private void showDeletePhotoDialog(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                .setTitle("Delete Photo?")
                .setPositiveButton("OK", (dialog, which) -> {
                    new File(photoItemList.get(position).getPhotoPath()).delete();
                    photoItemList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addOrRemoveFavorites(int position) {
        if (photoItemList.get(position).isFavorites()) {
            photoItemList.get(position).setFavorites(false);
        } else {
            photoItemList.get(position).setFavorites(true);
        }
        notifyItemChanged(position);
    }
}
