package cc.ixcc.novelthree.widget.viewhoder;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.StackRoomBookBean;
import cc.ixcc.novelthree.ui.activity.my.BookDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.BookMoreActivity;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomSuggestAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.StackRoomBookAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import cc.ixcc.novelthree.utils.WordUtil;

import static cc.ixcc.novelthree.Constants.MORE_VISIBLE;
import static cc.ixcc.novelthree.utils.WordUtil.getString;

public class StackRoomModeHolder9 extends AbsViewHolder {
    protected Context mContext;
    String mTitleStr;
    StackRoomSuggestAdapter mHotAdapter;
    StackRoomBookAdapter mHotAdapter2;
    @BindView(R.id.mode9_title)
    TextView mode9Title;
    @BindView(R.id.mode9_more)
    TextView mode9More;
    @BindView(R.id.mode9_content)
    TextView mode9Content;
    @BindView(R.id.mode9_img)
    RoundedImageView mode9Img;
    @BindView(R.id.mode9_name)
    TextView mode9Name;
    @BindView(R.id.mode9_author)
    TextView mode9Author;
    @BindView(R.id.mode9_pingfen)
    TextView mode9Pingfen;
    @BindView(R.id.mode9_fen)
    TextView mode9Fen;
    @BindView(R.id.mode9_type)
    TextView mode9Type;
    @BindView(R.id.mode9_lianzai)
    TextView mode9Lianzai;
    @BindView(R.id.rv_mode9_1)
    WrapRecyclerView rvMode91;
    @BindView(R.id.rv_mode9_2)
    WrapRecyclerView rvMode92;
    StackRoomBookBean.ColumnBean mBean;
    List<StackRoomBookBean.ColumnBean.ListBean> mList;
    List<StackRoomBookBean.ColumnBean.ListBean> mNewList;
    boolean hasother = true;
    @BindView(R.id.book)
    LinearLayout book;

    public StackRoomModeHolder9(Context context, StackRoomBookBean.ColumnBean bean, ViewGroup parentView) {
        super(context, parentView);
        mContext = context;
        mBean = bean;
        mTitleStr = bean.getTitle();
        mList = bean.getList();
        init2();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_stack_mode9;
    }

    @Override
    public void init() {
    }

    public void init2() {
        if (mList.size() > 0) {
            StackRoomBookBean.ColumnBean.ListBean bean = mList.get(0);
            Glide.with(mContext)
//                    .load(getItem(position).getImgurl())
                    .load(R.mipmap.book_cover_def)
                    .into(mode9Img);
            mode9Name.setText(bean.getTitle());
            mode9Type.setText(bean.getClassify().name);
            mode9Type.setVisibility(TextUtils.isEmpty(bean.getClassify().name) ? View.GONE : View.VISIBLE);
            mode9Pingfen.setVisibility(bean.getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            mode9Fen.setVisibility(bean.getAverage_score() > 0 ? View.VISIBLE : View.GONE);
            mode9Pingfen.setText(bean.getAverage_score() + "");
            mode9Author.setText(bean.getAuthor());
            mode9Content.setText(bean.getDesc());
            mode9Lianzai.setText(bean.getIswz() == Constants.lianzai ? WordUtil.getString(R.string.lianzai) : WordUtil.getString(R.string.wanjie));
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.start(mContext, bean.getId());
                }
            });
        }
        initHotRv();
        initHotRv2();
        mode9More.getPaint().setFakeBoldText(true);
        mode9Title.setText(mTitleStr);
        mode9More.setVisibility(mBean.getMore() == MORE_VISIBLE ? View.VISIBLE : View.GONE);
        mode9More.setOnClickListener(v -> BookMoreActivity.start(mContext, mTitleStr, mBean.getId() + ""));

        SpannableStringBuilder span = new SpannableStringBuilder("缩进缩" + mode9Content.getText());
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 3,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mode9Content.setText(span);
    }

    private void initHotRv() {
        if (mList.size() == 0 || mList.size() == 1) {
            return;
        }
        if (mList.size() > 2) {
            mNewList = mList.subList(1, 3);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(1, mNewList.size());
        }
        rvMode91.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mHotAdapter = new StackRoomSuggestAdapter(mContext);
        mHotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext, mHotAdapter.getData().get(position).getId());
            }
        });
        rvMode91.setAdapter(mHotAdapter);
        rvMode91.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter.setData(mNewList);
    }

    private void initHotRv2() {
        if (!hasother) return;
        if (mList.size() > 6) {
            mNewList = mList.subList(3, 7);
            hasother = true;
        } else {
            hasother = false;
            mNewList = mList.subList(3, mNewList.size());
        }
        rvMode92.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        mHotAdapter2 = new StackRoomBookAdapter(mContext, 1);
        mHotAdapter2.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                BookDetailActivity.start(mContext,  mHotAdapter2.getData().get(position).getId());
            }
        });
        rvMode92.setAdapter(mHotAdapter2);
        rvMode92.setNestedScrollingEnabled(false); //禁止滑动
        mHotAdapter2.setData(mNewList);
    }
}
