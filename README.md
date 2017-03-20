# ShoppingCartAnimation

<!-- Baidu Button BEGIN -->

<div id="article_content" class="article_content">

<p><span style="line-height:26px"><span style="font-family:Arial; color:rgb(51,51,51)"><span style="font-size:12px"><strong>前言</strong></span><span style="font-size:14px">：</span></span><span style="font-size:14px"><span style="font-family:SimHei">当我们写商城类的项目的时候，一般都会有加入购物车的功能，加入购物车的时候会有一些抛物线动画，最近做到这个功能，借助别人的demo写了一个。</span></span></span></p>
<p><span style="line-height:26px"><span style="font-size:14px"><span style="font-family:SimHei"><br>
</span></span></span></p>
<p><span style="font-family:SimHei"><span style="font-size:14px"><strong>效果：</strong></span></span></p>
<p><span style="font-family:SimHei"><span style="font-size:14px"><strong><img src="http://wx2.sinaimg.cn/large/006pg0W4gy1fdtfi51rl4g30as0inhdt.gif" width="220" height="360" alt=""><br>
</strong></span></span></p>
<p><strong style="color:rgb(51,51,51); font-family:&quot;Microsoft YaHei&quot;,Arial; font-size:14px"><br>
</strong></p>
<p><strong style="color:rgb(51,51,51); font-family:&quot;Microsoft YaHei&quot;,Arial; font-size:14px">开发环境：AndroidStudio2.1.2&#43;gradle-2.10</strong></p>
<p><span style="font-family:SimHei"><span style="font-size:14px"><strong><br>
</strong></span></span></p>
<p><span style="font-family:SimHei"><span style="font-size:14px"><strong>涉及知识：</strong></span></span><span style="font-size:14px; font-family:SimHei">1.沉浸式状态栏，</span><span style="font-size:14px; font-family:SimHei">2.单位精度计算(价&#26684;)，</span><span style="font-size:14px; font-family:SimHei">3.List之Iterator。</span></p>
<p><span style="font-size:14px; font-family:SimHei"><br>
</span></p>
<p><span style="font-size:14px; font-family:SimHei"><strong>部分代码：</strong></span></p>
<p><span style="font-family:SimHei"></span><pre name="code" class="java" style="font-size: 14px; font-weight: bold;">public class MainActivity extends AppCompatActivity implements FoodAdapter.FoodActionCallback {


    private ListView listView;
    private TextView goods_all_pricetv;
    private TextView good_numtv;
    private List&lt;FoodModel&gt; list;
    private List&lt;FoodModel&gt; selectList = new ArrayList&lt;&gt;();
    private FoodAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT &gt;= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        listView = (ListView) findViewById(R.id.listView);
        goods_all_pricetv = (TextView) findViewById(R.id.goods_all_pricetv);
        good_numtv = (TextView) findViewById(R.id.good_numtv);
        findViewById(R.id.goods_submit_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != selectList &amp;&amp; selectList.size() &gt; 0) {
                    Toast.makeText(MainActivity.this, &quot;你一共加入购物车&quot; + good_numtv.getText() +
                            &quot;份商品,总价格为：&quot; + goods_all_pricetv.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, &quot;你的购物车为空&quot;, Toast.LENGTH_SHORT).show();
                }


            }
        });

        initData();
    }

    private void initData() {
        list = AppConfig.factoryFoods();
        adapter = new FoodAdapter(this, list, this);
        listView.setAdapter(adapter);
    }

    //添加商品动画
    @Override
    public void addAction(View view, int item) {
        ShoppingCartAnimationView shoppingCartAnimationView = new ShoppingCartAnimationView(this);
        int position[] = new int[2];
        view.getLocationInWindow(position);
        shoppingCartAnimationView.setStartPosition(new Point(position[0], position[1]));
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        rootView.addView(shoppingCartAnimationView);
        int endPosition[] = new int[2];
        good_numtv.getLocationInWindow(endPosition);
        shoppingCartAnimationView.setEndPosition(new Point(endPosition[0], endPosition[1]));
        shoppingCartAnimationView.startBeizerAnimation();
        FoodModel model = list.get(item);
        model.setNum(model.getNum() + 1);
        adapter.notifyDataSetChanged();
        calculatePrice();

    }

    //减少商品
    @Override
    public void reduceGood(int position) {
        FoodModel model = list.get(position);
        model.setNum(model.getNum() - 1);
        adapter.notifyDataSetChanged();
        calculatePrice();
    }

    //购物车份数+总价格计算
    private void calculatePrice() {
        selectList.clear();
        double price = 0;
        int num = 0;
        Iterator&lt;FoodModel&gt; iterator = list.iterator();
        while (iterator.hasNext()) {
            FoodModel model = iterator.next();
            if (model.getNum() != 0) {
                selectList.add(model);
                price += model.getPrice() * model.getNum();
                num += model.getNum();
            }
        }
        goods_all_pricetv.setText(&quot;￥&quot; + adapter.priceResult(price) + &quot;元&quot;);
        good_numtv.setText(num + &quot;&quot;);
    }
}
</pre><br>
</p>
</div>

<!-- Baidu Button END -->
