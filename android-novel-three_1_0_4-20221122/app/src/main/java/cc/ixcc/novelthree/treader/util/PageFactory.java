package cc.ixcc.novelthree.treader.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.treader.Config;
import cc.ixcc.novelthree.treader.db.BookList;
import cc.ixcc.novelthree.treader.view.PageWidget;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.ui.activity.my.BookCoverActivity;
import com.jiusen.base.util.DpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20 0020.
 * 核心
 */
public class PageFactory {
    private static final String TAG = "PageFactory";
    private static PageFactory pageFactory;

    private Context mContext;
    private Config config;

    // 默认背景颜色
    private int m_backColor = 0xffdde5c8;
    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;
    //文字字体大小
    private float m_fontSize;
    //时间格式
    private SimpleDateFormat sdf;
    //时间
    private String date;
    //进度格式
    private DecimalFormat df;
    //电池边界宽度
    private float mBorderWidth;
    // 上下与边缘的距离
    private float marginHeight;
    // 左右与边缘的距离
    private float measureMarginWidth;
    // 左右与边缘的距离
    private float marginWidth;
    //状态栏距离底部高度
    private float statusMarginBottom;
    //行间距
    private float lineSpace;
    //段间距
    private float paragraphSpace;
    //字高度
    private float fontHeight;
    //字体
    private Typeface typeface;
    //文字画笔
    private Paint mPaint;
    //加载画笔
    private Paint waitPaint;
    //文字颜色
    private int m_textColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页可以显示的行数
    private int mLineCount;
    //电池画笔
    private Paint mBatterryPaint;
    //章节画笔
    private Paint mChapterPaint;
    //电池字体大小
    private float mBatterryFontSize;
    //背景图片
    private Bitmap m_book_bg = null;
    private Intent batteryInfoIntent;
    //电池电量百分比
    private float mBatteryPercentage;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //当前是否为第一页
    private boolean m_isfirstPage;
    //当前是否为最后一页
    private boolean m_islastPage;
    //书本widget
    private PageWidget mBookPageWidget;
    //现在的进度
    private float currentProgress;
    //书本路径
    private String bookPath = "";
    //书本名字
    private String bookName = "";
    //
    private BookList bookList;
    //书本章节
    private int currentCharter = 0;
    //当前电量
    private int level = 0;
    private BookUtil mBookUtil;
    private PageEvent mPageEvent;
    private TRPage currentPage;
    private TRPage prePage;
    private TRPage cancelPage;
    private BookTask bookTask;
    ContentValues values = new ContentValues();

    //是否跨越章节 -1前一章 0本章 1后一章
    private int PageState = 0;

    private static Status mStatus = Status.OPENING;

    public enum Status {
        OPENING,
        FINISH,
        FAIL,
    }

    public static synchronized PageFactory getInstance() {
        return pageFactory;
    }

    public static synchronized PageFactory createPageFactory(Context context) {
        if (pageFactory == null) {
            pageFactory = new PageFactory(context);
        }
        return pageFactory;
    }

