package online.z0lk1n.android.instagram_lite.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import online.z0lk1n.android.instagram_lite.R;
import online.z0lk1n.android.instagram_lite.model.PhotoItem;
import online.z0lk1n.android.instagram_lite.util.AutoFitGridLayoutManager;
import online.z0lk1n.android.instagram_lite.util.RecyclerViewAdapter;
import online.z0lk1n.android.instagram_lite.util.VirtualDatabase;

import static android.app.Activity.RESULT_OK;

public class PhotoTilesFragment extends Fragment implements View.OnClickListener {
    public static final String NAME = "cb2d00bb-ca6b-45e6-a501-80f70efa65b9";
    private static final String TAG = "PhotoTilesFragment";
    public static final String PICTURE = "205348a2-a71c-4269-b894-6eb778180e5f";
    private RecyclerView recyclerView;
    private int currentPosition = 0;
    private final int CAMERA_REQUEST = 1;
    private Uri photoURI;
    private File storageDir;
    private Snackbar uploadedSnackbar;
    private RecyclerViewAdapter adapter;
    private int numberOfColumns;
    private List<PhotoItem> photoItemList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        numberOfColumns = AutoFitGridLayoutManager.calculateNumberOfColumns(context);
        storageDir = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_tiles, container, false);
        photoItemList = VirtualDatabase.getInstance().photoItemList;
        getFilesList();
        recyclerView = view.findViewById(R.id.recycler_view);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_picture);
        fab.setOnClickListener(this);
        uploadedSnackbar = Snackbar.make(view, R.string.photo_uploaded, Snackbar.LENGTH_SHORT);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(photoItemList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosition = position;
            }
        });

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(PICTURE, 0);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PICTURE, currentPosition);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_picture:
                capturePhoto();
                break;
            default:
                break;
        }
    }

    private void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.provider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photoItemList.add(new PhotoItem(photoURI.getPath(), false));
//            adapter.notifyDataSetChanged();
            uploadedSnackbar.show();

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void getFilesList() {

        if (photoItemList.size() != storageDir.listFiles().length) {
            for (File file : storageDir.listFiles()) {
               photoItemList.add(new PhotoItem(file.getPath(), false));
            }
        }
    }
}
