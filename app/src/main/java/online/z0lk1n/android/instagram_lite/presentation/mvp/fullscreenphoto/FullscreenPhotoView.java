package online.z0lk1n.android.instagram_lite.presentation.mvp.fullscreenphoto;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FullscreenPhotoView extends MvpView {
}