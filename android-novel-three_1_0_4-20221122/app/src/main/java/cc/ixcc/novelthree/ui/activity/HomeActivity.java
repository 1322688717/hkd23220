package cc.ixcc.novelthree.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.bean.ActiviyNoticeInfo;
import cc.ixcc.novelthree.bean.ActiviyNoticeInfoAll;
import cc.ixcc.novelthree.bean.ClassificationTitleBean;
import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.event.CancelSelectBookEvent;
import cc.ixcc.novelthree.event.GoLibraryEvent;
import cc.ixcc.novelthree.event.SelectBookEvent;
import cc.ixcc.novelthree.event.SkipWealEvent;
import cc.ixcc.novelthree.event.SkipStackEvent;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.helper.UpdateHelper;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.interfaces.CommonCallback;
import cc.ixcc.novelthree.jsReader.model.local.ReadSettingManager;
import cc.ixcc.novelthree.jsReader.utils.SharedPreUtils;
import cc.ixcc.novelthree.other.AppConfig;
import cc.ixcc.novelthree.pay.ali.GooglePay;
import cc.ixcc.novelthree.statistics.AdjustUtil;
import cc.ixcc.novelthree.treader.AppContext;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.ThirdLoginActivity;
import cc.ixcc.novelthree.ui.activity.my.TopUpActivity;
import cc.ixcc.novelthree.ui.activity.my.vip.OpenVipActivity;
import cc.ixcc.novelthree.ui.adapter.MyFragmentPagerAdapter;
import cc.ixcc.novelthree.ui.fragment.myFragment.BookShelveFragment;
import cc.ixcc.novelthree.ui.fragment.myFragment.ClassificationFragment_new;
import cc.ixcc.novelthree.ui.fragment.myFragment.HomeStackRoomFragment;
import cc.ixcc.novelthree.ui.fragment.myFragment.MineFragment;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.helper.DoubleClickHelper;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.ui.fragment.myFragment.WealFragment;
import cc.ixcc.novelthree.utils.DialogManager;
import cc.ixcc.novelthree.utils.DiscountTimerUtil;
import cc.ixcc.novelthree.utils.SpUtil;

import com.jiusen.widget.layout.NoScrollViewPager;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.ixcc.novelthree.view.DialogView;

class TodaySignInfo {
    private String sign_type;

    public String GetSignType() {
        return sign_type;
    }
}

