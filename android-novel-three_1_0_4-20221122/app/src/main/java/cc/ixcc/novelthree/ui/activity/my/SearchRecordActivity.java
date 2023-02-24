package cc.ixcc.novelthree.ui.activity.my;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.action.TitleBarAction;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.bean.ReadHistoryBean;
import cc.ixcc.novelthree.common.MyActivity;
import cc.ixcc.novelthree.helper.BindEventBus;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.http.MainHttpUtil;
import cc.ixcc.novelthree.jsReader.Activity.JsReadActivity;
import cc.ixcc.novelthree.jsReader.model.bean.BookChapterBean;
import cc.ixcc.novelthree.jsReader.model.bean.CollBookBean;
import cc.ixcc.novelthree.jsReader.utils.ToastUtils;
import cc.ixcc.novelthree.other.KeyboardWatcher;
import cc.ixcc.novelthree.ui.adapter.RefreshAdapter;
import cc.ixcc.novelthree.ui.adapter.SearchRecordAdapter;
import cc.ixcc.novelthree.ui.dialog.AddShelveDialog;
import cc.ixcc.novelthree.ui.dialog.TipDialog;
import cc.ixcc.novelthree.widget.CommonRefreshView;
import cc.ixcc.novelthree.widget.viewhoder.ItemDecoration;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * desc   : 阅读记录
 */
