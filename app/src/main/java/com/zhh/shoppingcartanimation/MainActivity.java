package com.zhh.shoppingcartanimation;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhh.shoppingcartanimation.adapter.FoodAdapter;
import com.zhh.shoppingcartanimation.datas.AppConfig;
import com.zhh.shoppingcartanimation.model.FoodModel;
import com.zhh.shoppingcartanimation.view.ShoppingCartAnimationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FoodAdapter.FoodActionCallback {


    private ListView listView;
    private TextView goods_all_pricetv;
    private TextView good_numtv;
    private List<FoodModel> list;
    private List<FoodModel> selectList = new ArrayList<>();
    private FoodAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
                if (null != selectList && selectList.size() > 0) {
                    Toast.makeText(MainActivity.this, "你一共加入购物车" + good_numtv.getText() +
                            "份商品,总价格为：" + goods_all_pricetv.getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "你的购物车为空", Toast.LENGTH_SHORT).show();
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
        Iterator<FoodModel> iterator = list.iterator();
        while (iterator.hasNext()) {
            FoodModel model = iterator.next();
            if (model.getNum() != 0) {
                selectList.add(model);
                price += model.getPrice() * model.getNum();
                num += model.getNum();
            }
        }
        goods_all_pricetv.setText("￥" + adapter.priceResult(price) + "元");
        good_numtv.setText(num + "");
    }
}
