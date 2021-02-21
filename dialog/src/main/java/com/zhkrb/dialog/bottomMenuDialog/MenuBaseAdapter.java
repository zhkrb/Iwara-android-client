package com.zhkrb.dialog.bottomMenuDialog;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhkrb.dialog.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MenuBaseAdapter<T> extends RecyclerView.Adapter<MenuBaseAdapter<T>.MenuBaseViewHolder> {

    protected List<T> mList = new ArrayList();
    private final LayoutInflater mLayoutInflater;
    private final View.OnClickListener mClickListener;
    private MenuSelectInterface mSelectInterface;

    public MenuBaseAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mClickListener = v -> {
            if (!BaseDialog.canClick()) {
                return;
            }
            if (mSelectInterface != null) {
                int pos = (int) v.getTag();
                Object object = mList.get(pos);
                mSelectInterface.onMenuSelect(object, pos);
            }
        };
    }

    @NonNull
    @Override
    public MenuBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createHolder(mLayoutInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuBaseViewHolder holder, int position) {
        holder.preBind(position);
        convert(holder, position);
    }

    public void setList(List<T> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 创建viewHolder
     *
     * @param inflater
     * @param parent
     * @param viewType
     * @return
     */
    public abstract MenuBaseViewHolder createHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    protected abstract void convert(MenuBaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setSelectInterface(MenuSelectInterface selectInterface) {
        mSelectInterface = selectInterface;
    }


    public class MenuBaseViewHolder extends RecyclerView.ViewHolder {

        private final View mView;

        public MenuBaseViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void preBind(int pos) {
            mView.setTag(pos);
            mView.setOnClickListener(mClickListener);
        }


        public View findView(@IdRes int id) {
            return mView.findViewById(id);
        }

        public View getRootView() {
            return mView;
        }
    }

    public interface MenuSelectInterface {

        /**
         * 选择item
         *
         * @param data
         * @param pos
         */
        void onMenuSelect(Object data, int pos);
    }

}
