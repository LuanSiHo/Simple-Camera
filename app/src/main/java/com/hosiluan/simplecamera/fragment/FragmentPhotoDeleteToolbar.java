package com.hosiluan.simplecamera.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hosiluan.simplecamera.R;

/**
 * Created by User on 11/9/2017.
 */

public class FragmentPhotoDeleteToolbar extends android.support.v4.app.Fragment {

    DeleteToolbarFragmentListener mDeleteToolbarFragmentListener;

    TextView mHuyTextView,mXoaTextView;

    private static FragmentPhotoDeleteToolbar sFragment;

    public static FragmentPhotoDeleteToolbar create(){
        if (sFragment == null){
            sFragment = new FragmentPhotoDeleteToolbar();
        }
        return sFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDeleteToolbarFragmentListener  = (DeleteToolbarFragmentListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_delete_photo_toolbar,container,false);
        mHuyTextView = view.findViewById(R.id.tv_huy_delete_toolbar);
        mXoaTextView = view.findViewById(R.id.tv_xoa_delete_toolbar);
        setEvent();
        return view;
    }

    private void setEvent(){
        mHuyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteToolbarFragmentListener.onCancel();
            }
        });

        mXoaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteToolbarFragmentListener.onDelete();
            }
        });
    }

    public interface DeleteToolbarFragmentListener{

        void onCancel();
        void onDelete();

    }
}
