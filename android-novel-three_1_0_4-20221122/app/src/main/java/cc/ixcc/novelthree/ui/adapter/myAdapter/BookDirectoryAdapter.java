package cc.ixcc.novelthree.ui.adapter.myAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.common.MyAdapter;
import cc.ixcc.novelthree.treader.Config;

import butterknife.BindView;

public final class BookDirectoryAdapter extends MyAdapter<ChapterBean.ChaptersBean, RecyclerView.ViewHolder> {
    private OnClickListener mClickListener;
    public static String NOPAY = "0";
    Config config;
    boolean isdefault;

    public BookDirectoryAdapter(Context context, boolean isdefault) {
        super(context);
        this.isdefault = isdefault;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    final class ViewHolder extends MyAdapter.ViewHolder {

        @BindView(R.id.suo)
        ImageView suo;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tvFree)
        TextView tvFree;
        @BindView(R.id.line)
        View line;

        ViewHolder() {
            super(R.layout.item_directory);
        }

        @Override
        public void onBindView(int position) {
//            Glide.with(getContext())
////                    .load(getItem(position).getImgurl())
//                    .load(R.mipmap.book_img1)
//                    .into(mBookImg);
            name.setText(getItem(position).getTitle());
//            suo.setVisibility(getItem(position).getIs_pay().equals(NOPAY) ? View.GONE : View.VISIBLE);
            ChapterBean.ChaptersBean item = getItem(position);
            if (TextUtils.equals(item.getCoin(),"0")) {
                tvFree.setVisibility(View.VISIBLE);
                suo.setVisibility(View.INVISIBLE);
            }else {
                tvFree.setVisibility(View.INVISIBLE);
                suo.setVisibility(TextUtils.equals(item.getIs_pay(),"1")?View.INVISIBLE:View.VISIBLE);
            }
            initDayOrNight();
        }

        private void initDayOrNight() {
            config = Config.getInstance();
            if (isdefault) {
                name.setTextColor(getColor(R.color.read_font_default));
                line.setBackground(getDrawable(R.color.colorbg));
                suo.setImageResource(R.mipmap.icon_suo);
            } else {
                name.setTextColor(config.getDayOrNight() ? getColor(R.color.read_font_night) : getColor(R.color.read_font_default));
                line.setBackground(config.getDayOrNight() ? getDrawable(R.color.read_font_default) : getDrawable(R.color.line));
                suo.setImageResource(config.getDayOrNight() ? R.mipmap.icon_suo_night : R.mipmap.icon_suo);
            }
        }
    }

    public interface OnClickListener {
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
}