package com.example.skudemo.sku;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Anthor: Zhuangmingzhu
 * Date: 2019/4/25 下午7:46
 * Describe:sku算法工具类
 */
public class SkuUtil {

    /**
     * 计算所有的sku
     * @param initData 所有库存的hashMap组合
     * @return 拆分所有组合产生的所有情况(生成客户端自己的字典)
     */
    public static Map<String,BaseSkuModel> skuCollection(Map<String,BaseSkuModel> initData){
        HashMap<String,BaseSkuModel> result=new HashMap<>();
        //遍历所有库存
        for(String subKey:initData.keySet()){
            BaseSkuModel skuModel=initData.get(subKey);
            //根据;拆分key的组合
            String[] skuAttrs=subKey.split(";");
            //获取所有的组合
            ArrayList<ArrayList<String>> combArr=combInArray(skuAttrs);

            //对应所有组合添加到结果集里面
            for(int i=0;i<combArr.size();i++){
                add2SKUResult(result,combArr.get(i),skuModel);
            }

            String key=TextUtils.join(";",skuAttrs);
            result.put(key,skuModel);
        }
        return result;
    }

    /**
     * 获取所有的组合放到ArrayList里面
     * @param skuKeyAttrs 单个key被； 拆分的数组
     * @return ArrayList
     */
    private static ArrayList<ArrayList<String>> combInArray(String[] skuKeyAttrs){
        if(skuKeyAttrs==null||skuKeyAttrs.length<=0){
            return null;
        }
        int len=skuKeyAttrs.length;
        ArrayList<ArrayList<String>> aResult=new ArrayList<>();
        for(int n=1;n<len;n++){
            ArrayList<Integer[]> aaFlags=getCombFlags(len,n);
            for(int i=0;i<aaFlags.size();i++){
                Integer[] aFlag=aaFlags.get(i);
                ArrayList<String> aComb=new ArrayList<>();
                for(int j=0;j<aFlag.length;j++){
                    if(aFlag[j]==1){
                        aComb.add(skuKeyAttrs[j]);
                    }
                }
                aResult.add(aComb);
            }

        }
        return aResult;
    }

    /**
     * 算法拆分组合 用1和0的移位去做控制
     * @param len
     * @param n
     * @return
     */
    private static ArrayList<Integer[]> getCombFlags(int len,int n){
        if(n<=0){
            return new ArrayList<>();
        }
        ArrayList<Integer[]> aResult=new ArrayList<>();
        Integer[] aFlag=new Integer[len];
        boolean bNext=true;
        int iCnt1=0;
        //初始化
        for(int i=0;i<len;i++){
            aFlag[i]=i<n?1:0;
        }
        aResult.add(aFlag.clone());
        while (bNext){
            iCnt1=0;
            for(int i=0;i<len-1;i++){
                if(aFlag[i]==1&&aFlag[i+1]==0){
                    for(int j=0;j<i;j++){
                        aFlag[j]=j<iCnt1?1:0;
                    }
                    aFlag[i]=0;
                    aFlag[i + 1] = 1;
                    Integer[] aTmp=aFlag.clone();
                    aResult.add(aTmp);
                    if(!TextUtils.join("",aTmp).substring(len-n).contains("0")){
                        bNext=false;
                    }
                    break;
                }
                if(aFlag[i]==1){
                    iCnt1++;
                }
            }
        }
        return aResult;
    }

    //添加到数据集合
    private static void add2SKUResult(HashMap<String, BaseSkuModel> result, ArrayList<String> newKeyList,
                                      BaseSkuModel skuModel){
        String key=TextUtils.join(";",newKeyList);
        if(result.keySet().contains(key)){
            result.get(key).setStock(result.get(key).getStock()+skuModel.getStock());
            result.get(key).setPrice(skuModel.getPrice());
            result.get(key).setPicture(skuModel.getPicture());
        }else{
            result.put(key, new BaseSkuModel(skuModel));
        }
    }

