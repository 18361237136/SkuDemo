# SkuDemo
Sku算法小demo,
Sku的原理
我们正常人的思维是选择一个规格然后去循环遍历看其他的规格是否可选，这样很浪费时间。

sku其实用的就是数学中的求集合的子集。

首先算出有库存的组合的集合的子集，如果被选中的按钮和将要选的按钮组合起来属于子集，那么将要选的按钮属于可点击状态。

比如
a,b,c库存为10件，那么你现在只选择了a,b和 c都可选
