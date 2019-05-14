package com.zhkrb.iwara.custom.RefreshView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhkrb.iwara.R;
import com.zhkrb.iwara.utils.SortUtil;

public abstract class RefreshAdapter<T> extends RecyclerView.Adapter {

    protected List<T> mList;
    private Context mContext;
    protected LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private int mLoadMoreState = LOAD_TYPE_NOM;
    private String mErrorMsg = "";
    private LoadMoreCallback mLoadMoreCallback;
    private Vh_more mView_loadmore;


    protected static final int TYPE_MORE = -1;      //加载更多Vh type

    public static final int LOAD_TYPE_NOM = -1;    //加载更多
    public static final int LOAD_TYPE_EMPTY = -2;  //有内容时显示“没有更多内容提示”
    public static final int LOAD_TYPE_ERROR = -3;  //有内容时显示“网络错误提示”

    public void setLoadMoreCallback(LoadMoreCallback loadMoreCallback) {
        mLoadMoreCallback = loadMoreCallback;
    }

    @IntDef({
        LOAD_TYPE_NOM,
        LOAD_TYPE_EMPTY,
        LOAD_TYPE_ERROR
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadMoreType{}


    public RefreshAdapter(Context context) {
        this(context,new ArrayList<T>());
    }

    public RefreshAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        setHasStableIds(true);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public int getItemCount() {
        if (mList !=null){
            return mList.size()+1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MORE){
            if (mView_loadmore ==null){
                mView_loadmore = new Vh_more(mInflater.inflate(R.layout.view_loadmore,parent,false));
            }
            return mView_loadmore;
        }else {
            return onViewHolderCreate(parent,viewType);
        }
    }

    protected abstract RecyclerView.ViewHolder onViewHolderCreate(@NonNull ViewGroup parent,int viewType);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == mList.size()){
            ((Vh_more)holder).setData(mLoadMoreState);
        }else {
            onViewHolderBind(holder,position);
        }
    }

    protected abstract void onViewHolderBind(@NonNull RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        if (mList==null||position==mList.size()){
            return TYPE_MORE;
        }
        return getViewType(position);
    }

    protected abstract int getViewType(int pos);

    public void setLoadState(@LoadMoreType int state,String errorMsg){
        mLoadMoreState = state;
        mErrorMsg = errorMsg;
        notifyItemChanged(mList.size());
    }

    public void setList(List<T> list){
        if (mList==null){
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void refreshData(List<T> list){
        if (mRecyclerView != null) {
            if (mList==null){
                mList = new ArrayList<>();
            }
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void insertList(List<T> list) {
        if (mRecyclerView != null && mList != null && list != null && list.size() > 0) {
            int p = mList.size();
            mList.addAll(list);
            notifyItemRangeInserted(p, list.size());
        }
    }

    public void clearData() {
        if (mRecyclerView != null && mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public interface LoadMoreCallback{
        void loadMore();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getList() {
        return mList;
    }


    class Vh_more extends RecyclerView.ViewHolder {
        int pos;
        ViewGroup mLoading;
        ViewGroup mNo_more;
        ViewGroup mError;
        TextView mError_text;


        Vh_more(@NonNull View itemView) {
            super(itemView);
            mLoading = itemView.findViewById(R.id.load_more);
            mNo_more = itemView.findViewById(R.id.no_more);
            mError = itemView.findViewById(R.id.error);
            mError_text = itemView.findViewById(R.id.error_text);
            mError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLoadMoreCallback!=null){
                        mLoadMoreCallback.loadMore();
                    }
                }
            });
        }


        void setData(@LoadMoreType int state) {
            setAllGone();
            switch (state){
                case LOAD_TYPE_NOM:
                    mLoading.setVisibility(View.VISIBLE);
                    break;
                case LOAD_TYPE_EMPTY:
                    mNo_more.setVisibility(View.VISIBLE);
                    break;
                case LOAD_TYPE_ERROR:
                    mError.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(mErrorMsg)){
                        mError_text.setText(mErrorMsg);
                    }
                    break;
            }
        }

        private void setAllGone(){
            mLoading.setVisibility(View.GONE);
            mNo_more.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);
        }
    }

}