@BindEventBus
public final class HomeActivity extends MyActivity
        implements KeyboardWatcher.SoftKeyboardStateListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public enum FirstShowType {
        //ReadPreferencesActivity,
        SignInfo,
        ActiviyNotice
    }

    @BindView(R.id.vp_home_pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.ll_move_book)
    LinearLayout mMoveBookView;
    private MyFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> fragments;
    public MMKV mmkv = MMKV.defaultMMKV();

    public List<ActiviyNoticeInfo> ActivityList = new ArrayList<>();
    public UserBean userBean1;

    public static HomeActivity mHomeActivity;

    int lastItem = 0;

    //签到提醒
    private DialogView mShowSignView;

    //书籍推荐
    private DialogView mShowBooksView;

    //充值
    private DialogView mShowPayView;

    //VIP充值
    private DialogView mShowVipView;

    //URL运营位
    private DialogView mShowUrlView;

    public Boolean mShowActiveNotie = false;

    //标签筛选
    public List<ClassificationTitleBean> Classifications = new ArrayList<>();

    //书架界面banner运营位
    public List<ActiviyNoticeInfo> adList_shelve = new ArrayList<>();

    //福利界面banner运营位
    public List<ActiviyNoticeInfo> adList_weal = new ArrayList<>();

    //详情页界面banner运营位
    public List<ActiviyNoticeInfo> adList_detail = new ArrayList<>();

    //支付界面banner运营位
    public List<ActiviyNoticeInfo> adList_topUp = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    //TextView crash;
    @Override
    protected void initView() {
//        getkey(this);
//        //获取Gdid
//        getGAID();
        mHomeActivity = this;
        //crash.setText("test");
        //初始化谷歌支付
        GooglePay.GetInstance().Init(this);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        adjustNavigationIcoSize(mBottomNavigationView);
        setOnClickListener(R.id.ll_move_book);
        KeyboardWatcher.with(this).setListener(this);

        Constants.getInstance().setLaunched(true);

        if (fragments == null) {
            fragments = new ArrayList<>();
        } else {
            fragments.clear();
        }

        DiscountTimerUtil.getInstance().startTimer();

        fragments.add(BookShelveFragment.getInstance());
        fragments.add(HomeStackRoomFragment.getInstance());
        fragments.add(WealFragment.newInstance());
        fragments.add(MineFragment.newInstance());
        fragments.add(ClassificationFragment_new.newInstance());

        mPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(4);
        mBottomNavigationView.setSelectedItemId(R.id.menu_stack);

        //首次登录，弹提示框
        int firstTag = SharedPreUtils.getInstance().getInt(ReadSettingManager.IS_FIRST_ENTER, 0);
        if (firstTag == 0) {
            SharedPreUtils.getInstance().putInt(ReadSettingManager.IS_FIRST_ENTER, 1);
            //startActivity(ReadPreferencesActivity.class);
        } else {
            //onShowComplete(FirstShowType.ReadPreferencesActivity);
        }
        mHomeActivity.getTodaySignInfo();
    }

    public static void onShowComplete(FirstShowType type) {
//        if(type == FirstShowType.ReadPreferencesActivity)
//        {
//            //mHomeActivity.getTodaySignInfo();
//        }
//        else
//        if (type == FirstShowType.SignInfo) {
//            mHomeActivity.getActiviyNotice();
//        }
    }

    public void getTodaySignInfo() {
        HttpClient.getInstance().get(AllApi.todaysigninfo, AllApi.todaysigninfo)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        TodaySignInfo bean = new Gson().fromJson(info[0], TodaySignInfo.class);

                        if (bean.GetSignType().equals("0")) {
                            mShowActiveNotie = true;
                            //ShowDaySignNotice();
                            //GetActiviyNoticeMsg(null);
                        } else {
                            //onShowComplete(FirstShowType.SignInfo);
                        }
                        //onShowComplete(FirstShowType.SignInfo);
                    }
                });
    }

    public boolean IsPostNotice = false;

    public void getActiviyNotice() {
        if (!IsPostNotice) {
            GetActiviyNoticeMsg(() -> {
                ShowNotice();
            });
        } else {
            ShowNotice();
        }
    }

    //获取运营活动信息                 获取到运营位后的操作，无操作则传null
    public void GetActiviyNoticeMsg(Runnable target) {
        if (!IsPostNotice) {
            HttpClient.getInstance().get(AllApi.activitynotice, AllApi.activitynotice)
                    .execute(new HttpCallback() {
                        @Override
                        public void onSuccess(int code, String msg, String[] info) {
                            ActiviyNoticeInfoAll bean = new Gson().fromJson(info[0], ActiviyNoticeInfoAll.class);
                            ActivityList = bean.getList();

                            IsPostNotice = true;

                            adList_shelve.clear();
                            adList_weal.clear();
                            adList_detail.clear();
                            adList_topUp.clear();
                            for (int i = 0; i < ActivityList.size(); i++) {
                                //1-4为弹框 5-8为书架界面banner 9-12为福利界面banner 13-16为详情页banner 17-20为支付界面banner
                                if (ActivityList.get(i).getType() == 5
                                        || ActivityList.get(i).getType() == 6
                                        || ActivityList.get(i).getType() == 7
                                        || ActivityList.get(i).getType() == 8) {
                                    adList_shelve.add(ActivityList.get(i));
                                } else if (ActivityList.get(i).getType() == 9
                                        || ActivityList.get(i).getType() == 10
                                        || ActivityList.get(i).getType() == 11
                                        || ActivityList.get(i).getType() == 12) {
                                    adList_weal.add(ActivityList.get(i));
                                } else if (ActivityList.get(i).getType() == 13
                                        || ActivityList.get(i).getType() == 14
                                        || ActivityList.get(i).getType() == 15
                                        || ActivityList.get(i).getType() == 16) {
                                    adList_detail.add(ActivityList.get(i));
                                } else if (ActivityList.get(i).getType() == 17
                                        || ActivityList.get(i).getType() == 18
                                        || ActivityList.get(i).getType() == 19
                                        || ActivityList.get(i).getType() == 20) {
                                    adList_topUp.add(ActivityList.get(i));
                                }
                            }

                            if (target != null) {
                                target.run();
                            }
                        }
                    });
        }
    }

    public void ShowNotice() {
        userBean1 = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        for (int i = 0; i < ActivityList.size(); i++) {
            if (ActivityList.get(i).getType() == 1) {
                //显示图层
                ShowPayNotice();
                //判断
                ImageView img_btn = mShowPayView.findViewById(R.id.dialog_Activity);
                Glide.with(AppContext.sInstance).load(ActivityList.get(i).getUrl()).into(img_btn);
                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // AdjustUtil.GetInstance().SendBannerEvent(1);

                        if (userBean1.getBindStatus().equals("0")) {
                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                        } else {
                            DialogManager.getInstance().hide(mShowPayView);
                            startActivity(TopUpActivity.class);
                        }
                    }
                });

                ActivityList.remove(i);
                break;
            } else if (ActivityList.get(i).getType() == 2) {
                //显示图层
                ShowBooksNotice();
                ImageView img_btn = mShowBooksView.findViewById(R.id.dialog_Activity);
                Glide.with(AppContext.sInstance).load(ActivityList.get(i).getUrl()).into(img_btn);
                int finalI = i;
                int Anid = ActivityList.get(finalI).getAnid();
                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AdjustUtil.GetInstance().SendBannerEvent(1);

                        DialogManager.getInstance().hide(mShowBooksView);
                        BookDetailActivity.start(getContext(), Anid);
                    }
                });
                ActivityList.remove(i);
                break;
            } else if (ActivityList.get(i).getType() == 3) {
                //显示图层
                ShowVipNotice();
                ImageView img_btn = mShowVipView.findViewById(R.id.dialog_Activity);
                Glide.with(AppContext.sInstance).load(ActivityList.get(i).getUrl()).into(img_btn);
                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AdjustUtil.GetInstance().SendBannerEvent(1);

                        if (userBean1.getBindStatus().equals("0")) {
                            ThirdLoginActivity.start(getContext(), ThirdLoginActivity.EnterIndex.PAY);
                        } else {
                            DialogManager.getInstance().hide(mShowVipView);
                            OpenVipActivity.start(getContext());
                        }
                    }
                });
                ActivityList.remove(i);
                break;
            } else if (ActivityList.get(i).getType() == 4) {
                //显示图层
                ShowURLNotice();
                ImageView img_btn = mShowUrlView.findViewById(R.id.dialog_Activity);
                Glide.with(AppContext.sInstance).load(ActivityList.get(i).getUrl()).into(img_btn);
                int finalI = i;
                String http_url = ActivityList.get(finalI).gethttp_url();
                img_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        AdjustUtil.GetInstance().SendBannerEvent(1);

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(http_url)));
                        DialogManager.getInstance().hide(mShowUrlView);
                    }
                });
                ActivityList.remove(i);
                break;
            }
        }
    }

    //运营弹框广告位
    private void ShowBooksNotice() {
        mShowBooksView = DialogManager.getInstance().initView(this, R.layout.dialog_notice_activity);
        //显示图层
        DialogManager.getInstance().show(mShowBooksView);

        ImageView activity_close_btn = mShowBooksView.findViewById(R.id.banner_close);
        activity_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件后隐藏提示层
                DialogManager.getInstance().hide(mShowBooksView);
                mShowBooksView.dismiss();
            }
        });
        mShowBooksView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActiviyNotice();
            }
        });
    }

    //运营弹框支付
    private void ShowPayNotice() {
        mShowPayView = DialogManager.getInstance().initView(this, R.layout.dialog_notice_activity);
        //显示图层
        DialogManager.getInstance().show(mShowPayView);

        ImageView activity_close_btn = mShowPayView.findViewById(R.id.banner_close);
        activity_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件后隐藏提示层
                DialogManager.getInstance().hide(mShowPayView);
                mShowPayView.dismiss();
            }
        });
        mShowPayView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActiviyNotice();
            }
        });
    }

    //运营弹框VIP
    private void ShowVipNotice() {
        mShowVipView = DialogManager.getInstance().initView(this, R.layout.dialog_notice_activity);
        //显示图层
        DialogManager.getInstance().show(mShowVipView);

        ImageView activity_close_btn = mShowVipView.findViewById(R.id.banner_close);
        activity_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件后隐藏提示层
                DialogManager.getInstance().hide(mShowVipView);
                mShowVipView.dismiss();
            }
        });
        mShowVipView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActiviyNotice();
            }
        });
    }

    //运营弹框URL跳转
    private void ShowURLNotice() {
        mShowUrlView = DialogManager.getInstance().initView(this, R.layout.dialog_notice_activity);
        //显示图层
        DialogManager.getInstance().show(mShowUrlView);

        ImageView activity_close_btn = mShowUrlView.findViewById(R.id.banner_close);
        activity_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件后隐藏提示层
                DialogManager.getInstance().hide(mShowUrlView);
                mShowUrlView.dismiss();
            }
        });
        mShowUrlView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActiviyNotice();
            }
        });
    }

    private void ShowDaySignNotice() {
        // 每日签到提示
        mShowSignView = DialogManager.getInstance().initView(this, R.layout.sign_tips);
        //显示签到提示层
        DialogManager.getInstance().show(mShowSignView);
        //获取立即签到按钮,绑定事件
        Button sing_in_btn = mShowSignView.findViewById(R.id.sing_in_btn);
        sing_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件后隐藏提示层
                DialogManager.getInstance().hide(mShowSignView);
                //福利中心
                HomeActivity.mHomeActivity.Gotofrag_weal();
            }
        });
        mShowSignView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActiviyNotice();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HttpClient.getInstance().get(AllApi.userinfo, AllApi.userinfo)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        UserBean bean = new Gson().fromJson(info[0], UserBean.class);
                        AppContext.sInstance.setTenjinFlag(bean.getCopyright());
                        Log.e("EventBus", "EventBus已经发送Copyright");
                        EventBus.getDefault().postSticky(String.valueOf(bean.getCopyright()));
