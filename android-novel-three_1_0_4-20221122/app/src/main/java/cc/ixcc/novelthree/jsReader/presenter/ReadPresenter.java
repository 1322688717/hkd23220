package cc.ixcc.novelthree.jsReader.presenter;


import android.util.Log;

import com.google.gson.Gson;

import cc.ixcc.novelthree.bean.UserBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.jsReader.Contract.ReadContract;
import cc.ixcc.novelthree.jsReader.model.bean.ChapterInfoBean;
import cc.ixcc.novelthree.jsReader.model.local.BookRepository;
import cc.ixcc.novelthree.jsReader.page.TxtChapter;
import cc.ixcc.novelthree.jsReader.utils.LogUtils;
import cc.ixcc.novelthree.treader.db.BookList;
import cc.ixcc.novelthree.utils.SpUtil;
import cc.ixcc.novelthree.utils.StringUtil;

import com.tencent.mmkv.MMKV;

import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by newbiechen on 17-5-16.
 */

public class ReadPresenter extends RxPresenter<ReadContract.View>
        implements ReadContract.Presenter {
    private static final String TAG = "ReadPresenter";

    private Subscription mChapterSub;

    //加载章节
    @Override
    public void loadCategory(String bookId) {
        //TODO  加载章节
//        mView.showCategory(beans);
//            Disposable disposable = RemoteRepository.getInstance()
//                .getBookChapters(bookId)
//                .doOnSuccess(new Consumer<List<BookChapterBean>>() {
//                    @Override
//                    public void accept(List<BookChapterBean> bookChapterBeen) throws Exception {
//                        //进行设定BookChapter所属的书的id。
//                        for (BookChapterBean bookChapter : bookChapterBeen) {
//                            bookChapter.setId(MD5Utils.strToMd5By16(bookChapter.getLink()));
//                            bookChapter.setBookId(bookId);
//                        }
//                    }
//                })
//                .compose(RxUtils::toSimpleSingle)
//                .subscribe(
//                        beans -> {
//                            mView.showCategory(beans);
//                        }
//                        ,
//                        e -> {
//                            //TODO: Haven't grate conversation method.
//                            LogUtils.e(e);
//                        }
//                );
//        addDisposable(disposable);
    }

    //加载章节
    @Override
    public void loadChapter(String bookId, List<TxtChapter> bookChapters) {
        try {
            int size = bookChapters.size();

            //取消上次的任务，防止多次加载
            if (mChapterSub != null) {
                mChapterSub.cancel();
            }

            List<Single<ChapterInfoBean>> chapterInfos = new ArrayList<>(bookChapters.size());
            ArrayDeque<String> titles = new ArrayDeque<>(bookChapters.size());


            // 将要下载章节，转换成网络请求。
            for (int i = 0; i < size; ++i) {
                TxtChapter bookChapter = bookChapters.get(i);
                if (checkVip() || bookChapter.getCoin() <= 0 || bookChapter.isIs_pay()) {
                    HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
                            .isMultipart(true)
                            .params("anime_id", bookChapter.getBookId())
                            .params("chaps", bookChapter.getLink())
                            .execute(new HttpCallback() {
                                @Override
                                public void onSuccess(int code, String msg, String[] info) {

                                    BookList resultBean = new Gson().fromJson(info[0], BookList.class);
                                    BookRepository.getInstance().saveChapterInfo(
                                            bookId, resultBean.getTitle(), resultBean.getInfo().replace("\\&quot;", "")
                                    );
                                    mView.finishChapter();

                                }
                            });
                } else {
                    boolean isAuto = mmkv.decodeBool(bookId, false);
                    if (isAuto) {
                        HttpClient.getInstance().post(AllApi.paychapter, AllApi.paychapter)
                                .params("anime_id", bookId)
                                .params("chaps", bookChapter.getLink())
                                .execute(new HttpCallback() {
                                    @Override
                                    public void onSuccess(int code, String msg, String[] info) {
                                        if (code == 200) {
                                            //更新状态
                                            Log.e(TAG, "msg: " + msg + " ,chap: " + bookChapter.getLink() + " line130");
                                            mView.upDate(bookChapter.getLink());

                                            HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
                                                    .isMultipart(true)
                                                    .params("anime_id", bookChapter.getBookId())
                                                    .params("chaps", bookChapter.getLink())
                                                    .execute(new HttpCallback() {
                                                        @Override
                                                        public void onSuccess(int code, String msg, String[] info) {
                                                            BookList resultBean = new Gson().fromJson(info[0], BookList.class);
                                                            BookRepository.getInstance().saveChapterInfo(
                                                                    bookId, resultBean.getTitle(), resultBean.getInfo().replace("\\&quot;", "")
                                                            );
                                                            mView.finishChapter();
                                                        }
                                                    });
                                        } else {
                                            HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
                                                    .isMultipart(true)
                                                    .params("anime_id", bookChapter.getBookId())
                                                    .params("chaps", bookChapter.getLink())
                                                    .execute(new HttpCallback() {
                                                        @Override
                                                        public void onSuccess(int code, String msg, String[] info) {
                                                            BookList resultBean = new Gson().fromJson(info[0], BookList.class);
                                                            BookRepository.getInstance().saveChapterInfo(
                                                                    bookId, resultBean.getTitle(), resultBean.getInfo().replace("\\&quot;", "")
                                                            );
                                                            mView.finishChapter();
                                                        }
                                                    });
                                        }
                                    }
                                });
                    } else {
                        HttpClient.getInstance().post(AllApi.chapterinfo, AllApi.chapterinfo)
                                .isMultipart(true)
                                .params("anime_id", bookChapter.getBookId())
                                .params("chaps", bookChapter.getLink())
                                .execute(new HttpCallback() {
                                    @Override
                                    public void onSuccess(int code, String msg, String[] info) {
                                        BookList resultBean = new Gson().fromJson(info[0], BookList.class);
                                        BookRepository.getInstance().saveChapterInfo(
                                                bookId, resultBean.getTitle(), resultBean.getInfo().replace("\\&quot;", "")
                                        );
                                        mView.finishChapter();
                                    }
                                });
                    }
                }
//            chapterInfos.add(chapterInfoSingle);
//
//            titles.add(bookChapter.getTitle());
            }

        } catch (Exception e) {
            LogUtils.e(TAG, " error! " + e);
        }

//        Single.concat(chapterInfos)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Subscriber<ChapterInfoBean>() {
//                            String title = titles.poll();
//
//                            @Override
//                            public void onSubscribe(Subscription s) {
//                                s.request(Integer.MAX_VALUE);
//                                mChapterSub = s;
//                            }
//
//                            @Override
//                            public void onNext(ChapterInfoBean chapterInfoBean) {
//                                //存储数据
//                                BookRepository.getInstance().saveChapterInfo(
//                                        bookId, title, chapterInfoBean.getBody()
//                                );
//                                mView.finishChapter();
//                                //将获取到的数据进行存储
//                                title = titles.poll();
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//                                //只有第一个加载失败才会调用errorChapter
//                                if (bookChapters.get(0).getTitle().equals(title)) {
//                                    mView.errorChapter();
//                                }
//                                LogUtils.e(t);
//                            }
//
//                            @Override
//                            public void onComplete() {
//                            }
//                        }
//                );
    }

    @Override
    public void loadAd() {

    }

    private MMKV mmkv = MMKV.defaultMMKV();

    //是否是vip
    private boolean checkVip() {
        UserBean bean = mmkv.decodeParcelable(SpUtil.USER_INFO, UserBean.class);
        if (bean != null && bean.getIs_vip() != null) {
            return StringUtil.isVip(bean);
        } else {
            return false;
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mChapterSub != null) {
            mChapterSub.cancel();
        }
    }
}
