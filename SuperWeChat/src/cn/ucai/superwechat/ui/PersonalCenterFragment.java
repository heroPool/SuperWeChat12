package cn.ucai.superwechat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.superwechat.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends Fragment {

    @BindView(R.id.image_personal_avatar)
    ImageView imagePersonalAvatar;
    @BindView(R.id.text_usernick)
    TextView textUsernick;
    @BindView(R.id.text_username)
    TextView textUsername;
    @BindView(R.id.layout_avatar)
    RelativeLayout layoutAvatar;
    Unbinder unbinder;

    public PersonalCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.layout_avatar, R.id.layout_photo, R.id.layout_collect, R.id.layout_money, R.id.layout_smail, R.id.layout_settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_avatar:
                break;
            case R.id.layout_photo:
                break;
            case R.id.layout_collect:
                break;
            case R.id.layout_money:
                break;
            case R.id.layout_smail:
                break;
            case R.id.layout_settings:
                break;
        }
    }
}
