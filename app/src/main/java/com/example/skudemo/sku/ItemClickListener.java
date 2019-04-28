package com.example.skudemo.sku;

import android.view.View;

import com.example.skudemo.adapter.SkuAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/28 上午10:23
 * Describe:sku观察者回调类
 */
public class ItemClickListener implements TagFlowLayout.OnTagClickListener {

    private UiData mUiData;
    private SkuAdapter currentAdapter;

    public ItemClickListener(UiData mUiData, SkuAdapter currentAdapter) {
        this.mUiData = mUiData;
        this.currentAdapter = currentAdapter;
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        if(currentAdapter.getmAttributeMembersEntities().get(position).getStatus()==2){
            return true;
        }
        //设置当前单选点击
        for(ProductModel.AttributesEntity.AttributeMembersEntity entity:currentAdapter.getmAttributeMembersEntities()){
            if(entity.equals(currentAdapter.getmAttributeMembersEntities().get(position))){
                if(entity.getStatus()==0){
                    entity.setStatus(1);
                    //添加已经选择的对象
                    currentAdapter.setCurrentSelectedItem(entity);
                }else{
                    //如果之前已经选中则取消该按钮（删除节点)
                    currentAdapter.setCurrentSelectedItem(null);
                }
            }else {
                entity.setStatus(entity.getStatus()==2?2:0);
            }
        }
        //存放当前被点击的按钮,status=2表示没有库存，按钮为不可选状态，=1表示被选中状态，=0表示可选状态
        mUiData.getSelectedEntities().clear();
        for(int i=0;i<mUiData.getAdapters().size();i++){
            for(ProductModel.AttributesEntity.AttributeMembersEntity entity:mUiData.getAdapters().get(i).getmAttributeMembersEntities()){
                //处理没有数据和没有库存(检测单个)
                if(mUiData.getResult().get(entity.getAttributeMemberId()+"")==null||
                        mUiData.getResult().get(entity.getAttributeMemberId()+"").getStock()<=0){
                    entity.setStatus(2);
                }else if(entity.equals(mUiData.getAdapters().get(i).getCurrentSelectedItem())){
                    entity.setStatus(1);
                }else{
                    entity.setStatus(0);
                }
                //冒泡排序
                List<ProductModel.AttributesEntity.AttributeMembersEntity> cacheSelected=new ArrayList<>();
                cacheSelected.add(entity);
                cacheSelected.addAll(mUiData.getSelectedEntities());
                for(int j=0;j<cacheSelected.size()-1;j++){
                    for(int k=0;k<cacheSelected.size()-j;k++){
                        ProductModel.AttributesEntity.AttributeMembersEntity cacheEntity;
                        if(cacheSelected.get(k).getAttributeGroupId()>cacheSelected.get(k+1).getAttributeGroupId()){
                            cacheEntity = cacheSelected.get(k);
                            cacheSelected.set(k, cacheSelected.get(k + 1));
                            cacheSelected.set(k + 1, cacheEntity);
                        }

                    }
                }

                for(int j=0;j<cacheSelected.size()-1;i++){
                    for (int k = 0; k < cacheSelected.size() - 1 - j; k++) {
                        if (cacheSelected.get(k).getAttributeGroupId() == cacheSelected.get(k + 1).getAttributeGroupId()) {
                            cacheSelected.remove(k + 1);
                        }
                    }
                }
                StringBuffer buffer = new StringBuffer();
                for (ProductModel.AttributesEntity.AttributeMembersEntity selectedEntity : cacheSelected) {
                    buffer.append(selectedEntity.getAttributeMemberId() + ";");
                }
//                Log.e(TAG,"cacheSelected"+cacheSelected.toString());
//                Log.e(TAG, "key = " + buffer.substring(0, buffer.length() - 1));
                //TODO 检查数据
                if (mUiData.getResult().get(buffer.substring(0, buffer.length() - 1)) != null && mUiData.getResult().get(buffer.substring(0, buffer.length() - 1)).getStock() > 0) {
                    entity.setStatus(entity.getStatus() == 1 ? 1 : 0);
                } else {
                    entity.setStatus(2);
                }
            }
            mUiData.getAdapters().get(i).notifyDataChanged();
        }
        SkuUtil.setBabyShowData(mUiData);
        return true;
    }
}
