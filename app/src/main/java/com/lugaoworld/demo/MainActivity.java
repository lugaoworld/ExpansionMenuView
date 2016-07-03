package com.lugaoworld.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lugaoworld.expansionmenuview.ExpansionMenuView;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private ExpansionMenuView mExpansionMenuView;
    private ExpansionMenuView mExpansionMenuView2;
    private String[] mMenuArray;
    private String[] mMenuArray2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.id_btn);
        mExpansionMenuView =  (ExpansionMenuView)findViewById(R.id.id_expanmenuview);
        mExpansionMenuView2 =  (ExpansionMenuView)findViewById(R.id.id_expanmenuview2);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeMenuData();
            }
        });

        mMenuArray = new String[]{"帅哥","美女"};
        mExpansionMenuView.setMenuDataSource(mMenuArray);
        mExpansionMenuView.setOnMenuItemSelectListener(new ExpansionMenuView.OnMenuItemSelectListener() {

            @Override
            public void selected(String menu,int position) {

                Toast.makeText(MainActivity.this,"menu:"+menu,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void autoclose() {

                if(mExpansionMenuView2.getExpanState()){
                    mExpansionMenuView2.autoClose();
                }

            }
        });

        mMenuArray2 = new String[]{"北京","上海"};
        mExpansionMenuView2.setMenuDataSource(mMenuArray2);


        mExpansionMenuView2.setOnMenuItemSelectListener(new ExpansionMenuView.OnMenuItemSelectListener() {

            @Override
            public void selected(String menu,int position) {

                Toast.makeText(MainActivity.this,"menu:"+menu,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void autoclose() {
                if(mExpansionMenuView.getExpanState()){
                    mExpansionMenuView.autoClose();
                }
            }
        });
    }

    void changeMenuData(){

        mMenuArray = new String[]{"苹果","西瓜","梨","草莓"};
        mMenuArray2 = new String[]{"中国","美国","新加坡"};

        mExpansionMenuView.setMenuDataSource(mMenuArray);
        mExpansionMenuView2.setMenuDataSource(mMenuArray2);
    }
}
