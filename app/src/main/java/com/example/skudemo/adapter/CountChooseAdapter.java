package com.example.skudemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skudemo.R;
import com.example.skudemo.sku.UiData;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/26 下午6:10
 * Describe:规格选择adapter(最下面是采购数量)
 */
public class CountChooseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int STATE_HIDE = 1;
    public static final int STATE_Max = 2;//大于当前库存最大数字
    public static final int STATE_Min = 3;//小于当前库存最小数

    public static final int VIEW_TYPE_FOOTER = -1;

    private int mState;
    private Context context;
    private UiData mUiData;
    boolean isHide;//true表示修改规格false显示购买数量
    int number;

    StandardFootView standardFootView;

    public StandardFootView getStandardFootView() {
        return standardFootView;
    }

    public void setStandardFootView(StandardFootView standardFootView) {
        this.standardFootView = standardFootView;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public CountChooseAdapter(Context context) {
        this.context = context;
        mState=STATE_HIDE;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.standard_top_item, viewGroup, false);
            viewHolder = new StandardFootView(view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_standard,viewGroup, false);
            viewHolder = new SkuViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder.getItemViewType()==VIEW_TYPE_FOOTER){
            ((StandardFootView)viewHolder).initView();
        }else{
            if(mUiData!=null){
                ((SkuViewHolder)viewHolder).initView(mUiData.getAdapters().get(position),mUiData.getProjecttype().get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mUiData==null?0:(isHide?mUiData.getAdapters().size():mUiData.getAdapters().size()+1);
    }

    @Override
    public int getItemViewType(int position) {
        if(!isHide){
            if(position+1==getItemCount()){
                return VIEW_TYPE_FOOTER;
            }
        }
        return super.getItemViewType(position);
    }

    public void setState(int state) {
        this.mState = state;
        updateItem(getItemCount() - 1);
    }

    public void add(UiData mList) {
        mUiData = mList;
    }

    public void updateItem(int position){
        if(getItemCount()>position){
            notifyItemChanged(position);
        }
    }

    public class StandardFootView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView pop_add, pop_reduce;
        public TextView pop_num;

        public StandardFootView(@NonNull View itemView) {
            super(itemView);
            pop_add = itemView.findViewById(R.id.pop_add);
            pop_reduce = itemView.findViewById(R.id.pop_reduce);
            pop_num = itemView.findViewById(R.id.pop_num);
            pop_num.setText("1");
            number = 1;
            pop_reduce.setImageResource(R.drawable.subunselect_number);
            pop_reduce.setEnabled(false);
            pop_reduce.setOnClickListener(this);
            pop_add.setOnClickListener(this);
            if (mUiData.getCurrentskumodel().getStock() == 0) {
                pop_reduce.setImageResource(R.drawable.subunselect_number);
                pop_reduce.setEnabled(false);
                pop_num.setText("0");
                number = 0;
                pop_add.setImageResource(R.drawable.addunselect_number);
                pop_add.setEnabled(false);
            }
        }

        private void initView() {
            number = Integer.parseInt(pop_num.getText().toString());
            if (mState == STATE_Max) {
                if (checkStock()) {
                    number = Integer.parseInt(mUiData.getBaseSkuModel().getStock() + "");
                    pop_num.setText(number + "");
                    pop_add.setImageResource(R.drawable.addunselect_number);
                    pop_add.setEnabled(false);
                    pop_reduce.setImageResource(R.drawable.sub_number);
                    pop_reduce.setEnabled(true);
                }

                if (mUiData.getCurrentskumodel().getStock() != 0 && number == 0) {
                    number = 1;
                    pop_num.setText("1");
                    pop_reduce.setImageResource(R.drawable.subunselect_number);
                    pop_reduce.setEnabled(false);
                    pop_add.setImageResource(R.drawable.add_number);
                    pop_add.setEnabled(true);
                }
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_add:
                    addNum();
                    break;
                case R.id.pop_reduce:
                    subNum();
                    break;
            }
        }

        /**
         * 增加数量
         */
        private void addNum() {
            String strNum = pop_num.getText().toString();
            int num = Integer.parseInt(strNum);
            if (mUiData.getCurrentskumodel().getStock() - num > 0) {
                int num_add = Integer.parseInt(strNum) + 1;
                pop_num.setText(String.valueOf(num_add));
                number = num_add;
            } else {
                pop_add.setImageResource(R.drawable.addunselect_number);
                pop_add.setEnabled(false);
            }
            pop_reduce.setImageResource(R.drawable.sub_number);
            pop_reduce.setEnabled(true);
        }

        /**
         * 减小数量
         */
        private void subNum() {
            String strNum = pop_num.getText().toString();
            if (Integer.parseInt(strNum) > 1) {
                int num_reduce = Integer.parseInt(strNum) - 1;
                pop_num.setText(String.valueOf(num_reduce));
                number = num_reduce;
            } else {
                pop_reduce.setImageResource(R.drawable.subunselect_number);
                pop_reduce.setEnabled(false);
            }
            pop_add.setImageResource(R.drawable.add_number);
            pop_add.setEnabled(true);
        }

    }

    private boolean checkStock() {
        return mUiData != null &&
                mUiData.getCurrentskumodel() != null && (mUiData.getCurrentskumodel().getStock() <= number);
    }

    public class SkuViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        private TagFlowLayout tagFlowLayout;
        private TextView standard_tv;

        public SkuViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rootView = itemView;
            tagFlowLayout = (TagFlowLayout) itemView.findViewById(R.id.standard_fl);
            standard_tv = (TextView) itemView.findViewById(R.id.standard_tv);
        }
        private void initView(SkuAdapter skuAdapter, String title){
            if(skuAdapter!=null){
                tagFlowLayout.setOnTagClickListener(skuAdapter.getmOnClickListener());
                tagFlowLayout.setAdapter(skuAdapter);
            }
            standard_tv.setText(title);
        }
    }
}
