package online.z0lk1n.android.instagram_lite.presentation.ui.mainbottomtab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;

import org.jetbrains.annotations.NotNull;

import online.z0lk1n.android.instagram_lite.R;

public final class NetworkFragment extends MvpAppCompatFragment {

    public static final String NAME = "676e7daa-88da-437c-b675-4075f66de676";
    private static final String TAG = "NetworkFragment";

    public static NetworkFragment newInstance(Bundle bundle) {
        NetworkFragment currentFragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putBundle("gettedArgs", bundle);
        currentFragment.setArguments(args);
        return currentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
    }
}
