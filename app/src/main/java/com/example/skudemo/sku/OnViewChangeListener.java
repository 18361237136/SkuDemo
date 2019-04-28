package com.example.skudemo.sku;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/26 上午10:52
 * Describe:更新视图接口类
 */
public interface OnViewChangeListener {

    //更新价格和库存
    void showPriceAndSku(BaseSkuModel baseSkuModel,String sku);
}
