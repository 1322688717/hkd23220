package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;
import cc.ixcc.novelthree.R;

import java.util.Arrays;

import butterknife.BindView;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.aop.SingleClick;
import cc.ixcc.novelthree.bean.BindThirdBean;
import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.utils.ToastUtils;
import cc.ixcc.novelthree.ui.activity.NewSplashActivity;
import cc.ixcc.novelthree.utils.SpUtil;

/**
 * desc   : 登录界面
 */

public final class ThirdLoginActivity extends MyActivity {
    //facebook
    private MMKV mmkv = MMKV.defaultMMKV();
    private static EnterIndex mEnterIndex;
    private ThirdType mCurThirdType = ThirdType.FACE_BOOK;
    private CallbackManager mCallbackManager;
    private String FACEBOOK_TAG = "FaceBook Login";

    //google
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String web_client_id = "561325189662-0eatp337ncmaph8klk2gnrb1iq18rm32.apps.googleusercontent.com";

    private GoogleSignInClient mGoogleSignInClient;

    //提示文本
    @BindView(R.id.tiptext)
    TextView tiptext;

    public enum EnterIndex {
        VIP,
        PAY,
        LOGIN_NOTICE,
        PHONE_BIND,
        BIND_THIRD,
        OTHER
    }

    public enum ThirdType{
        GOOGLE,
        FACE_BOOK
    }