    //所选规格的价格和库存展示
    public static void setBabyShowData(UiData mUiData){
        StringBuilder stringBuilder=new StringBuilder();
        List<String> noChooseName = new ArrayList<>();

        for(int i=0;i<mUiData.getAdapters().size();i++){
            if(mUiData.getAdapters().get(i).getCurrentSelectedItem()==null){
                noChooseName.add(mUiData.getProjecttype().get(i));
            }
        }
        if(noChooseName.size()>0){
            for(int i=0;i<noChooseName.size();i++){
                if(i==0){
                    stringBuilder.append("请选择 ");
                }
                if(i==noChooseName.size()-1){
                    stringBuilder.append(noChooseName.get(i));
                }else{
                    stringBuilder.append(noChooseName.get(i)+" ");
                }
            }
            StringBuilder newStringBuilder = new StringBuilder();
            for (int i = 0; i < mUiData.getAdapters().size(); i++) {
                if (mUiData.getAdapters().get(i).getCurrentSelectedItem()!=null)
                    newStringBuilder.append(mUiData.getAdapters().get(i)
                            .getCurrentSelectedItem().getAttributeMemberId()+";");
            }
            if(newStringBuilder.length()!=0){
                List<String> priceList=new ArrayList<>();
                for(Map.Entry<String,BaseSkuModel> entry:mUiData.getResult().entrySet()){
                    if(mUiData.getSelectedEntities().size()==1){
                        if(entry.getKey().contains(stringBuilder.toString())){
                            priceList.add(entry.getValue().getPicture());
                        }
                    }else{
                        if (entry.getKey().contains(stringBuilder.substring(0, stringBuilder.length() - 1))) {
                            priceList.add(entry.getValue().getPicture());
                        }
                    }
                }
                List<String> newList=ifRepeat(priceList);
                BaseSkuModel baseSkuModel=new BaseSkuModel();
                if(newList.size()==1){
                    baseSkuModel.setPicture(newList.get(0));
                    baseSkuModel.setPrice(mUiData.getBaseSkuModel().getPrice());
                    baseSkuModel.setFormatNum(mUiData.getBaseSkuModel().getFormatNum());
                    baseSkuModel.setStock(mUiData.getBaseSkuModel().getStock());
                }else{
                    baseSkuModel=mUiData.getBaseSkuModel();
                }
                mUiData.setCurrentskumodel(baseSkuModel);
            }else{
                mUiData.setCurrentskumodel(mUiData.getBaseSkuModel());
            }
            mUiData.getSkuPopWindow().showPriceAndSku(mUiData.getCurrentskumodel(),stringBuilder.toString());
        }else{
            for (int i = 0; i < mUiData.getAdapters().size(); i++) {
                if (i == 0){
                    stringBuilder.append("已选： ");
                }
                stringBuilder.append("\""+mUiData.getAdapters().get(i).getCurrentSelectedItem().getName()+"\" ");
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            for (int i = 0; i < mUiData.getAdapters().size(); i++) {
                stringBuilder2.append(mUiData.getAdapters().get(i).getCurrentSelectedItem().getAttributeMemberId()+";");
            }
            if (!TextUtils.isEmpty(stringBuilder.toString())) {
                BaseSkuModel currentmodel = mUiData.getResult().get(stringBuilder.substring(0,
                        stringBuilder.length() - 1));
                mUiData.setCurrentskumodel(currentmodel);
            }else{
                mUiData.setCurrentskumodel(mUiData.getBaseSkuModel());
            }
            mUiData.getSkuPopWindow().showPriceAndSku(mUiData.getCurrentskumodel(),stringBuilder2.toString());
        }
    }

    //如果有重复数据，则去重
    public static List<String> ifRepeat(List<String> list){
        Set set=new HashSet();
        List newList=new ArrayList();
        for(Iterator iter=list.iterator();iter.hasNext();){
            Object element=iter.next();
            if(set.add(element)){
                newList.add(element);
            }
        }
        Log.e("list",list.toString());
        return newList;
    }

    //使用String的split方法
    public static String[] convertStrToArray(String str){
        return str.split(",");
    }

}
