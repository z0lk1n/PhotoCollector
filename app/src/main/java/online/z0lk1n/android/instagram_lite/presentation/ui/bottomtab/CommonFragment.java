package online.z0lk1n.android.instagram_lite.presentation.ui.bottomtab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.z0lk1n.android.instagram_lite.App;
import online.z0lk1n.android.instagram_lite.R;
import online.z0lk1n.android.instagram_lite.data.model.PhotoItem;
import online.z0lk1n.android.instagram_lite.presentation.presenters.bottomtab.CommonPresenter;
import online.z0lk1n.android.instagram_lite.util.Const;
import online.z0lk1n.android.instagram_lite.util.FileManager;
import online.z0lk1n.android.instagram_lite.util.PhotoManager;
import online.z0lk1n.android.instagram_lite.util.RecyclerViewAdapter;

import static android.app.Activity.RESULT_OK;

public final class CommonFragment extends MvpAppCompatFragment
        implements RecyclerViewAdapter.OnItemClickListener, CommonView {

    private static final int PHOTO_CAMERA_REQUEST = 1;

    private RecyclerViewAdapter adapter;
    private GridLayoutManager layoutManager;
    private Uri currentUriFile;

    @BindView(R.id.recycler_view_common) RecyclerView recyclerView;
    @BindView(R.id.fab_add_picture) FloatingActionButton fab;

    @Inject PhotoManager photoManager;
    @Inject FileManager fileManager;

    @InjectPresenter CommonPresenter presenter;

    @NonNull
    @ProvidePresenter
    CommonPresenter provideCommonPresenter() {
        CommonPresenter presenter = new CommonPresenter();
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    public static CommonFragment getNewInstance(Bundle bundle) {
        CommonFragment currentFragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putBundle("gettedArgs", bundle);
        currentFragment.setArguments(args);
        return currentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.getInstance().getAppComponent().inject(this);
        layoutManager = new GridLayoutManager(context, photoManager.calculateNumberOfColumns());
    }

    @NonNull
    @Override
    public View onCreateView(@NotNull LayoutInflater li, @Nullable ViewGroup vg, @Nullable Bundle b) {
        View view = li.inflate(R.layout.fragment_common, vg, false);
        init(view);
        return view;
    }

    private void init(@NotNull View view) {
        ButterKnife.bind(this, view);

        adapter = new RecyclerViewAdapter(photoManager, fileManager, photoManager.calculateWidthOfPhoto());
        adapter.setOnItemClickListener(this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> presenter.capturePhoto());
    }

    @Override
    public void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (getActivity() == null || intent.resolveActivity(getActivity().getPackageManager()) == null) {
            return;
        }

        try {
            currentUriFile = fileManager.createUriForIntent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentUriFile);
            startActivityForResult(intent, PHOTO_CAMERA_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_CAMERA_REQUEST && resultCode == RESULT_OK) {
            presenter.addPhoto(currentUriFile.getLastPathSegment());
        }
    }

    @Override
    public void onPhotoClick(int position) {
        presenter.showFullPhoto(position);
    }

    @Override
    public void onPhotoLongClick(int position) {
        presenter.onPhotoLongClick(position);
    }

    @Override
    public void onFavoritesClick(boolean isChecked, int position) {
        presenter.onFavoritesClick(isChecked, position);
    }

    @Override
    public void showDeletePhotoDialog(final int position) {
        if (getContext() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.ask_delete_photo)
                .setPositiveButton(R.string.ok_button, (dialog, which) -> presenter.deletePhoto(position))
                .setNegativeButton(R.string.cancel_button, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void showNotifyingMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void notifyItem(int position, int action) {
        switch (action) {
            case Const.NOTIFY_ITEM_INSERT:
                adapter.notifyItemInserted(position);
                break;
            case Const.NOTIFY_ITEM_REMOVE:
                adapter.notifyItemRemoved(position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.setOnItemClickListener(null);
    }

    @Override
    public void updatePhotoList(List<PhotoItem> photoItems) {
        adapter.addItems(photoItems);
    }
}