package cc.ixcc.novelthree.ui.activity.my;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import cc.ixcc.novelthree.R;

import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.interfaces.ImageResultCallback;
import cc.ixcc.novelthree.treader.util.AppUtils;
import cc.ixcc.novelthree.ui.activity.NewSplashActivity;
import cc.ixcc.novelthree.ui.dialog.PhotoDialog;
import cc.ixcc.novelthree.ui.dialog.SetNickDialog;
import cc.ixcc.novelthree.ui.dialog.SexDialog;
import cc.ixcc.novelthree.ui.dialog.TipDialog;
import cc.ixcc.novelthree.upload.UploadBean;
import cc.ixcc.novelthree.upload.UploadCallback;
import cc.ixcc.novelthree.upload.UploadQnImpl;
import cc.ixcc.novelthree.utils.CleanDataUtils;
import cc.ixcc.novelthree.utils.DialogUitl;
import cc.ixcc.novelthree.utils.ProcessImageUtil;
import cc.ixcc.novelthree.utils.SpUtil;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static cc.ixcc.novelthree.utils.SpUtil.APPSYSTEM;

/**
 * 设置
 */

public class SettingActivity extends MyActivity {
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.change_pic)
    RelativeLayout changePic;
    @BindView(R.id.change_username)
    RelativeLayout changeUsername;
    @BindView(R.id.change_sex)
    RelativeLayout changeSex;
    @BindView(R.id.change_sign)
    RelativeLayout changeSign;
    @BindView(R.id.change_system)
    RelativeLayout changeSystem;
    @BindView(R.id.crash_hc)
    RelativeLayout crashHc;
    @BindView(R.id.bing_number)
    RelativeLayout bingNumber;
    @BindView(R.id.out_app)
    TextView outApp;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.sw_system)
    Switch swSystem;
    @BindView(R.id.au_chapter)
    Switch autoChapter;
    @BindView(R.id.mFlContainer)
    LinearLayout mFlContainer;
    String dbid;
    UserBean userBean;
    ContentValues values = new ContentValues();
    @BindView(R.id.cache)
    TextView cache;
    private ProcessImageUtil mImageUtil;
    private UploadQnImpl mUploadStrategy;
    private TipDialog dialog;

    private MMKV mmkv = MMKV.defaultMMKV();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting2;
    }

    @Override
    protected void initView() {
        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }

            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    if (getContext() == null) {
                        return;
                    }
                    if (dialog == null) {
                        dialog = TipDialog.getMyDialog(getActivity());
                        dialog.setTitle("Whether to replace the avatar");
                        dialog.setCancelCallBack(new TipDialog.DialogCancelCallBack() {
                            @Override
                            public void onCancel() {
                                dialog.dismiss();
                            }
                        });
                        dialog.setDialogCallBack(new TipDialog.DialogCallBack() {
                            @Override
                            public void onActionClick() {
                                Glide.with(getContext())
                                        .load(file.getAbsolutePath())
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(img);
                                values.put("avatar", file.getAbsolutePath());
                                uploadPhoto(file);
                                dialog.dismiss();
                            }
                        });
                    }
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure() {
            }
        });
        getCache();

        swSystem.setChecked(mmkv.decodeBool(APPSYSTEM, false));
        swSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mmkv.encode(APPSYSTEM, isChecked);
            }
        });

        autoChapter.setChecked(mmkv.decodeBool("fuck_auto", false));
        autoChapter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mmkv.encode("fuck_auto", isChecked);
            }
        });
    }

    private void getCache() {
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(Objects.requireNonNull(getContext()));
            cache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCache() {
        //清除缓存
        CleanDataUtils.clearAllCache(Objects.requireNonNull(getContext()));
        String clearSize = CleanDataUtils.getTotalCacheSize(Objects.requireNonNull(getContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDbInfo();
    }

    private void uploadPhoto(File file) {
        List<UploadBean> list = new ArrayList<>();
        list.add(new UploadBean(file));
        if (mUploadStrategy == null) {
            mUploadStrategy = new UploadQnImpl(this);
        }
        mUploadStrategy.upload(list, false, new UploadCallback() {
            @Override
            public void onFinish(List<UploadBean> list, boolean success) {
                if (success) {
                    if (list != null) {
                        for (UploadBean bean : list) {
                            updateAvatar(bean.getRemoteFileName());
                        }
                    }
                }
            }
        });
    }

    private void updateAvatar(String avatar) {
        HttpClient.getInstance().post(AllApi.saveavatar, AllApi.saveavatar)
                .isMultipart(true)
                .params("avatar", avatar)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        getUserInfo();
                    }
                });
    }

    private void getUserInfo() {
        HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        UserBean bean = new Gson().fromJson(info[0], UserBean.class);
                        mmkv.encode(SpUtil.USER_INFO, bean);
                        getDbInfo();
                    }
                });
    }

    @Override
    protected void initData() {

    }

    private void getDbInfo() {
        try {
            userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
            Glide.with(getContext()).load(userBean.getAvatar()).apply(RequestOptions.circleCropTransform()).into(img);
            name.setText(userBean.getNickname());
            sign.setText(userBean.getSign());
            if (userBean.getGender().equals("0")) { //1男2女0保密
            } else {
                sex.setText(userBean.getGender().equals("1") ? "Male" : "Femal");
            }

            //刷新登入登出按钮
            if (userBean.getBindStatus().equals("1")) {
                outApp.setVisibility(View.VISIBLE);
                bingNumber.setVisibility(View.GONE);
            } else {
                outApp.setVisibility(View.GONE);
                bingNumber.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showPhotoDialog() {
        final PhotoDialog dialog = PhotoDialog.getMyDialog(this);
        //回调实现点击
        dialog.setDialogCallBack(new PhotoDialog.DialogCallBack() {
            @Override
            public void onTakePhotoClick() {
                mImageUtil.getImageByCamera(false);
            }

            @Override
            public void onSelectPhotoClick() {
                mImageUtil.getImageByAlumb(false);
            }
        });
        dialog.show();
    }

    SexDialog sexDialog;

    private void showSexDialog() {
        sexDialog = SexDialog.getMyDialog(this);
        //回调实现点击
        sexDialog.setDialogCallBack(new SexDialog.DialogCallBack() {
            @Override
            public void onSexClick(boolean isBoy) {
                setSex(isBoy);
            }
        });
        sexDialog.show();
    }

    SetNickDialog nickDialog;

    private void showNickDialog() {
        nickDialog = SetNickDialog.getMyDialog(this);
        //回调实现点击
        nickDialog.setDialogCallBack(new SetNickDialog.DialogCallBack() {

            @Override
            public void onActionClick(String nickname) {
                saveNick(nickname);
            }
        });
        nickDialog.show();
    }

    private void setSex(boolean isBoy) {
        HttpClient.getInstance().post(AllApi.savesex, AllApi.savesex)
                .isMultipart(true)
                .params("gender", isBoy ? "1" : "2")
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        values.put("gender", isBoy ? "1" : "2");
                        if (sexDialog != null && sexDialog.isShowing()) {
                            sexDialog.dismiss();
                        }
                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        userBean.setGender(isBoy ? "1" : "2");
                        mmkv.encode(SpUtil.USER_INFO, userBean);
                        getDbInfo();
                    }
                });
    }

    private void saveNick(String nickname) {
        if (TextUtils.isEmpty(nickname) || nickname.length() < 2) {
            ToastUtils.show("Please enter a nickname from 2-10");
            return;
        }
        HttpClient.getInstance().post(AllApi.savenickname, AllApi.savenickname)
                .isMultipart(true)
                .params("nickname", nickname)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        values.put("nickname", nickname);
                        if (nickDialog != null && nickDialog.isShowing()) {
                            nickDialog.dismiss();
                        }
                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        userBean.setNickname(nickname);
                        mmkv.encode(SpUtil.USER_INFO, userBean);
                        getDbInfo();
                    }
                });
    }

    @OnClick({R.id.title, R.id.change_pic, R.id.change_username, R.id.change_sex, R.id.change_sign, R.id.change_system, R.id.crash_hc, R.id.bing_number, R.id.out_app})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title:
                break;
            case R.id.change_pic:
                showPhotoDialog();
                break;
            case R.id.change_username:
                showNickDialog();
                break;
            case R.id.change_sex:
                showSexDialog();
                break;
            case R.id.change_sign:
                startActivity(SetSignActivity.class);
                break;
            case R.id.change_system:

                break;
            case R.id.crash_hc:
                clearCh();
                break;
            case R.id.bing_number:
                ThirdLoginActivity.start(getApplicationContext(), ThirdLoginActivity.EnterIndex.OTHER);
                break;
            case R.id.out_app:
                logOut();
                break;
        }
    }

    private void logOut() {
        TipDialog dialog = TipDialog.getMyDialog(this);
        dialog.setTitle("Are you sure you want to log out？");
        String openID = userBean.getOpenid();

        //回调实现点击
        dialog.setDialogCallBack(new TipDialog.DialogCallBack() {
            @Override
            public void onActionClick() {

                HttpClient.getInstance().post(AllApi.unbindthird, AllApi.unbindthird)
                        .params("openid", openID)
                        .execute(new HttpCallback() {
                            @Override
                            public void onSuccess(int code, String msg, String[] info) {
                                //解绑后，还原imei
                                NewSplashActivity.globleMEIDStr = AppUtils.getDeviceId(NewSplashActivity.self);

                                //重新设置用户信息
                                mmkv.encode(SpUtil.USER_INFO, userBean);
                                //更换meid,客户端登录依靠meid；

                                if (!AppUtils.isRandomIMEID) {
                                    mmkv.encode(SpUtil.MEID, NewSplashActivity.globleMEIDStr);
                                }
                                //token设置为空，走重新登录流程
                                mmkv.encode(SpUtil.TOKEN, "");

                                NewSplashActivity.autoLogin("gcode");
                                //成功后，重刷用户信息
//                                getDbInfo();
//
//                                if (dialog != null) {
//                                    dialog.dismiss();
//                                }
                                //finish();

                                if (dialog != null) {
                                    dialog.dismiss();
                                }

                                finish();
                            }
                        });
            }
        });
        dialog.show();
    }

    /**
     * 清除缓存
     */
    private void clearCh() {
        final Dialog dialog = DialogUitl.loadingDialog(getContext(), getString(R.string.setting_clear_cache_ing));
        dialog.show();
        clearCache();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                getCache();
                ToastUtils.show(R.string.setting_clear_cache);
            }
        }, 2000);
    }
}