//                        Log.e("HomeActivity", String.valueOf(bean.getCopyright()));
                    }
                });
    }

    //TODO 先初始化View 然后加载数据
    @Override
    protected void initData() {
//        TenjinSDK instance = TenjinSDK.getInstance(this, "HKZXUSKGYMSF3PRHSVJYZYJNGE5WK3ZF");
//        instance.eventWithNameAndValue("item", 300);

//        getAppInfo(new CommonCallback<ConfigBean>() {
//            @Override
//            public void callback(ConfigBean bean) {
//                //BookShelveFragment fragment = (BookShelveFragment) fragments.get(0);
//
//                if (Integer.valueOf(bean.getAndroid_version_code()).intValue() > AppConfig.getVersionCode()) {
//                    UpdateHelper updateHelper = new UpdateHelper(getContext());
//                    updateHelper.updateApp(bean);
//                }
//            }
//        });
    }

//    /**
//     * {@link BottomNavigationView.OnNavigationItemSelectedListener}
//     */
//    private void getAppInfo(final CommonCallback<ConfigBean> commonCallback) {
//        HttpClient.getInstance().post(AllApi.appinit, AllApi.appinit)
//                .execute(new HttpCallback() {
//                    @Override
//                    public void onSuccess(int code, String msg, String[] info) {
//                        ConfigBean config = new Gson().fromJson(info[0], ConfigBean.class);
//                        Constants.getInstance().setConfig(config);
//
//                        MMKV.defaultMMKV().encode(SpUtil.CONFIG, config);
//                        if (commonCallback != null) {
//                            commonCallback.callback(config);
//                        }
//                    }
//                });
//    }

    private void adjustNavigationIcoSize(BottomNavigationView navigation) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    //福利界面
    public void Gotofrag_weal() {
        lastItem = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(2);
    }

    //分类界面
    public void Gotofrag_Classification(int position) {
        try {
            lastItem = mViewPager.getCurrentItem();
            SetSelect_ClassificationTitleBean(position);
            mViewPager.setCurrentItem(4);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //刷新分类界面标签
    public void RefreshClassification_title(String str) {
        ClassificationFragment_new classificationFragment_new = (ClassificationFragment_new) fragments.get(4);
        classificationFragment_new.RefreshView(str);
    }

    //设置分类界面选择的分类
    public void SetSelect_ClassificationTitleBean(int position) {

        ClassificationFragment_new classificationFragment_new = (ClassificationFragment_new) fragments.get(4);

        classificationFragment_new.SetSelect_ClassificationTitleBean(position);
    }

    public void GotoLastfrag() {
        mViewPager.setCurrentItem(lastItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_shelve:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.menu_stack:
                if (IsPostNotice) {
                    //todo
                    //getActiviyNotice();
                }
                mViewPager.setCurrentItem(1);
                return true;
//            case R.id.menu_fuli:
//                mViewPager.setCurrentItem(2);
//                return true;
            case R.id.menu_mine:
                mViewPager.setCurrentItem(3);
                return true;
            default:
                break;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SelectBookEvent e) {
        mBottomNavigationView.setVisibility(View.GONE);
        mMoveBookView.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent2(CancelSelectBookEvent e) {
        mBottomNavigationView.setVisibility(View.VISIBLE);
        mMoveBookView.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLibrary(GoLibraryEvent e) {
        if (mBottomNavigationView != null) {
            mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(1).getItemId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSkipEvent(SkipStackEvent e) {
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(1).getItemId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSkipEvent(SkipWealEvent e) {
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(2).getItemId());
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        if (mViewPager.getCurrentItem() == 2 || mViewPager.getCurrentItem() == 4) {
            GotoLastfrag();
        } else {
            if (DoubleClickHelper.isOnDoubleClick()) {
                // 移动到上一个任务栈，避免侧滑引起的不良反应
                moveTaskToBack(false);

//                postDelayed(() -> {
//                    // 进行内存优化，销毁掉所有的界面
//                    ActivityStackManager.getInstance().finishAllActivities();
//                }, 300);
            } else {
                toast(R.string.home_exit_hint);
            }
        }
    }

    private void finishApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_move_book:
                BookShelveFragment.getInstance().removeShelve();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        EventBus.getDefault().unregister(this);
        Constants.getInstance().setLaunched(false);
        super.onDestroy();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    public void getkey(Activity activity) {

        try {

            PackageInfo info = activity.getPackageManager().getPackageInfo("cc.ixcc.noveltwo", PackageManager.GET_SIGNATURES);

            for (Signature sign : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(sign.toByteArray());

                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

            Log.e("KeyHash:", "error " + e.toString());

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

            Log.e("KeyHash:", "error " + e.toString());

        }

    }


//    //获取 GAID
//    public String getGAID() {
//        String gaid = "";
//        AdvertisingIdClient.Info adInfo = null;
//        try {
//            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(this);
//        } catch (IOException e) {
//            // Unrecoverable error connecting to Google Play services (e.g.,
//            // the old version of the service doesn't support getting AdvertisingId).
//            Log.e("getGAID", "IOException");
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // Google Play services is not available entirely.
//            Log.e("getGAID", "GooglePlayServicesNotAvailableException");
//        } catch (Exception e) {
//            Log.e("getGAID", "Exception:" + e.toString());
//            // Encountered a recoverable error connecting to Google Play services.
//        }
//        if (adInfo != null) {
//            gaid = adInfo.getId();
//            Log.w("getGAID", "gaid:" + gaid);
//        }
//        return gaid;
//    }
}