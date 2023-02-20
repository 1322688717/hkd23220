package cc.ixcc.novelthree.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.aop.DebugLog;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.Activity.JsReadActivity;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.ui.adapter.myAdapter.BookDirectoryAdapter;
import com.jiusen.base.BaseAdapter;
import com.jiusen.widget.layout.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 目录
 */
public class BookCatalogueActivity extends MyActivity {
    int anime_id;
    @BindView(R.id.title)
    TitleBar title;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.chapter_num)
    TextView chapterNum;
    @BindView(R.id.rv_directory)
    WrapRecyclerView rvDirectory;
    BookDirectoryAdapter mDirectoryAdapter;
    private List<ChapterBean.ChaptersBean> directoryList = new ArrayList<>();
    private ChapterBean chapterBean;
    boolean isSort = false; //false :正序 true:倒叙
    String isShelve = Constants.SHELVE_NO_EXIST;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_catalogue;
    }

    @DebugLog
    public static void start(Context context, int anime_id,String isshelve) {
        Intent intent = new Intent(context, BookCatalogueActivity.class);
        intent.putExtra("anime_id", anime_id);
        intent.putExtra("isshelve", isshelve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        anime_id = getIntent().getIntExtra("anime_id", 0);
        isShelve = getIntent().getStringExtra("isshelve");
        getChapterList(anime_id);
    }

    public void getChapterList(int anime_id) {
        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
                .isMultipart(true)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        chapterBean = new Gson().fromJson(info[0], ChapterBean.class);
                        directoryList.addAll(chapterBean.getChapters());
                        setView();
                    }
                });
    }

    @Override
    public void onRightClick(View v) {
        isSort = !isSort;
        SortChapter();
    }

    private void SortChapter() {
        if (chapterBean == null || chapterBean.getChapters().size() == 0) {
            return;
        }

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setStackFromEnd(isSort);//列表再底部开始展示，反转后由上面开始展示
        layout.setReverseLayout(isSort);//列表翻转
        rvDirectory.setLayoutManager(layout);
        title.setRightIcon(isSort ? getDrawable(R.mipmap.icon_sort1) : getDrawable(R.mipmap.icon_sort2));
    }

    private void setView() {
        if (chapterBean == null || chapterBean.getChapters().size() == 0) return;
        type.setText(chapterBean.getIswz() == Constants.lianzai ? getString(R.string.lianzai) : getString(R.string.wanjie));
        chapterNum.setText(chapterBean.getAllchapter() + "chapters in total");
        SortChapter();
//        chapter.setText(chapterBean.getChapters().get(0).getTitle());
        mDirectoryAdapter = new BookDirectoryAdapter(this, true);
        mDirectoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                read(Integer.parseInt(mDirectoryAdapter.getItem(position).getAnid()),mDirectoryAdapter.getItem(position).getChaps(),isShelve);
            }
        });
        rvDirectory.setAdapter(mDirectoryAdapter);
//        rv_directory.setNestedScrollingEnabled(false); //禁止滑动
        mDirectoryAdapter.setData(chapterBean.getChapters());
    }

    //TODO　替换为JsReadActivity
    public void read(int anime_id, String chaps,String isshelve) {
        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
                .isMultipart(true)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        UserBean bean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
//                        boolean isVip = TextUtils.equals(bean.getIs_vip(),"1");
                        ChapterBean chapterBean = new Gson().fromJson(info[0], ChapterBean.class);
                        int bookId = chapterBean.getAnid();
                        List<BookChapterBean> chapters = new ArrayList<>();
                        List<ChapterBean.ChaptersBean> chaptersBeanList = chapterBean.getChapters();
                        for (int i = 0; i < chaptersBeanList.size(); i++) {
                            ChapterBean.ChaptersBean chaptersBean = chaptersBeanList.get(i);
                            BookChapterBean bookChapterBean = new BookChapterBean();
                            bookChapterBean.setTitle(chaptersBean.getTitle());
                            bookChapterBean.setBookId(bookId + "");
                            bookChapterBean.setLink(chaptersBean.getChaps());
                            bookChapterBean.setUnreadble(true);
                            bookChapterBean.setCoin(Integer.parseInt(chaptersBean.getCoin()));
                            bookChapterBean.setIs_pay(TextUtils.equals(chaptersBean.getIs_pay(),"1"));
                            chapters.add(bookChapterBean);
                        }
//                        BookChapterBean coverBookChapter = new BookChapterBean();
//                        coverBookChapter.setTitle("");
//                        coverBookChapter.setBookId(bookId + "");
//                        coverBookChapter.setLink("");
//                        chapters.add(0,coverBookChapter);
//                        DownloadUtil.get(getContext()).download();

                        CollBookBean collBookBean = new CollBookBean();
                        collBookBean.set_id(bookId+"");
                        collBookBean.setAuthor(chapterBean.getAuthor());
                        collBookBean.setTitle(chapterBean.getTitle());
                        collBookBean.setCoverPic(chapterBean.getCoverpic());
                        collBookBean.setShareTitle(chapterBean.getSharetitle());
                        collBookBean.setShortIntro(chapterBean.getSharedesc());
                        collBookBean.setShare_link(chapterBean.getShare_link());
                        collBookBean.setChaptersCount(chapterBean.getAllchapter());
                        //最后更新时间
//                        collBookBean.setUpdated(detailBean.getUpdated_at());
                        collBookBean.setUpdated(chapterBean.getCreated_at());
                        collBookBean.setLastRead("");
                        collBookBean.setPaychapter(chapterBean.getPaychapter());
                        collBookBean.setIsLocal(false);
                        collBookBean.setHasCp(false);
                        collBookBean.setIsUpdate(false);
                        collBookBean.setLatelyFollower(0);
                        collBookBean.setRetentionRatio(0.0);
                        collBookBean.setBookChapters(chapters);
                        collBookBean.setIswz(chapterBean.getIswz());
                        collBookBean.setAllchapter(chapterBean.getAllchapter());
                        JsReadActivity.startActivity(getContext(),collBookBean, false,Integer.parseInt(chaps),isshelve,true);
                    }
                });
    }
//    public void read(int anime_id, String chaps,String isshelve) {
//
//        HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
//                .isMultipart(true)
//                .params("anime_id", anime_id)
//                .params("chaps", chaps)
//                .execute(new HttpCallback() {
//                    @Override
//                    public void onSuccess(int code, String msg, String[] info) {
//                        BookList resultBean = new Gson().fromJson(info[0], BookList.class);
//                        resultBean.setIs_shelve(isshelve);
//                        ReadActivity.setIsCache(false); //不从数据库获取上次阅读的翻页位置
//                        ReadActivity.openBook(resultBean, getActivity());
//                    }
//                });
//    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
