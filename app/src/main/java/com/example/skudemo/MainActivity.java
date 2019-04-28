package com.example.skudemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.skudemo.adapter.SkuAdapter;
import com.example.skudemo.sku.BaseSkuModel;
import com.example.skudemo.sku.GoodsDetailsModel;
import com.example.skudemo.sku.ItemClickListener;
import com.example.skudemo.sku.ProductModel;
import com.example.skudemo.sku.SkuUtil;
import com.example.skudemo.sku.UiData;
import com.example.skudemo.view.SkuPopWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SkuPopWindow.OnItemClickListener {

    private Button showSku;
    private TextView showResult;
    private UiData mUiData;
    private ProductModel productModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String skuData=getFromAssets();
        Gson gson=new Gson();
        Type type=new TypeToken<GoodsDetailsModel>(){}.getType();
        GoodsDetailsModel mAddressEntity=gson.fromJson(skuData,type);
        initData(mAddressEntity);
    }

    private void initView(){
        showSku=findViewById(R.id.show_sku_button);
        showResult=findViewById(R.id.show_result_textView);
        showSku.setOnClickListener(this);

    }

    //从asset中拿到Json数据转化为String
    private String getFromAssets(){
        String json="";
        try {
            InputStreamReader inputStreamReader=new InputStreamReader(getResources().getAssets().open("sku.json"));
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuilder buffer=new StringBuilder("");
            String line="";

            while ((line=bufferedReader.readLine())!=null){
                buffer.append(line);
            }

            json=buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    //处理数据
    private void initData(GoodsDetailsModel skuModels){
        mUiData=new UiData();
        productModel=new ProductModel();
        BaseSkuModel baseSkuModel=new BaseSkuModel();
        GoodsDetailsModel.RowsBean.GoodsBean goodsBean=skuModels.getRows().getGoods();
        baseSkuModel.setPrice(Double.parseDouble(goodsBean.getShowPrice()));
        baseSkuModel.setPicture(goodsBean.getPicture());
        baseSkuModel.setStock(30);
        mUiData.setBaseSkuModel(baseSkuModel);

        for(int i=0;i<goodsBean.getModelAliasSKU().size();i++){
            if(checkIsEmpty(skuModels,i)){
                break;
            }
            goodsBean.getModelAliasSKU().get(i).setModelAlias(
                    SkuUtil.ifRepeat(goodsBean.getModelAliasSKU().get(i).getModelAlias())
            );
            ProductModel.AttributesEntity group01=new ProductModel.AttributesEntity();
            group01.setName(goodsBean.getName());

            for(int j=0;j<goodsBean.getModelAliasSKU().get(i).getModelAlias().size();j++){
                group01.getAttributeMembers().add(j,new ProductModel.AttributesEntity.AttributeMembersEntity(i,(j+1+i*10),
                        goodsBean.getModelAliasSKU().get(i).getModelAlias().get(j)));
            }
            productModel.getAttributes().add(i,group01);//第一组
            String projectName=goodsBean.getModelAliasSKU().get(i).getName();
            mUiData.getProjecttype().add(i,projectName);
        }


        for (int i = 0; i < skuModels.getRows().getGoodsFormatSKU().size(); i++) {
//            product.getProductStocks().put("1;4", new BaseSkuModel(13, 20));
            List<String> testdata = new ArrayList<>();
            String[] bianli = SkuUtil.convertStrToArray(skuModels.getRows().getGoodsFormatSKU().get(i).getKey());
            for (int j = 0; j < bianli.length; j++) {
                if (!(bianli[j].length() == 0|| TextUtils.equals("-",bianli[j]))){
                    testdata.add(bianli[j]);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int z = 0; z < testdata.size(); z++) {
                for (int d = 0; d < skuModels.getRows().getGoods().getModelAliasSKU().get(z).getModelAlias().size(); d++) {
                    if (TextUtils.equals(testdata.get(z),"{"+skuModels.getRows().getGoods().getModelAliasSKU().get(z).getModelAlias().get(d)+"}")){
                        stringBuilder.append((d+1+z*10)+";");
                    }
                }
            }
            if (stringBuilder.length() != 0) {
                String key = stringBuilder.substring(0, stringBuilder.length() - 1);
                productModel.getProductStocks().put(key, new BaseSkuModel(skuModels.getRows()
                        .getGoodsFormatSKU().get(i).getSku_Key()));
            }
        }
        Log.e("---testda",productModel.toString());
    }

    @Override
    public void onClick(View v) {
        if(v==showSku){
            showPop(v);
        }
    }

    private void showPop(View view){
        if(mUiData.getSkuPopWindow()==null){
            mUiData.getSelectedEntities().clear();
            mUiData.getAdapters().clear();
            //添加list组
            for(int i=0;i<productModel.getAttributes().size();i++){
                SkuAdapter skuAdapter = new SkuAdapter(productModel.getAttributes().get(i).getAttributeMembers());
                mUiData.getAdapters().add(skuAdapter);
            }

            //sku计算
            mUiData.setResult(SkuUtil.skuCollection(productModel.getProductStocks()));
            for(String key:mUiData.getResult().keySet()){
                Log.e("SKU Result", "key = " + key + " value = " + mUiData.getResult().get(key));
            }
            //设置点击监听
            for(SkuAdapter adapter:mUiData.getAdapters()){
                ItemClickListener listener = new ItemClickListener(mUiData, adapter);
                adapter.setmOnClickListener(listener);
            }

            //初始化按钮
            initDataTwo();
            SkuPopWindow popWindow=new SkuPopWindow(MainActivity.this,mUiData);
        }
        SkuUtil.setBabyShowData(mUiData);
        mUiData.getSkuPopWindow().showAsDropDown(view);
        mUiData.getSkuPopWindow().setListener(this);
    }

    //初始化按钮的状态（如果该规格或库存为0不可点)
    private void initDataTwo(){
        for (int i = 0; i < mUiData.getAdapters().size(); i++) {
            for (ProductModel.AttributesEntity.AttributeMembersEntity entity : mUiData.getAdapters().get(i).getmAttributeMembersEntities()) {
                if (mUiData.getResult().get(entity.getAttributeMemberId() + "") == null
                        || mUiData.getResult().get(entity.getAttributeMemberId() + "").getStock() <= 0) {
                    entity.setStatus(2);
                }
            }
        }
    }

    //检查数据是否为空
    private boolean checkIsEmpty(GoodsDetailsModel skuModels,int i){
        List<GoodsDetailsModel.RowsBean.GoodsBean.ModelAliasSKUBean> modelAliasSKUBeans=skuModels.getRows().getGoods().getModelAliasSKU();
        return TextUtils.isEmpty(modelAliasSKUBeans.get(i).getName())
                &&(modelAliasSKUBeans.get(i).getModelAlias()==null
                ||modelAliasSKUBeans.get(i).getModelAlias().size()==0);
    }

    @Override
    public void onClickOKPop(String BuyNum) {
        showResult.setText(String.format("选择的商品为：%s\n数量为:%s", mUiData.getCurrentskumodel().toString(), BuyNum));
    }

    @Override
    public void cancel() {

    }
}
