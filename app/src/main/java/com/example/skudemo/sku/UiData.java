package com.example.skudemo.sku;

import com.example.skudemo.adapter.SkuAdapter;
import com.example.skudemo.view.SkuPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/25 下午7:09
 * Describe:sku数据管理类
 */
public class UiData {

    private BaseSkuModel baseSkuModel;

    private List<String> projecttype=new ArrayList<>();

    private BaseSkuModel currentskumodel;//当前选中的模型(不用再去寻找)

    //
    public List<SkuAdapter> adapters=new ArrayList<>();

    //存放计算的结果
    private Map<String,BaseSkuModel> result;

    public Map<String, BaseSkuModel> getResult() {
        return result;
    }

    public void setResult(Map<String, BaseSkuModel> result) {
        this.result = result;
    }

    private boolean isHide;

    //存放被选中的按钮对应的数据
    private List<ProductModel.AttributesEntity.AttributeMembersEntity> selectedEntities=new ArrayList<>();

    public List<ProductModel.AttributesEntity.AttributeMembersEntity> getSelectedEntities() {
        return selectedEntities;
    }

    public void setSelectedEntities(List<ProductModel.AttributesEntity.AttributeMembersEntity> selectedEntities) {
        this.selectedEntities = selectedEntities;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public BaseSkuModel getCurrentskumodel() {
        return currentskumodel;
    }

    public void setCurrentskumodel(BaseSkuModel currentskumodel) {
        this.currentskumodel = currentskumodel;
    }

    public SkuPopWindow skuPopWindow;

    public List<SkuAdapter> getAdapters() {
        return adapters;
    }

    public void setAdapters(List<SkuAdapter> adapters) {
        this.adapters = adapters;
    }

    public SkuPopWindow getSkuPopWindow() {
        return skuPopWindow;
    }

    public void setSkuPopWindow(SkuPopWindow skuPopWindow) {
        this.skuPopWindow = skuPopWindow;
    }

    public List<String> getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(List<String> projecttype) {
        this.projecttype = projecttype;
    }

    public BaseSkuModel getBaseSkuModel() {
        return baseSkuModel;
    }

    public void setBaseSkuModel(BaseSkuModel baseSkuModel) {
        this.baseSkuModel = baseSkuModel;
    }
}
