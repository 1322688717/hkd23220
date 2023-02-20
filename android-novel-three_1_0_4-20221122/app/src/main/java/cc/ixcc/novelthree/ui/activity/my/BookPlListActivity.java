package cc.ixcc.novelthree.ui.activity.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.BookPl;
import cc.ixcc.novelthree.bean.CommentBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.event.BookLikeEvent;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.ui.adapter.myAdapter.BookPlListAdapter;
import cc.ixcc.novelthree.utils.StarBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static cc.ixcc.novelthree.Constants.COMMENT_NEW;
import static cc.ixcc.novelthree.Constants.COMMENT_RECOMMEND;
import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

/**
 * 书籍评价列表
 */
public class BookPlListActivity extends MyActivity {
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.book_img)
    ImageView bookImg;
    @BindView(R.id.book_name)
    TextView bookName;
    @BindView(R.id.go_read)
    TextView goRead;
    @BindView(R.id.book_status)
    TextView bookStatus;
    @BindView(R.id.starBar)
    StarBar starBar;
    @BindView(R.id.tj_pl)
    TextView tjPl;
    @BindView(R.id.new_pl)
    TextView newPl;
    @BindView(R.id.rv_pllist)
    RecyclerView rvPllist;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private BookPlListAdapter adapter;
    List<BookPl> mlist;
    int page = 1;
    String type = COMMENT_RECOMMEND;
    int anime_id;
    CommentBean mCommentBean;
    List<CommentBean.ListBean> mCommentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_pl_list;
    }

    public static void start(Context context, int anime_id) {
        Intent intent = new Intent(context, BookPlListActivity.class);
        intent.putExtra("anime_id", anime_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void getInfo() {
        HttpClient.getInstance().get(AllApi.commentlist, AllApi.commentlist)
            .params("anime_id", anime_id)
            .params("page", page)
            .params("page_size", PAGE_SIZE)
            .params("type", type)
            .execute(new HttpCallback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    mCommentBean = new Gson().fromJson(info[0], CommentBean.class);
                    if (page == 1) {
                        mCommentList.clear();
                    }
                    mCommentList.addAll(mCommentBean.getList());
                    setView();
                    if (page == 1) {
                        refresh.finishRefresh(500);
                    }
                    else {
                        refresh.finishLoadMore(500);
                    }
                }
            });
    }

    private void setView() {
        if (mCommentBean == null) return;
        bookName.setText(mCommentBean.getTitle());
        Glide.with(getContext()).load(mCommentBean.getCoverpic()).into(bookImg);
        //设置星星为整数评分
        starBar.setIntegerMark(true);
        //设置显示的评分等级  默认为1
        starBar.setStarMark(mCommentBean.getAverage_star());
        starBar.setSelected(false);
        getScore();
        score.setText(mCommentBean.getTotal_score_person() + " reviews ");
        adapter.setData(mCommentList);
        adapter.notifyDataSetChanged();
    }

    private void getScore() {
        int starBarNum = (int) starBar.getStarMark();
        if (starBarNum == 1) {
            bookStatus.setText("2.0");
        } else if (starBarNum == 2) {
            bookStatus.setText("4.0");
        } else if (starBarNum == 3) {
            bookStatus.setText("6.0");
        } else if (starBarNum == 4) {
            bookStatus.setText("8.0");
        } else if (starBarNum == 5) {
            bookStatus.setText("10.0");
        }
    }

    @Override
    protected void initView() {
        anime_id = getIntent().getIntExtra("anime_id", 0);
        //默认显示推荐书评
        initCommentView();
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++page;
                getInfo();
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getInfo();
            }
        });
    }

    private void commentLike(CommentBean.ListBean bean) {
        HttpClient.getInstance().post(AllApi.commentlike, AllApi.commentlike)
            .params("comment_id", bean.getId())
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    getInfo();
                    toast(msg);
                }
            });
    }

    private void cancelLike(CommentBean.ListBean bean) {
        HttpClient.getInstance().post(AllApi.cancellike, AllApi.cancellike)
            .params("comment_id", bean.getId())
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    getInfo();
                    toast(msg);
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }

    private CommentBean.ListBean mBean;

    private void initCommentView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置方向
        rvPllist.setLayoutManager(layoutManager);
        adapter = new BookPlListAdapter(getContext());
        adapter.setOnClickListener(new BookPlListAdapter.OnClickListener() {
            @Override
            public void onActionClick(CommentBean.ListBean bean) {
                //跳转评论详情
                mBean = bean;
                BookPlDetailActivity.start(getContext(), bean.getId());
            }

            @Override
            public void onLikeClick(CommentBean.ListBean bean) {
                if (bean.getIs_admire().equals(Constants.LIKE)) {
                    cancelLike(bean);
                } else {
                    commentLike(bean);
                }
            }
        });

        rvPllist.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLikeEvent(BookLikeEvent e) {
        if (mBean == null) return;
        mBean.setIs_admire(e.getIs_admire());
        mBean.setLikes(e.getIs_admire().equals(Constants.LIKE) ? mBean.getLikes() + 1 : mBean.getLikes() - 1);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.tj_pl, R.id.new_pl})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tj_pl:
                //推荐书评
                tjPl.setTextColor(BookPlListActivity.this.getResources().getColor(R.color.colorTitle));
                newPl.setTextColor(BookPlListActivity.this.getResources().getColor(R.color.textColorHint));
                //设置textView选中时候的风格
                tjPl.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                newPl.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                type = COMMENT_RECOMMEND;
                getInfo();
                break;
            case R.id.new_pl:
                //最新书评
                tjPl.setTextColor(BookPlListActivity.this.getResources().getColor(R.color.textColorHint));
                newPl.setTextColor(BookPlListActivity.this.getResources().getColor(R.color.colorTitle));
                tjPl.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                newPl.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                type = COMMENT_NEW;
                getInfo();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.rv_pllist, R.id.comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_pllist:
                break;
            case R.id.comment:
                EvaluateBookActivity.start(getContext(), anime_id);
                break;
        }
    }
}
