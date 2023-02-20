package cc.ixcc.novelthree.ui.activity.my;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.utils.SpUtil;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;

/**
 * 设置签名
 */

public class SetSignActivity extends MyActivity {
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.sign)
    EditText sign;
    @BindView(R.id.size)
    TextView size;
    int maxsize = 100;
    ContentValues values = new ContentValues();
    private MMKV mmkv = MMKV.defaultMMKV();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_sign;
    }

    @Override
    protected void initView() {
        getDbInfo();
        sign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = sign.getText();
                int len = editable.length();
                //显示还可以输入多少字
                size.setText(len + "/" + maxsize);
                if (len > maxsize) {
                    ToastUtils.show("Exceeded word limit");
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxsize);
                    sign.setText(newStr);
                    editable = sign.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        saveSign();
    }

    private void saveSign() {
        String dbid = mmkv.decodeString(SpUtil.DBUID, "0");
        String signtext = sign.getText().toString();
        if (TextUtils.isEmpty(signtext)) {
            ToastUtils.show("Please set your personalized signature");
            return;
        }
        HttpClient.getInstance().post(AllApi.savesign, AllApi.savesign)
                .isMultipart(true)
                .params("sign", signtext)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        values.put("sign", signtext);
                        UserBean userBean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
                        userBean.setSign(signtext);
                        mmkv.encode(SpUtil.USER_INFO,userBean);
                        finish();
                    }
                });
    }

    private void getDbInfo() {
//        String dbid = mmkv.decodeString(SpUtil.DBUID, "0");
//        List<MyUserBean> userLists = DataSupport.findAll(MyUserBean.class);
//        for (int i = 0; i < userLists.size(); i++) {
//            MyUserBean myUserBean = userLists.get(i);
//            if (String.valueOf(myUserBean.getId()).equals(dbid)) {
//                sign.setText(myUserBean.getSign());
//                sign.setSelection(sign.length());
//                size.setText(sign.length() + "/" + maxsize);
//            }
//        }
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        sign.setText(bean.getSign());
        sign.setSelection(sign.length());
        size.setText(sign.length() + "/" + maxsize);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
