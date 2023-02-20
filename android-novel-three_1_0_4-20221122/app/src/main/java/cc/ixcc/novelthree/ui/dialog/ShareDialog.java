//package cc.ixcc.novelthree.ui.dialog;
//
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.DrawableRes;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import cc.ixcc.novelthree.treader.Config;
//import com.jiusen.base.BaseAdapter;
//import com.jiusen.base.BaseDialog;
//import com.yixuan.xiaosuojia.R;
//import cc.ixcc.novelthree.common.MyAdapter;
//import com.hjq.toast.ToastUtils;
////import com.jiusen.umeng.Platform;
////import com.jiusen.umeng.UmengClient;
////import com.jiusen.umeng.UmengShare;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * time   : 2019/03/23
// * desc   : 分享对话框
// */
//public final class ShareDialog {
//    private static Config config;
//
//    public static final class Builder
//            extends BaseDialog.Builder<Builder>
//            implements BaseAdapter.OnItemClickListener {
//
//        private final ShareAdapter mAdapter;
//
////        private final UmengShare.ShareData mData;
////        private UmengShare.OnShareListener mListener;
//
//        private LinearLayout layout;
//        private TextView cancel;
//        private View line;
//
//        public Builder(Context context) {
//            super(context);
//
//            setContentView(R.layout.dialog_share);
//            config = Config.getInstance();
//
//            final List<ShareBean> data = new ArrayList<>();
//            data.add(new ShareBean(config.getDayOrNight() ? getDrawable(R.mipmap.icon_wx_night) : getDrawable(R.mipmap.icon_wx), getString(R.string.share_platform_wechat), Platform.WECHAT));
//            data.add(new ShareBean(config.getDayOrNight() ? getDrawable(R.mipmap.icon_wx_py_night) : getDrawable(R.mipmap.icon_wx_py), getString(R.string.share_platform_moment), Platform.CIRCLE));
//            data.add(new ShareBean(config.getDayOrNight() ? getDrawable(R.mipmap.icon_qq_night) : getDrawable(R.mipmap.icon_qq), getString(R.string.share_platform_qq), Platform.QQ));
//            data.add(new ShareBean(config.getDayOrNight() ? getDrawable(R.mipmap.icon_qq_zone_night) : getDrawable(R.mipmap.icon_qq_zone), getString(R.string.share_platform_qzone), Platform.QZONE));
////            data.add(new ShareBean(config.getDayOrNight() ? getDrawable(R.mipmap.icon_wb_night) : getDrawable(R.mipmap.icon_wb), getString(R.string.share_platform_weibo), Platform.QWEIBO));
//
//            mAdapter = new ShareAdapter(context);
//            mAdapter.setData(data);
//            mAdapter.setOnItemClickListener(this);
//
//            layout = findViewById(R.id.layout);
//            cancel = findViewById(R.id.tv_cancel);
//            line = findViewById(R.id.line);
//            cancel.setOnClickListener(view -> {
//                dismiss();
//            });
//
//            RecyclerView recyclerView = findViewById(R.id.rv_share_list);
//            recyclerView.setLayoutManager(new GridLayoutManager(context, data.size()));
//            recyclerView.setAdapter(mAdapter);
//
//            mData = new UmengShare.ShareData(context);
//            initDayOrNight();
//        }
//
//        private void initDayOrNight() {
//            if (config.getDayOrNight()) {
//                setBgColor(Config.BOOK_BG_NIGHT);
//            } else {
//                setBgColor(config.getBookBgType());
//            }
//            cancel.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
//            line.setBackground(config.getDayOrNight() ? getDrawable(R.color.read_font_night) : getDrawable(R.color.line));
//        }
//
//        //设置页面的背景
//        public void setBgColor(int type) {
//            int color = 0;
//            switch (type) {
//                case Config.BOOK_BG_NIGHT:
//                    color = config.isHuyan() ? R.color.day_night_huyan : R.color.day_night;
//                    break;
//                case Config.BOOK_BG_DEFAULT:
//                    color = config.isHuyan() ? R.color.read_bg_default_huyan : R.color.read_bg_default;
//                    break;
//                case Config.BOOK_BG_1:
//                    color = config.isHuyan() ? R.color.read_bg_1_huyan : R.color.read_bg_1;
//                    break;
//                case Config.BOOK_BG_2:
//                    color = config.isHuyan() ? R.color.read_bg_2_huyan : R.color.read_bg_2;
//                    break;
//                case Config.BOOK_BG_3:
//                    color = config.isHuyan() ? R.color.read_bg_3_huyan : R.color.read_bg_3;
//                    break;
//                case Config.BOOK_BG_4:
//                    color = config.isHuyan() ? R.color.read_bg_4_huyan : R.color.read_bg_4;
//                    break;
//            }
//            layout.setBackground(getContext().getResources().getDrawable(color));
//        }
//
//        public Builder setShareTitle(String title) {
//            mData.setShareTitle(title);
//            return this;
//        }
//
//        public Builder setShareDescription(String description) {
//            mData.setShareDescription(description);
//            return this;
//        }
//
//        public Builder setShareLogo(String url) {
//            mData.setShareLogo(url);
//            return this;
//        }
//
//        public Builder setShareLogo(@DrawableRes int id) {
//            mData.setShareLogo(id);
//            return this;
//        }
//
//        public Builder setShareUrl(String url) {
//            mData.setShareUrl(url);
//            return this;
//        }
//
//        public Builder setListener(UmengShare.OnShareListener listener) {
//            mListener = listener;
//            return this;
//        }
//
//        /**
//         * {@link BaseAdapter.OnItemClickListener}
//         */
//        @Override
//        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
//            Platform platform = mAdapter.getItem(position).getSharePlatform();
//            if (platform != null) {
//                UmengClient.share(getActivity(), platform, mData, mListener);
//            } else {
//                // 复制到剪贴板
//                getSystemService(ClipboardManager.class).setPrimaryClip(ClipData.newPlainText("url", mData.getShareUrl()));
//                ToastUtils.show(R.string.share_platform_copy_hint);
//            }
//            dismiss();
//        }
//    }
//
//    private static class ShareAdapter extends MyAdapter<ShareBean, RecyclerView.ViewHolder> {
//
//        private ShareAdapter(Context context) {
//            super(context);
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return new ViewHolder();
//        }
//
//        final class ViewHolder extends MyAdapter.ViewHolder {
//
//            private final ImageView mImageView;
//            private final TextView mTextView;
//
//            private ViewHolder() {
//                super(R.layout.item_share);
//                mImageView = (ImageView) findViewById(R.id.iv_share_image);
//                mTextView = (TextView) findViewById(R.id.tv_share_text);
//            }
//
//            @Override
//            public void onBindView(int position) {
//                ShareBean bean = getItem(position);
//                mImageView.setImageDrawable(bean.getShareIcon());
//                mTextView.setText(bean.getShareName());
//                mTextView.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
//            }
//        }
//    }
//
//    private static class ShareBean {
//
//        /**
//         * 分享图标
//         */
//        private final Drawable mShareIcon;
//        /**
//         * 分享名称
//         */
//        private final String mShareName;
//        /**
//         * 分享平台
//         */
//        private final Platform mSharePlatform;
//
//        private ShareBean(Drawable icon, String name, Platform platform) {
//            mShareIcon = icon;
//            mShareName = name;
//            mSharePlatform = platform;
//        }
//
//        private Drawable getShareIcon() {
//            return mShareIcon;
//        }
//
//        private String getShareName() {
//            return mShareName;
//        }
//
//        private Platform getSharePlatform() {
//            return mSharePlatform;
//        }
//    }
//}