    private PageFactory(Context context) {
        mBookUtil = new BookUtil();
        mContext = context;
        PageState = 0;
        config = Config.getInstance();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mWidth = metric.widthPixels;
        mHeight = metric.heightPixels;

        sdf = new SimpleDateFormat("HH:mm");//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());
        df = new DecimalFormat("#0.0");

        marginWidth = mContext.getResources().getDimension(R.dimen.readingMarginWidth);
        marginHeight = DpUtil.dip2px(mContext, 60);

        statusMarginBottom = mContext.getResources().getDimension(R.dimen.reading_status_margin_bottom);
        lineSpace = context.getResources().getDimension(R.dimen.reading_line_spacing);
        paragraphSpace = context.getResources().getDimension(R.dimen.reading_paragraph_spacing);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;

        typeface = config.getTypeface();
        m_fontSize = config.getFontSize();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(m_fontSize);// 字体大小
        mPaint.setColor(m_textColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(mContext.getResources().getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(m_textColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果
        calculateLineCount();

        mBorderWidth = mContext.getResources().getDimension(R.dimen.reading_board_battery_border_width);
        mBatterryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatterryFontSize = CommonUtil.sp2px(context, 12);
        mBatterryPaint.setTextSize(mBatterryFontSize);
        mBatterryPaint.setTypeface(typeface);
        mBatterryPaint.setTextAlign(Paint.Align.LEFT);
        mBatterryPaint.setColor(m_textColor);
        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册广播,随时获取到电池电量信息

        initBg(config.getDayOrNight());
        measureMarginWidth();
    }

    private void measureMarginWidth() {
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;
    }

    //初始化背景
    @SuppressLint("ResourceAsColor")
    private void initBg(Boolean isNight) {
        if (isNight) {
            setBookBg(Config.BOOK_BG_NIGHT);
        } else {
            //设置背景
            setBookBg(config.getBookBgType());
        }
    }

    private void calculateLineCount() {
        mLineCount = (int) (mVisibleHeight / (m_fontSize + lineSpace));// 可显示的行数
    }

    private void drawStatus(Bitmap bitmap) {
        String status = "";
        switch (mStatus) {
            case OPENING:
                status = "正在打开书本...";
                break;
            case FAIL:
                status = "打开书本失败！";
                break;
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        Button b = new Button(mContext);
        b.setText("Text正在打开书本Text");
        b.draw(c);

        waitPaint.setColor(getTextColor());
        waitPaint.setTextSize(DpUtil.dip2px(mContext, 15));
        waitPaint.setTextAlign(Paint.Align.CENTER);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
        Paint.FontMetricsInt fontMetrics = waitPaint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        waitPaint.setTextAlign(Paint.Align.CENTER);
        c.drawText(status, targetRect.centerX(), baseline, waitPaint);
        mBookPageWidget.postInvalidate();
    }

    /**
     * todo
     * @param bitmap
     * @param m_lines
     * @param updateCharter
     */
    @SuppressLint("ResourceAsColor")
    public void onDraw(Bitmap bitmap, List<String> m_lines, Boolean updateCharter) {
        Log.i(TAG, "onDraw: ");
        if (getDirectoryList().size() > 0 && updateCharter) {
            currentCharter = getCurrentCharter();
        }

        //更新数据库进度
        if (currentPage != null && bookList != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (currentPage != null)
                        values.put("begin", currentPage.getBegin());
                    if (mBookUtil.getDirectoryList().size() > currentCharter)
                        values.put("chaps", mBookUtil.getDirectoryList().get(currentCharter).getChaps());
                    if (bookList != null)
                        DataSupport.update(BookList.class, values, bookList.getId());
                }
            }.start();
        }

        Canvas c = new Canvas(bitmap);
        //先画背景
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        mPaint.setTextSize(getFontSize());
        mPaint.setColor(getTextColor());
        mBatterryPaint.setColor(getTextColor());
        mBatterryPaint.setTextSize(DpUtil.dip2px(mContext, 13));
        if (m_lines.size() == 0) {
            return;
        }

        //TODO 再画文字
        if (m_lines.size() > 0) {
            float y = marginHeight;
            for (String strLine : m_lines) {
                y += m_fontSize + lineSpace;
                c.drawText(strLine, measureMarginWidth, y, mPaint);
            }
        }

        //画进度及时间
        int dateWith = (int) (mBatterryPaint.measureText(date) + mBorderWidth);//时间宽度
        float fPercent = (float) (currentCharter * 1.0 / mBookUtil.getDirectoryList().size());//进度
        /**
         * currentCharter 当前章节数
         * mBookUtil.getDirectoryList().size(); 总章节数
         */
        Log.i(TAG, "onDraw: " + currentCharter + "size" + mBookUtil.getDirectoryList().size());
        currentProgress = fPercent;
        if (mPageEvent != null) {
            mPageEvent.changeProgress(fPercent, bookList);
        }
        String strPercent = df.format(fPercent * 100) + "%";//进度文字
        int nPercentWidth = (int) mBatterryPaint.measureText("999.9%") + 1;  //Paint.measureText直接返回參數字串所佔用的寬度
        // 画电池
        level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = mWidth - nPercentWidth + dateWith - DpUtil.dip2px(mContext, 15);//电池外框left位置
        //画电池外框
        float width = CommonUtil.convertDpToPixel(mContext, 20) - mBorderWidth;
        float height = CommonUtil.convertDpToPixel(mContext, 10);
        rect1.set(rect1Left, mHeight - height - statusMarginBottom, rect1Left + width, mHeight - statusMarginBottom);
        rect2.set(rect1Left + mBorderWidth, mHeight - height + mBorderWidth - statusMarginBottom, rect1Left + width - mBorderWidth, mHeight - mBorderWidth - statusMarginBottom);

        //画电池头
        int poleHeight = (int) CommonUtil.convertDpToPixel(mContext, 10) / 2;
        rect2.left = rect1.right;
        rect2.top = rect2.top + poleHeight / 4;
        rect2.right = rect1.right + mBorderWidth;
        rect2.bottom = rect2.bottom - poleHeight / 4;
        String charterName = bookList.getCharset();

        int nChaterWidth = (int) mBatterryPaint.measureText(charterName) + 1;

        //画章节名
        c.drawText(charterName, measureMarginWidth, DpUtil.dip2px(mContext, 45), mBatterryPaint);
        mBookPageWidget.postInvalidate();
    }

    boolean isNextPage;

    //向前翻页
    public void prePage() {
        if (currentPage == null) {
            return;
        }
        isNextPage = false;
        if (currentPage.getBegin() <= 0) {
            Log.e(TAG, "当前是第一页");
            if (!m_isfirstPage) {
                PageState = -1;
                preChapter(false);
            }
            return;
        }
        else {
            m_isfirstPage = false;
        }
        PageState = 0;
        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
        currentPage = getPrePage();
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);
    }

    //TODO 向后翻页
    public void nextPage() {
        if (currentPage == null) return;
        isNextPage = true;
        if (currentPage.getEnd() >= mBookUtil.getBookLen()) {
            //小说到了章节最后一页
            Log.e(TAG, "已经是最后一页了");
            if (!m_islastPage) {
                //TODO 如果不是最后一个章节 下个章节
                PageState = 1;
                nextChapter();
//                Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
            else {
//                ToastUtils.show("已经是最后一页了");
            }
            return;
        } else {

        }
        PageState = 0;
        m_islastPage = false;
        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
        prePage = currentPage;
        currentPage = getNextPage();
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);
        Log.e("nextPage", "nextPagenext");
    }

    //取消翻页
    public void cancelPage() {
        currentPage = cancelPage;
    }

    /**
     * 打开书本
     *
     * @throws IOException
     */
    long lastbegin;

    public void openBook(BookList bookList, boolean cache) throws IOException {
        //清空数据
        currentCharter = 0;
        initBg(config.getDayOrNight());

        bookPath = bookList.getBookpath();
        bookName = FileUtils.getFileName(bookPath);

        mStatus = Status.OPENING;
        drawStatus(mBookPageWidget.getCurPage());
        drawStatus(mBookPageWidget.getNextPage());
        if (bookTask != null && bookTask.getStatus() != AsyncTask.Status.FINISHED) {
            bookTask.cancel(true);
        }
        bookTask = new BookTask();

        if (cache) {
            List<BookList> bookLists = DataSupport.findAll(BookList.class);
            for (BookList book : bookLists) {
                if (bookList.getAnid() == book.getAnid()) {
                    bookList.setBegin(book.getBegin());
                    bookList.setChaps(book.getChaps());
                }
            }
        }
        this.bookList = bookList;
        lastbegin = bookList.getBegin();
        currentCharter = bookList.getChaps();
        if (currentCharter == 1 && lastbegin == 0 && cache) { //第一次阅读并且不是从目录进来
            BookCoverActivity.Start(mContext, bookList.getAnid(), mBookPageWidget, pageFactory);
        }
        getInfo(bookList.getAnid(), String.valueOf(bookList.getChaps()));
    }

    private class BookTask extends AsyncTask<Long, Void, Boolean> {
        private long begin = 0;

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.e("onPostExecute", isCancelled() + "");
            if (isCancelled()) {
                return;
            }
            if (result) {
                PageFactory.mStatus = PageFactory.Status.FINISH;
                currentPage = getPageForBegin(begin);
                if (mBookPageWidget != null) {
                    currentPage(true);
                }
                lastbegin = -1;
            } else {
                PageFactory.mStatus = PageFactory.Status.FAIL;
                drawStatus(mBookPageWidget.getCurPage());
                drawStatus(mBookPageWidget.getNextPage());
                Toast.makeText(mContext, "打开书本失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            begin = params[0];
            try {
                mBookUtil.openBook(bookList, true);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }

    public TRPage getNextPage() {
        mBookUtil.setPostition(currentPage.getEnd());

        TRPage trPage = new TRPage();
        trPage.setBegin(currentPage.getEnd() + 1);
        Log.e("begin", currentPage.getEnd() + 1 + "");
        trPage.setLines(getNextLines());
        Log.e("end", mBookUtil.getPosition() + "");
        trPage.setEnd(mBookUtil.getPosition());
        return trPage;
    }

    public TRPage getPrePage() {
        mBookUtil.setPostition(currentPage.getBegin());

        TRPage trPage = new TRPage();
        trPage.setEnd(mBookUtil.getPosition() - 1);
        Log.e("end", mBookUtil.getPosition() - 1 + "");
        trPage.setLines(getPreLines());
        Log.e("begin", mBookUtil.getPosition() + "");
        trPage.setBegin(mBookUtil.getPosition());
        return trPage;
    }

    public void refresh() {
        if (currentPage == null) return;
        currentPage(true);
    }

    //TODO
    public TRPage getPageForBegin(long begin) {
        TRPage trPage = new TRPage();
        trPage.setBegin(begin);

        mBookUtil.setPostition(begin - 1);
        trPage.setLines(getNextLines());
        trPage.setEnd(mBookUtil.getPosition());
        return trPage;
    }

    //下一行
    public List<String> getNextLines() {
        List<String> lines = new ArrayList<>();
        float width = 0;
        float height = 0;
        String line = "";
        while (mBookUtil.next(true) != -1) {
            char word = (char) mBookUtil.next(false);
            //判断是否换行
            if ((word + "").equals("\r") && (((char) mBookUtil.next(true)) + "").equals("\n")) {
                mBookUtil.next(false);
                if (!line.isEmpty()) {
                    lines.add(line);
                    line = "";
                    width = 0;
                    if (lines.size() == mLineCount) {
                        break;
                    }
                }
            } else {
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    lines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }

            if (lines.size() == mLineCount) {
                if (!line.isEmpty()) {
                    mBookUtil.setPostition(mBookUtil.getPosition() - 1);
                }
                break;
            }
        }

        if (!line.isEmpty() && lines.size() < mLineCount) {
            lines.add(line);
        }
        for (String str : lines) {
            Log.e(TAG, str + "   ");
        }
        return lines;
    }

    //上一行
    public List<String> getPreLines() {
        List<String> lines = new ArrayList<>();
        float width = 0;
        String line = "";

        char[] par = mBookUtil.preLine();
        while (par != null) {
            List<String> preLines = new ArrayList<>();
            for (int i = 0; i < par.length; i++) {
                char word = par[i];
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    preLines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }
            if (!line.isEmpty()) {
                preLines.add(line);
            }

            lines.addAll(0, preLines);

            if (lines.size() >= mLineCount) {
                break;
            }
            width = 0;
            line = "";
            par = mBookUtil.preLine();
        }

        List<String> reLines = new ArrayList<>();
        int num = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (reLines.size() < mLineCount) {
                reLines.add(0, lines.get(i));
            } else {
                num = num + lines.get(i).length();
            }
            Log.e(TAG, lines.get(i) + "   ");
        }

        if (num > 0) {
            if (mBookUtil.getPosition() > 0) {
                mBookUtil.setPostition(mBookUtil.getPosition() + num + 2);
            } else {
                mBookUtil.setPostition(mBookUtil.getPosition() + num);
            }
        }

        return reLines;
    }

    //上一章
    public void preChapter(boolean isClick) {
        if (mBookUtil.getDirectoryList().size() > 0) {
            int num = currentCharter;
            if (num == 0) {
                num = getCurrentCharter();
            }
            num--;
            if (num >= 0) {
                String chaps = mBookUtil.getDirectoryList().get(num).getChaps();
                currentCharter = num;
                getInfo(bookList.getAnid(), chaps);
                m_isfirstPage = false;
            } else {
                if (isClick) {
                    ToastUtils.show("已经是第一章了");
                }
                else {
                    if (currentCharter == 0 && lastbegin == -1) { //第一次阅读并且不是从目录进来
                        BookCoverActivity.Start(mContext, bookList.getAnid(), mBookPageWidget, pageFactory);
                    }
                }
            }
        }
    }

    //TODO 下一章
    public void nextChapter() {
        int num = currentCharter;
        if (num == 0) {
            num = getCurrentCharter();
        }
        num++;
        Log.e(TAG,"nextChapter num: "+ num);
        if (num < getDirectoryList().size()) {
            String chaps = mBookUtil.getDirectoryList().get(num).getChaps();
            currentCharter = num;
            Log.e(TAG,"nextChapter currentCharter: "+ currentCharter);
            getInfo(bookList.getAnid(), chaps);
        }
        else {
            Log.e(TAG,"nextChapter currentCharter: "+ currentCharter);
            ToastUtils.show("已经是最后一章了");
        }
    }

    private void addreadhistory(int anime_id, String chaps) {
        if (!AppUtils.isLogin()) return;
        HttpClient.getInstance().post(AllApi.addreadhistory, AllApi.addreadhistory)
                .params("anime_id", anime_id)
                .params("chaps", chaps)
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
//                        ToastUtils.show(msg);
                    }
                });
    }

