package cc.ixcc.novelthree.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.ShelveBookBean;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.event.SelectBookEvent;
import cc.ixcc.novelthree.event.SkipStackEvent;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public final class BookShelveAdapter2 extends MyAdapter<ShelveBookBean.ListBean, RecyclerView.ViewHolder> {
    private OnClickListener mLickListener;

    public BookShelveAdapter2(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.book_img)
        RoundedImageView mBookImg;
        @BindView(R.id.author)
        TextView mAuthor;
        @BindView(R.id.checkbox)
        CheckBox mCheckBox;
        @BindView(R.id.read_status_title)
        TextView mReadTitle;
        @BindView(R.id.book_name)
        TextView mBookName;
        @BindView(R.id.tvVip)
        TextView tvVip;
        @BindView(R.id.read_status)
        TextView mReadStatus;
        @BindView(R.id.book)
        LinearLayout mBookLayout;
        @BindView(R.id.add_book)
        LinearLayout mAddBookLayout;

        ViewHolder() {
            super(R.layout.item_book_shelve2);
        }

        @Override
        public void onBindView(int position) {
            mBookName.getPaint().setFakeBoldText(true);
            if (position == getData().size() - 1) {
                if (!getItem(position).isCheckshow()) {
                    mBookLayout.setVisibility(View.GONE);
                    mAddBookLayout.setVisibility(View.VISIBLE);
                } else {
                    mBookLayout.setVisibility(View.GONE);
                    mAddBookLayout.setVisibility(View.GONE);
                }
            } else {
                mBookLayout.setVisibility(View.VISIBLE);
                mAddBookLayout.setVisibility(View.GONE);
            }
            mCheckBox.setVisibility(getItem(position).isCheckshow() ? View.VISIBLE : View.GONE);
            mCheckBox.setChecked(getItem(position).isCheck());
            Glide.with(getContext()).
                    load(getItem(position).getCoverpic())
//                    .error(R.mipmap.book_cover_def) //异常时候显示的图片
                    .placeholder(R.mipmap.book_cover_def) //加载成功前显示的图片
//                    .fallback(R.mipmap.book_cover_def) //url为空的时候,显示的图片
                    .into(mBookImg);//在RequestBuilder 中使用自定义的ImageViewTarget
//            mBookShelf.setImageResource(getShelf(position));
//            mAddShelf.setImageResource(getShelf(position));
            mBookName.setText(getItem(position).getTitle());
            tvVip.setVisibility(TextUtils.equals(getItem(position).getIsvip(),"1")? View.VISIBLE:View.GONE);
            mReadStatus.setText(getItem(position).getRead_status() == 0 ? "未读" : "已读" + getItem(position).getRead_chaps() + "章/" + getItem(position).getAllchapter() + "章");
            mAuthor.setText(getItem(position).getAuthor());
//            mReadTitle.setText(getItem(position).getReadzj_title());
            mAddBookLayout.setOnClickListener(v -> {
                if (mLickListener != null) mLickListener.addBook();
            });
            mBookLayout.setOnClickListener(v -> {
                if (getItem(position).isCheckshow()) {
                    if (mLickListener != null)
                        mLickListener.selectBook(!mCheckBox.isChecked(), getItem(position));
                } else {
                    //跳到书本详情页
                    if (mLickListener != null)
                        mLickListener.readBook(getItem(position));
                }
            });
            mBookLayout.setOnLongClickListener(v -> {
                if (getItem(position).isCheckshow()) {
                    if (mLickListener != null)
                        mLickListener.selectBook(!mCheckBox.isChecked(), getItem(position));
                } else {
                    EventBus.getDefault().post(new SelectBookEvent(position));
                }
                return false;
            });
            mAddBookLayout.setOnClickListener(v -> EventBus.getDefault().post(new SkipStackEvent()));
        }
    }

    private int getShelf(int position) {
        int resource = 0;
        if (position % 3 == 0) {
            if (position == getData().size() - 1)
                resource = R.mipmap.book_shelf_added;
            else
                resource = R.mipmap.book_shelf_bg1;
        } else if (position % 3 == 1) {
            if (position == getData().size() - 1)
                resource = R.mipmap.book_shelf_bg3;
            else
                resource = R.mipmap.book_shelf_bg2;
        } else if (position % 3 == 2) {
            resource = R.mipmap.book_shelf_bg3;
        }
        return resource;
    }

    public interface OnClickListener {
        void addBook();
        void selectBook(boolean select, ShelveBookBean.ListBean bean);
        void readBook(ShelveBookBean.ListBean bean);
    }

    public void setOnClickListener(OnClickListener lickListener) {
        mLickListener = lickListener;
    }
}