    @DebugLog
    public static void start(Context context, EnterIndex enterIndex) {
        mEnterIndex = enterIndex;
        Intent intent = new Intent(context, ThirdLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weixin_login;
    }

    @Override
    protected void initView() {
        mCurThirdType = ThirdType.FACE_BOOK;
        //事件注册
        setOnClickListener(R.id.btn_google);

        //facebook
        mCallbackManager = CallbackManager.Factory.create();
        ConstraintLayout login = findViewById(R.id.facebook);
        LoginButton loginButton = findViewById(R.id.btn_facebook);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(ThirdLoginActivity.this,Arrays.asList("public_profile", "basic_info"));
            }
        });

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FACEBOOK_TAG, "facebook:onSuccess:" + loginResult);

                try {
                    Profile profile = Profile.getCurrentProfile();
                    String picUrl = profile.getProfilePictureUri(150, 150).toString();
                    onThirdBind(AccessToken.getCurrentAccessToken().getUserId(), profile.getName(), picUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                Log.d(FACEBOOK_TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FACEBOOK_TAG, "facebook:onError", error);
            }
        });
        loginButton.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(ThirdLoginActivity.this, Arrays.asList("public_profile", "basic_info")));

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FACEBOOK_TAG, "facebook:onSuccess:" + loginResult);

                try {
                    Profile profile = Profile.getCurrentProfile();
                    String picUrl = profile.getProfilePictureUri(150, 150).toString();
                    onThirdBind(AccessToken.getCurrentAccessToken().getUserId(), profile.getName(), picUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                Log.d(FACEBOOK_TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FACEBOOK_TAG, "facebook:onError", error);
            }
        });

        RefreshTip();
    }

    public void RefreshTip() {
        HttpClient.getInstance().post(AllApi.appinit, AllApi.appinit)
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    ConfigBean config = new Gson().fromJson(info[0], ConfigBean.class);
                    Constants.getInstance().setConfig(config);

                    mmkv.encode(SpUtil.CONFIG, config);

                    // 不需要点击的文字
                    tiptext.setText("By creating an account, I accept A novel for a life's ");
                    // 设置需要点击的文字
                    SpannableString clickString1 = new SpannableString("Terms of Services");
                    // 设置需要点击文字的样式
                    clickString1.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            //用户协议
                            WebViewActivity.forward(getContext(), config.getUser_agreement(), "Terms of Service", false);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            // 设置可点击文字的颜色
                            ds.setColor(getColor(R.color.colorButtonDefault)); //设置颜色
                        }
                        // 0-->clickString1.length()这个长度就是需要点击的文字长度
                    }, 0, clickString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 将需要点击的文字添加到我们的TextView中
                    tiptext.append(clickString1);
                    // 继续添加不需要点击的文字组装TextView
                    tiptext.append(new SpannableString(" and "));
                    // 设置需要点击的文字
                    SpannableString clickString2 = new SpannableString("Privacy Policy");
                    // 设置需要点击文字的样式
                    clickString2.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            //隐私政策
                            WebViewActivity.forward(getContext(), config.getUser_terms(), "Privacy policy", false);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            // 设置可点击文字的颜色
                            ds.setColor(getColor(R.color.colorButtonDefault)); //设置颜色
                        }
                        // 0-->clickString1.length()这个长度就是需要点击的文字长度
                    }, 0, clickString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 将需要点击的文字添加到我们的TextView中
                    tiptext.append(clickString2);
                    // 继续添加不需要点击的文字组装TextView
                    tiptext.append(new SpannableString("."));
                    // 设置点击文字点击效果为透明
                    tiptext.setHighlightColor(Color.TRANSPARENT);
                    // 开始响应点击事件
                    tiptext.setMovementMethod(LinkMovementMethod.getInstance());
                }
            });
    }

    @Override
    protected void initData() {
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google: {
                mCurThirdType = ThirdType.GOOGLE;
                GoogleSignInOptions gso = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(web_client_id)
                        .requestEmail()
                        .build();
                Log.e(TAG,"gso==="+gso);
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                Log.e(TAG,"mGoogleSignInClient==="+mGoogleSignInClient);
                //走登录流程
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                Log.e(TAG,"signInIntent==="+signInIntent);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"data==="+data);
        if(mCurThirdType == ThirdType.FACE_BOOK)
        {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
        else
        {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

                    //检索登录用户的个人资料信息
                    if (account != null) {
                        String personName = account.getDisplayName();
                        String personGivenName = account.getGivenName();
                        String personFamilyName = account.getFamilyName();
                        String personEmail = account.getEmail();
                        String personId = account.getId();
                        Uri personPhoto = account.getPhotoUrl();
                        Log.d(TAG, "onStart: ---- personName is : " + personName +
                                "\n personGivenName is : " + personGivenName +
                                "\n personFamilyName is : " + personFamilyName +
                                "\n personEmail is : " + personEmail +
                                "\n personId is : " + personId +
                                "\n personPhoto is : " + personPhoto);
                        signOut();

                        onThirdBind(account.getId(), account.getDisplayName(), account.getPhotoUrl().toString());
                    }
                }
                catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }
            }
        }

        mCurThirdType =ThirdType.FACE_BOOK;
    }

    //登录
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "signOut: -----------------------------");
                // ...
            }
        });
    }

    public void onThirdBind(String openid, String nickName, String avatar) {
        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        String tempMEID = userBean.getMeid();

        HttpClient.getInstance().post(AllApi.bindthird, AllApi.bindthird)
            .isMultipart(true)
            .params("openid", openid)
            .params("meid", tempMEID)
            .params("nickname", nickName)
            .params("avatar", avatar)
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if (code == 200) {
                        BindThirdBean bean = new Gson().fromJson(info[0], BindThirdBean.class);
                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        userBean.setNickname(bean.getNickname());
                        userBean.setAvatar(bean.getAvatar());
                        userBean.setOpenid(bean.getOpenID());
                        userBean.setId(bean.getUser_id());
                        userBean.setBindStatus("1");

                        //重新设置用户信息
                        mmkv.encode(SpUtil.USER_INFO, userBean);
                        //更换meid,客户端登录依靠meid；
                        mmkv.encode(SpUtil.MEID, bean.getMEID());
                        //token设置为空，走重新登录流程
                        mmkv.encode(SpUtil.TOKEN, "");

                        NewSplashActivity.autoLogin("gcode");

                        finish();
                        ToastUtils.show("Authentication success!");
                    }
                }
            });
    }
}