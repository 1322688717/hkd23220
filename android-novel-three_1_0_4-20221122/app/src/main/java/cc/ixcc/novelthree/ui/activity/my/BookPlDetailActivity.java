package cc.ixcc.novelthree.ui.activity.my;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.DetailCommentBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.utils.StarBar;

import butterknife.BindView;

/**
 * 书评详情
 */
public class BookPlDetailActivity extends MyActivity {
    int comment_id;
    DetailCommentBean mCommentBean;
    @BindView(R.id.pl_userImg)
    ImageView plUserImg;
    @BindView(R.id.pl_username)
    TextView plUsername;
    @BindView(R.id.pl_status)
    TextView plStatus;
    @BindView(R.id.starBar)
    StarBar starBar;
    @BindView(R.id.pl_content)
    TextView plContent;
    @BindView(R.id.pl_time)
    TextView plTime;
    @BindView(R.id.like)
    TextView like;
    @BindView(R.id.vip_img)
    ImageView vipImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_pl_detail;
    }

    public static void start(Context context, int comment_id) {
        Intent intent = new Intent(context, BookPlDetailActivity.class);
        intent.putExtra("comment_id", comment_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        comment_id = getIntent().getIntExtra("comment_id", 0);
        getInfo();
        like.setOnClickListener(view -> {
            if (mCommentBean.getIs_admire().equals(Constants.LIKE)) {
                cancelLike(mCommentBean);
            } else {
                commentLike(mCommentBean);
            }
        });
    }

    private void getInfo() {
        HttpClient.getInstance().get(AllApi.commentdetail, AllApi.commentdetail)
                .params("comment_id", comment_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        mCommentBean = new Gson().fromJson(info[0], DetailCommentBean.class);
                        setView();
                    }
                });
    }

    private void setView() {
        if (mCommentBean == null) return;
        plUsername.setText(mCommentBean.getNickname());
        Glide.with(getContext())
                .load(mCommentBean.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(plUserImg);
        starBar.setStarMark(mCommentBean.getStar());
        plContent.setText(mCommentBean.getContent());
        plTime.setText(mCommentBean.getUpdated_at());
        like.setText(mCommentBean.getLikes() + "");

        if (TextUtils.equals(mCommentBean.getIs_vip(),"1")) {
            vipImg.setVisibility(View.VISIBLE);
            vipImg.setImageResource(R.mipmap.mine_vip_sign);
        } else {
            vipImg.setVisibility(View.GONE);
        }

        getScore();
        setDrawable(like, mCommentBean.getIs_admire().equals(Constants.LIKE) ? getDrawable(R.mipmap.dz) : getDrawable(R.mipmap.dz_default));
        like.setTextColor(mCommentBean.getIs_admire().equals(Constants.LIKE) ? getColor(R.color.dz) : getColor(R.color.textColorHint));
    }

    private Drawable setDrawable(TextView textView, Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        return drawable;
    }

    @Override
    protected void initData() {

    }

    private void getScore() {
        int starBarNum = (int) starBar.getStarMark();
        if (starBarNum == 1) {
            plStatus.setText("2.0");
        } else if (starBarNum == 2) {
            plStatus.setText("4.0");
        } else if (starBarNum == 3) {
            plStatus.setText("6.0");
        } else if (starBarNum == 4) {
            plStatus.setText("8.0");
        } else if (starBarNum == 5) {
            plStatus.setText("10.0");
        }
    }

    private void commentLike(DetailCommentBean bean) {
        HttpClient.getInstance().post(AllApi.commentlike, AllApi.commentlike)
                .params("comment_id", bean.getId())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        bean.setIs_admire(Constants.LIKE);
//                        bean.setLikes(bean.getLikes() + 1);
//                        setView();
                        getInfo();
//                        EventBus.getDefault().post(new BookLikeEvent(Constants.LIKE));
                        toast(msg);
                    }
                });
    }

    private void cancelLike(DetailCommentBean bean) {
        HttpClient.getInstance().post(AllApi.cancellike, AllApi.cancellike)
                .params("comment_id", bean.getId())
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        bean.setIs_admire(Constants.UNLIKE);
//                        bean.setLikes(bean.getLikes() - 1);
//                        setView();
                        getInfo();
//                        EventBus.getDefault().post(new BookLikeEvent(Constants.UNLIKE));
                        toast(msg);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
