package cc.ixcc.novelthree.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import cc.ixcc.novelthree.R;

import java.io.File;

import cc.ixcc.novelthree.bean.ConfigBean;
import cc.ixcc.novelthree.utils.DialogManager;
import cc.ixcc.novelthree.view.DialogView;


/**
 * FileName: UpdateHelper
 * Founder: LiuGuiLin
 * Profile: App更新帮助类
 * versionCode +1
 */
public class UpdateHelper {

    private Context mContext;

    private DialogView mUpdateView;
    private TextView tv_desc;
    private TextView updataversion_name;
    private TextView tv_confirm;

    private ProgressDialog mProgressDialog;

    public UpdateHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void updateApp(ConfigBean appVersionModel) {
        createUpdateDialog(appVersionModel);
    }

    /**
     * 更新提示框
     */
    private void createUpdateDialog(final ConfigBean appVersionModel) {
        mUpdateView = DialogManager.getInstance().initView(mContext, R.layout.dialog_update_version);
        tv_desc = mUpdateView.findViewById(R.id.tv_update_desc);
        updataversion_name = mUpdateView.findViewById(R.id.updataversion_name);
        tv_confirm = mUpdateView.findViewById(R.id.tv_confirm);
        tv_desc.setText(appVersionModel.getAndroid_version_desc());
        updataversion_name.setText(appVersionModel.getAndroid_version());
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(appVersionModel.getAndroid_download_page());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        // 强制更新
        if (appVersionModel.getAndroid_version_must().equals("1")) {
            mUpdateView.setCanceledOnTouchOutside(false);
        }
        DialogManager.getInstance().show(mUpdateView);
        initProgress();
    }

    private void initProgress() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
    }

    /**
     *
     * @param path 文件夹路径
     */
    public static void isExist(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