    /**
     * 从服务器获取小说内容
     * @param anime_id
     * @param chaps 章节数
     */
    public void getInfo(int anime_id, String chaps) {
//        HttpClient.getInstance().cancel();
        HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
            .isMultipart(true)
            .params("anime_id", anime_id)
            .params("chaps", chaps)
            .execute(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    addreadhistory(anime_id, chaps); //添加阅读记录
                    BookList resultBean = new Gson().fromJson(info[0], BookList.class);
                    //TODO
                    bookList = resultBean;
                    try {
                        if (lastbegin != -1) {
                            bookList.setBegin(lastbegin);
                            bookTask.execute(bookList.getBegin());
                        }
                        else {
                            //不是第一次阅读 对应翻页
                            prePage = currentPage;
                            mBookUtil.openBook(bookList, false);
                            currentPage = getPageForBegin(isNextPage ? 0 : mBookUtil.getBookLen() - 100);
                            currentPage(true);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    //获取现在的章
    public int getCurrentCharter() {
        int num = 0;
        for (int i = 0; getDirectoryList().size() > i; i++) {
            ChapterBean.ChaptersBean bookCatalogue = getDirectoryList().get(i);
            if (String.valueOf(bookList.getChaps()).equals(bookCatalogue.getChaps())) {
                num = i;
                break;
            }
        }
        return num;
    }

    //TODO 绘制当前页面
    public void currentPage(Boolean updateChapter) {
        if (currentPage == null) return;

        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), updateChapter);

        //TODO
        if (prePage==null) {
            onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), updateChapter);
        }
        else{
            switch (PageState){
                case -1:
                    onDraw(mBookPageWidget.getNextPage(), prePage.getLines(), updateChapter);
                    break;
                case 0:
                    onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), updateChapter);
                    break;
                case 1:
                    onDraw(mBookPageWidget.getNextPage(), prePage.getLines(), updateChapter);
                    break;
            }
            if (PageState ==0) {

            }else {

            }
        }
    }

    //更新电量
    public void updateBattery(int mLevel) {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            if (level != mLevel) {
                level = mLevel;
                currentPage(false);
            }
        }
    }

    public void updateTime() {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            String mDate = sdf.format(new java.util.Date());
            if (date != mDate) {
                date = mDate;
                currentPage(false);
            }
        }
    }

    //改变进度
    public void changeProgress(float progress) {
        if (mBookUtil.getDirectoryList().size() == 0) return;
        long num = (long) (mBookUtil.getDirectoryList().size() * progress);
        if (num >= mBookUtil.getDirectoryList().size())
            num = mBookUtil.getDirectoryList().size() - 1;
        String chaps = mBookUtil.getDirectoryList().get((int) num).getChaps();
        currentCharter = (int) num;
        isNextPage = true;
        if (bookList != null)
            getInfo(bookList.getAnid(), chaps);
    }

    //改变进度
    public void changeChapter(String chaps) {
        isNextPage = true;
        getInfo(bookList.getAnid(), chaps);
    }

    //改变字体大小
    public void changeFontSize(int fontSize) {
        this.m_fontSize = fontSize;
        mPaint.setTextSize(m_fontSize);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);
    }

    //改变字体
    public void changeTypeface(Typeface typeface) {
        this.typeface = typeface;
        mPaint.setTypeface(typeface);
        mBatterryPaint.setTypeface(typeface);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);
    }

    //改变背景
    public void changeBookBg(int type) {
        setBookBg(type);
        currentPage(false);
    }

    //设置页面的背景
    public void setBookBg(int type) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (type) {
            case Config.BOOK_BG_NIGHT:
                canvas.drawColor(mContext.getResources().getColor(config.isHuyan() ? R.color.day_night_huyan : R.color.day_night));
                color = mContext.getResources().getColor(R.color.read_font_night);
                setBookPageBg(mContext.getResources().getColor(config.isHuyan() ? R.color.day_night_huyan : R.color.day_night));
                break;
            case Config.BOOK_BG_DEFAULT:
                canvas.drawColor(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_default_huyan : R.color.read_bg_default));
                color = mContext.getResources().getColor(R.color.read_font_default);
                setBookPageBg(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_default_huyan : R.color.read_bg_default));
                break;
            case Config.BOOK_BG_1:
                canvas.drawColor(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_1_huyan : R.color.read_bg_1));
                color = mContext.getResources().getColor(R.color.read_font_1);
                setBookPageBg(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_1_huyan : R.color.read_bg_1));
                break;
            case Config.BOOK_BG_2:
                canvas.drawColor(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_2_huyan : R.color.read_bg_2));
                color = mContext.getResources().getColor(R.color.read_font_2);
                setBookPageBg(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_2_huyan : R.color.read_bg_2));
                break;
            case Config.BOOK_BG_3:
                canvas.drawColor(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_3_huyan : R.color.read_bg_3));
                color = mContext.getResources().getColor(R.color.read_font_3);
                if (mBookPageWidget != null) {
                    mBookPageWidget.setBgColor(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_3_huyan : R.color.read_bg_3));
                }
                break;
            case Config.BOOK_BG_4:
                canvas.drawColor(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_4_huyan : R.color.read_bg_4));
                color = mContext.getResources().getColor(R.color.read_font_4);
                setBookPageBg(mContext.getResources().getColor(config.isHuyan() ? R.color.read_bg_4_huyan : R.color.read_bg_4));
                break;
        }
        setBgBitmap(bitmap);
        //设置字体颜色
        setM_textColor(color);
    }

    public void setBookPageBg(int color) {
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
            config.setBookPageBg(color);
        }
    }

    //设置日间或者夜间模式
    public void setDayOrNight(Boolean isNgiht) {
        initBg(isNgiht);
        currentPage(false);
    }

    public void clear() {
        currentCharter = 0;
        bookPath = "";
        bookName = "";
        bookList = null;
        mBookPageWidget = null;
        mPageEvent = null;
        cancelPage = null;
        prePage = null;
        currentPage = null;
    }

    public static Status getStatus() {
        return mStatus;
    }

    public long getBookLen() {
        return mBookUtil.getBookLen();
    }

    public TRPage getCurrentPage() {
        return currentPage;
    }

    //获取书本的章
    public List<ChapterBean.ChaptersBean> getDirectoryList() {
        return mBookUtil.getDirectoryList();
    }

    public String getBookPath() {
        return bookPath;
    }

    //是否是第一页
    public boolean isfirstPage() {
        return m_isfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return m_islastPage;
    }

    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //设置文字颜色
    public void setM_textColor(int m_textColor) {
        this.m_textColor = m_textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.m_textColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.m_fontSize;
    }

    public void setPageWidget(PageWidget mBookPageWidget) {
        this.mBookPageWidget = mBookPageWidget;
    }

    public void setPageEvent(PageEvent pageEvent) {
        this.mPageEvent = pageEvent;
    }

    public interface PageEvent {
        void changeProgress(float progress, BookList list);
    }
}
