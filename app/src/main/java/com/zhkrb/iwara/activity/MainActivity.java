package com.zhkrb.iwara.activity;


import android.view.View;
import com.zhkrb.iwara.R;
import com.zhkrb.iwara.base.AbsActivity;
import com.zhkrb.iwara.base.FragmentFrame;
import com.zhkrb.iwara.fragment.GalleryFragment;


public class MainActivity extends AbsActivity implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void main() {
        super.main();
        loadRootFragment(new FragmentFrame(GalleryFragment.class));
    }

    @Override
    protected int getContentView() {
        return R.id.frame_layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }


}
