package com.example.skudemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.skudemo.R;
import com.example.skudemo.sku.ProductModel;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/26 下午7:04
 * Describe:Tag的adapter
 */
public class SkuAdapter extends TagAdapter<ProductModel.AttributesEntity.AttributeMembersEntity> {

    private List<ProductModel.AttributesEntity.AttributeMembersEntity> mAttributeMembersEntities;
    TagFlowLayout.OnTagClickListener mOnClickListener;
    private ProductModel.AttributesEntity.AttributeMembersEntity currentSelectedItem;

    public ProductModel.AttributesEntity.AttributeMembersEntity getCurrentSelectedItem() {
        return currentSelectedItem;
    }

    public void setCurrentSelectedItem(ProductModel.AttributesEntity.AttributeMembersEntity currentSelectedItem) {
        this.currentSelectedItem = currentSelectedItem;
    }

    public List<ProductModel.AttributesEntity.AttributeMembersEntity> getmAttributeMembersEntities() {
        return mAttributeMembersEntities;
    }

    public TagFlowLayout.OnTagClickListener getmOnClickListener() {
        return mOnClickListener;
    }

    public void setmOnClickListener(TagFlowLayout.OnTagClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public SkuAdapter(List<ProductModel.AttributesEntity.AttributeMembersEntity> attributeMembersEntities) {
        super(attributeMembersEntities);
        this.mAttributeMembersEntities = attributeMembersEntities;

    }

    @Override
    public View getView(FlowLayout parent, int position, ProductModel.AttributesEntity.AttributeMembersEntity entity) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sku_item_layout, parent, false);
        TextView mTextView = v.findViewById(R.id.tv);
        mTextView.setText(entity.getName());
        switch (entity.getStatus()) {
            case 0:
                mTextView.setBackgroundResource(R.drawable.shape_radius_bg);
                mTextView.setTextColor(parent.getContext().getResources().getColor(R.color.text_black_color));
                break;
            case 1:
                mTextView.setBackgroundResource(R.drawable.shape_radius_bg2);
                mTextView.setTextColor(parent.getContext()
                        .getResources().getColor(R.color.white));
                break;
            case 2:
                mTextView.setBackgroundResource(R.drawable.shape_radius_nobg);
                mTextView.setTextColor(parent.getContext().getResources().getColor(R.color.select_gray));
                break;
        }
        return mTextView;
    }
}
