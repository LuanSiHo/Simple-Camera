package com.hosiluan.simplecamera;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by User on 11/9/2017.
 */

public class FragmentPhotoToolbarDefault extends android.support.v4.app.Fragment {



    DefaultToolbarFragmentListener mDefaultToolbarFragmentListener;
    private static FragmentPhotoToolbarDefault sFragment;
    private ImageButton mBackImageButton;

    public static FragmentPhotoToolbarDefault create(){
        if (sFragment == null){
            sFragment = new FragmentPhotoToolbarDefault();
        }
        return sFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDefaultToolbarFragmentListener = (DefaultToolbarFragmentListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_photo_toolbar_default,container,false);
        mBackImageButton = view.findViewById(R.id.img_btn_user_info_back);
        setEvent();
        return view;
    }

    private void setEvent(){
        mBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDefaultToolbarFragmentListener.onBack();
            }
        });
    }



    public interface DefaultToolbarFragmentListener{
        void onBack();
    }
}
