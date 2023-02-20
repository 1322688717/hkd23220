package cc.ixcc.novelthree.ui.fragment.myFragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.fastjson.JSON;
import cc.ixcc.novelthree.Constants;
import cc.ixcc.novelthree.R;
import cc.ixcc.novelthree.bean.MyMessagebean;
import cc.ixcc.novelthree.common.MyFragment;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.MainHttpUtil;
import cc.ixcc.novelthree.ui.activity.HomeActivity;
import cc.ixcc.novelthree.ui.activity.my.BookPlDetailActivity;
import cc.ixcc.novelthree.ui.activity.my.SystemDetailActivity;
import cc.ixcc.novelthree.ui.adapter.RefreshAdapter;
import cc.ixcc.novelthree.ui.adapter.myAdapter.MyMessageAdapter;
import cc.ixcc.novelthree.ui.dialog.DeleteMessageDialog;
import cc.ixcc.novelthree.ui.dialog.MessageDetailDialog;
import cc.ixcc.novelthree.widget.CommonRefreshView;
import cc.ixcc.novelthree.widget.viewhoder.ItemDecoration;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static cc.ixcc.novelthree.Constants.PAGE_SIZE;

/**
 * 我的消息viewPage
 */
public class MyMessageFragment extends MyFragment<HomeActivity> {
    @BindView(R.id.refreshView)
    CommonRefreshView mRefreshLayout;
    MyMessageAdapter mMessageAdapter;
    String mCatType;
    String mType;

    public static MyMessageFragment newInstance(String cat_type, String type) {
        Bundle args = new Bundle();
        args.putString("cat_type", cat_type);
        args.putString("type", type);
        MyMessageFragment fragment = new MyMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_message;
    }

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        this.isVisible = isVisibleToUser;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
//        EventBus.getDefault().unregister(this);
//        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCatType = arguments.getString("cat_type");
            mType = arguments.getString("type");
        }
        initRefresh();
    }

    @Override
    protected void initData() {
        if (mRefreshLayout != null) {
            mRefreshLayout.initData();
        }
    }

    private void initRefresh() {
        try {
            mRefreshLayout.setEmptyLayoutId(R.layout.view_no_message);
            mRefreshLayout.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
            ItemDecoration decoration = new ItemDecoration(getContext(), 0x00000000, 5, 0);
            decoration.setOnlySetItemOffsetsButNoDraw(true);
            mRefreshLayout.setItemDecoration(decoration);
            mRefreshLayout.setDataHelper(new CommonRefreshView.DataHelper<MyMessagebean>() {
                @Override
                public RefreshAdapter<MyMessagebean> getAdapter() {
                    if (mMessageAdapter == null) {
                        mMessageAdapter = new MyMessageAdapter(mType, getContext());
                        mMessageAdapter.setOnClickListener(new MyMessageAdapter.OnClickListener() {
                            @Override
                            public void ActionClick(String type, int position, MyMessagebean bean) {
                                if (type.equals(Constants.TYPE_NOTICE)) {
                                    readMessage(position, bean);
                                } else
                                    showMessageDetailDialog(type, position, bean);
                            }
                        });

                    }
                    return mMessageAdapter;
                }

                @Override
                public void loadData(int p, HttpCallback callback) {
                    MainHttpUtil.getMessage(mCatType, mType, p, PAGE_SIZE, callback);
                }

                @Override
                public List<MyMessagebean> processData(String[] info) {
                    return JSON.parseArray(Arrays.toString(info), MyMessagebean.class);
                }

                @Override
                public void onRefreshSuccess(List<MyMessagebean> list, int listCount) {
//                if (CommonAppConfig.LIVE_ROOM_SCROLL) {
//                    LiveStorge.getInstance().put(Constants.LIVE_CLASS_PREFIX + mClassId, list);
//                }
                }

                @Override
                public void onRefreshFailure() {

                }

                @Override
                public void onLoadMoreSuccess(List<MyMessagebean> loadItemList, int loadItemCount) {

                }

                @Override
                public void onLoadMoreFailure() {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readMessage(int position, MyMessagebean bean) {
        MainHttpUtil.readmessage(bean.getType(), bean.getId() + "", new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                bean.setRead_status("1");
                mMessageAdapter.notifyDataSetChanged();
                SystemDetailActivity.start(getContext(), bean);
            }
        });
    }

    private void showMessageDetailDialog(String type, int position, MyMessagebean bean) {
        final MessageDetailDialog dialog = MessageDetailDialog.getMyDialog(getAttachActivity());
        //回调实现点击
        dialog.setDialogCallBack(new MessageDetailDialog.DialogCallBack() {
            @Override
            public void onTitleClick() {
                BookPlDetailActivity.start(getContext(), bean.getComment_id());
                dialog.dismiss();
            }

            @Override
            public void onTitle2Click() {
                showDeleteDialog(type, position, bean);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDeleteDialog(String type, int position, MyMessagebean bean) {
        DeleteMessageDialog dialog = DeleteMessageDialog.getMyDialog(getAttachActivity());
        //回调实现点击
        dialog.setDialogCallBack(new DeleteMessageDialog.DialogCallBack() {
            @Override
            public void onActionClick() {
                MainHttpUtil.deleteMessage(type, bean.getId() + "", new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        dialog.dismiss();
                        mMessageAdapter.getList().remove(position);
                        mMessageAdapter.notifyDataSetChanged();
                        if (mMessageAdapter.getList().size() == 0) {
                            mRefreshLayout.initData();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

}
