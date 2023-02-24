package cc.ixcc.novelthree.jsReader.page;


import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.jsReader.model.local.BookRepository;
import cc.ixcc.novelthree.jsReader.utils.BookManager;
import cc.ixcc.novelthree.jsReader.utils.Constant;
import cc.ixcc.novelthree.jsReader.utils.FileUtils;
import cc.ixcc.novelthree.jsReader.utils.StringUtils;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StringUtil;

import com.tencent.mmkv.MMKV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-5-29.
 * 网络页面加载器
 */

public class NetPageLoader extends PageLoader {
    private static final String TAG = "PageFactory";
    private int currentPage = 0;

    public NetPageLoader(PageView pageView, CollBookBean collBook, int startChapter) {
        super(pageView, collBook, startChapter);
    }

    //将 BookChapter 转换成当前可用的 Chapter
    private List<TxtChapter> convertTxtChapter(List<BookChapterBean> bookChapters) {
        List<TxtChapter> txtChapters = new ArrayList<>(bookChapters.size());
        for (BookChapterBean bean : bookChapters) {
            TxtChapter chapter = new TxtChapter();
            chapter.bookId = bean.getBookId();
            chapter.title = bean.getTitle();
            chapter.link = bean.getLink();
            chapter.coin = bean.getCoin();
            chapter.is_pay = bean.getIs_pay();
            txtChapters.add(chapter);
        }
        return txtChapters;
    }

    @Override
    public void refreshChapterList(boolean b) {
        if (mCollBook.getBookChapters() == null) {
            return;
        }

        // 将 BookChapter 转换成当前可用的 Chapter
        mChapterList = convertTxtChapter(mCollBook.getBookChapters());
        isChapterListPrepare = true;

        // 目录加载完成，执行回调操作。
        if (mPageChangeListener != null) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }

        // 如果章节未打开
        if (!isChapterOpen() || !b) {
            // 打开章节
            openChapter();
        }
    }

    @Override
    protected BufferedReader getChapterReader(TxtChapter chapter) throws Exception {
        File file = new File(Constant.BOOK_CACHE_PATH + mCollBook.get_id()
                + File.separator + chapter.title + FileUtils.SUFFIX_NB);
        if (!file.exists()) return null;

        Reader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        return br;
    }

    @Override
    protected boolean hasChapterData(TxtChapter chapter) {
        return BookManager.isChapterCached(mCollBook.get_id(), chapter.title);
    }

    // 装载上一章节的内容
    @Override
    boolean parsePrevChapter() {
        boolean isRight = super.parsePrevChapter();

        if (mStatus == STATUS_FINISH) {
            loadPrevChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载当前章内容。
    @Override
    boolean parseCurChapter() {
        boolean isRight = super.parseCurChapter();

        if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }
        return isRight;
    }

    // 装载下一章节的内容
    @Override
    boolean parseNextChapter() {
        boolean isRight = super.parseNextChapter();

        if (mStatus == STATUS_FINISH) {
            loadNextChapter();
        } else if (mStatus == STATUS_LOADING) {
            loadCurrentChapter();
        }

        return isRight;
    }

    /**
     * 加载当前页的前面两个章节 改为前一个章节
     */
    private void loadPrevChapter() {
        UserBean bean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (mPageChangeListener != null) {
            int end = mCurChapterPos;
            int begin = end - 1;
            if (begin < 0) {
                begin = 0;
            }
            requestChapters(begin, end, StringUtil.isVip(bean));
        }
    }

    /**
     * 加载前一页，当前页，后一页。
     */
    private void loadCurrentChapter() {
        if (mPageChangeListener != null) {
            UserBean bean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
            int begin = mCurChapterPos;
            int end = mCurChapterPos;

            // 是否当前不是最后一章
            if (end < mChapterList.size()) {
                end = end + 1;
                if (end >= mChapterList.size()) {
                    end = mChapterList.size() - 1;
                }
            }

            // 如果当前不是第一章
            if (begin != 0) {
                begin = begin - 1;
                if (begin < 0) {
                    begin = 0;
                }
            }

            requestChapters(begin, end, StringUtil.isVip(bean));
        }
    }

    /**
     * 加载当前页的后两个章节 修改为后一个章节
     */
    private void loadNextChapter() {
        UserBean bean = MMKV.defaultMMKV().decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (mPageChangeListener != null) {

            // 提示加载后两章
            int begin = mCurChapterPos + 1;
            int end = begin;

            // 判断是否大于最后一章
            if (begin >= mChapterList.size()) {
                // 如果下一章超出目录了，就没有必要加载了
                return;
            }

            requestChapters(begin, end, StringUtil.isVip(bean));
        }
    }

    //判断是否是vip
    private void requestChapters(int start, int end, boolean isVip) {
        // 检验输入值
        if (start < 0) {
            start = 0;
        }

        if (end >= mChapterList.size()) {
            end = mChapterList.size() - 1;
        }

        List<TxtChapter> chapters = new ArrayList<>();
        // 过滤，哪些数据已经加载了
        for (int i = start; i <= end; ++i) {
            TxtChapter txtChapter = mChapterList.get(i);
            String bookId = txtChapter.getBookId();
            if (isVip) {

            }
            if (!hasChapterData(txtChapter)) {
                chapters.add(txtChapter);
            }
        }

        if (!chapters.isEmpty()) {
            mPageChangeListener.requestChapters(chapters);
        }
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        if (mCollBook != null && isChapterListPrepare) {
            //表示当前CollBook已经阅读
            mCollBook.setIsUpdate(false);
            mCollBook.setLastRead(StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            //直接更新
            BookRepository.getInstance().saveCollBook(mCollBook);
        }
    }
}