@BindEventBus
public final class SearchRecordActivity extends MyActivity implements TitleBarAction, KeyboardWatcher.SoftKeyboardStateListener {
    @BindView(R.id.refreshView)
    CommonRefreshView mRefreshLayout;
    @BindView(R.id.toolbar)
    TitleBar mToolBar;
    private SearchRecordAdapter mSearchRecordAdapter;
    private SwipeMenuCreator swipeMenuCreator;
    private SwipeMenuItemClickListener swipeMenuItemClickListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_record;
    }

    @Override
    protected void initView() {
        initSearchRecordRv();
        ImmersionBar.setTitleBar(this, mToolBar);
        mRefreshLayout.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        ItemDecoration decoration = new ItemDecoration(getContext(), 0x00000000, 5, 0);
        decoration.setOnlySetItemOffsetsButNoDraw(true);
        mRefreshLayout.setItemDecoration(decoration);
        mRefreshLayout.setDataHelper(new CommonRefreshView.DataHelper<ReadHistoryBean.ListBean>() {
            @Override
            public RefreshAdapter<ReadHistoryBean.ListBean> getAdapter() {
                if (mSearchRecordAdapter == null) {
                    mSearchRecordAdapter = new SearchRecordAdapter(getContext());
                    mSearchRecordAdapter.setOnClickListener(new SearchRecordAdapter.OnClickListener() {
                        @Override
                        public void deleteRecord(int position, ReadHistoryBean.ListBean bean) {
                            showDeleteDialog(position, bean);
                        }

                        @Override
                        public void addBook(int position, ReadHistoryBean.ListBean bean) {
                            if (bean.getIs_shelve().equals(Constants.SHELVE_EXIST)) {
                                removeshelve(position, bean);
                                return;
                            }
                            addshelve(position, bean);
                        }

                        @Override
                        public void readBook(int position, ReadHistoryBean.ListBean bean) {
                            read(bean.getAnid(), "1", bean.getIs_shelve());
                        }
                    });

                }
                return mSearchRecordAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                MainHttpUtil.readhistory(p, PAGE_SIZE, callback);
            }

            @Override
            public List<ReadHistoryBean.ListBean> processData(String[] info) {
                if (info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    return JSON.parseArray(obj.getString("list"), ReadHistoryBean.ListBean.class);
                }
                return null;
            }

            @Override
            public void onRefreshSuccess(List<ReadHistoryBean.ListBean> list, int listCount) {
            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<ReadHistoryBean.ListBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
    }

    TipDialog deletedialog;

    private void showDeleteDialog(int position, ReadHistoryBean.ListBean bean) {
        deletedialog = TipDialog.getMyDialog(this);
        deletedialog.setTitle("Do you want to delete this record?");
        //回调实现点击
        deletedialog.setDialogCallBack(new TipDialog.DialogCallBack() {
            @Override
            public void onActionClick() {
                deleterecord(position, bean);
            }
        });
        deletedialog.show();
    }

    AddShelveDialog adddialog;

    private void showAddShelveDialog(int position, ReadHistoryBean.ListBean bean) {
        //获取实体类
        adddialog = AddShelveDialog.getMyDialog(this);
        //回调实现点击
        adddialog.setDialogCallBack(new AddShelveDialog.DialogCallBack() {
            @Override
            public void onActionClick(String code) {
                addshelve(position, bean);
            }
        });
        adddialog.show();
    }

    TipDialog movedialog;
//    private void showRemoveShelveDialog(int position, ReadHistoryBean.ListBean bean) {
//        movedialog = TipDialog.getMyDialog(this);
//        movedialog.setTitle("Do you want to remove my book?");
//        //回调实现点击
//        movedialog.setDialogCallBack(new TipDialog.DialogCallBack() {
//            @Override
//            public void onActionClick() {
//                removeshelve(position, bean);
//            }
//        });
//        movedialog.show();
//    }

    private void addshelve(int position, ReadHistoryBean.ListBean bean) {
        HttpClient.getInstance().get(AllApi.addshelve, AllApi.addshelve)
                .params("anime_id", bean.getAnid())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (adddialog != null && adddialog.isShowing()) adddialog.dismiss();
                        bean.setIs_shelve(Constants.SHELVE_EXIST);
                        mSearchRecordAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleterecord(int position, ReadHistoryBean.ListBean bean) {
        HttpClient.getInstance().get(AllApi.deletereadhistory, AllApi.deletereadhistory)
                .params("id", bean.getAnid())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (deletedialog != null && deletedialog.isShowing())
                            deletedialog.dismiss();
                        mSearchRecordAdapter.getList().remove(position);
                        mSearchRecordAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void removeshelve(int position, ReadHistoryBean.ListBean bean) {
        HttpClient.getInstance().get(AllApi.removeshelve, AllApi.removeshelve)
                .params("id", bean.getAnid())
                .execute(new HttpCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        if (movedialog != null && movedialog.isShowing()) movedialog.dismiss();
                        bean.setIs_shelve(Constants.SHELVE_NO_EXIST);
                        mSearchRecordAdapter.notifyDataSetChanged();
                    }
                });
    }

    //TODO 替换为JsReadActivity
    public void read(int anime_id, String chaps, String isshelve) {
        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
                .isMultipart(true)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
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
                            bookChapterBean.setIs_pay(TextUtils.equals(chaptersBean.getIs_pay(), "1"));
                            chapters.add(bookChapterBean);
                        }
                        CollBookBean collBookBean = new CollBookBean();
                        collBookBean.set_id(bookId + "");
                        collBookBean.setAuthor(chapterBean.getAuthor());
                        collBookBean.setTitle(chapterBean.getTitle());
                        collBookBean.setCoverPic(chapterBean.getCoverpic());
                        collBookBean.setShareTitle(chapterBean.getSharetitle());
                        collBookBean.setShortIntro(chapterBean.getSharedesc());
                        collBookBean.setShare_link(chapterBean.getShare_link());
                        collBookBean.setChaptersCount(chapterBean.getAllchapter());
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
                        JsReadActivity.startActivity(getContext(), collBookBean, false, Integer.parseInt(chaps), isshelve, false);
                    }
                });
    }

    private void initSearchRecordRv() {
        setSwipeMenu();//设置菜单
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void setSwipeMenu() {
        swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = 200;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(SearchRecordActivity.this)
                        .setBackground(R.color.red)
                        .setText("delete")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        };

        swipeMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection();//左边还是右边菜单
                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                    toast("Delete the " + position + " th entry");
                    mSearchRecordAdapter.getList().remove(position);
                    mSearchRecordAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    protected void initData() {
        if (mRefreshLayout != null) {
            mRefreshLayout.initData();
        }
    }

    @Override
    public void onRightClick(View v) {
        if (mRefreshLayout == null || mSearchRecordAdapter == null ||mSearchRecordAdapter.getItemCount()==0) {
            ToastUtils.show("No more");
        } else {
            showEmptyRecordDialog();
        }
    }

    private void showEmptyRecordDialog() {
        TipDialog dialog = TipDialog.getMyDialog(this);
        dialog.setTitle("Empty reading books");
        //回调实现点击
        dialog.setDialogCallBack(new TipDialog.DialogCallBack() {
            @Override
            public void onActionClick() {
                MainHttpUtil.emptyreadhistory(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        mRefreshLayout.initData();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_clear:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {

    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(MessageEvent messageEvent) {
        // 处理数据
    }

    // 自定义一个事件数据类，主要用来传递数据。
    public class MessageEvent {
        private String message;
        public MessageEvent(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }
}