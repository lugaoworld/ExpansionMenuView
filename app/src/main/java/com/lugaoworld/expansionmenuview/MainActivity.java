package com.lugaoworld.expansionmenuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lugaoworld.expansionmenuviewl.ExpansionMenuView;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private ExpansionMenuView mExpansionMenuView;
    private String[] mMenuArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.id_btn);
        mExpansionMenuView =  (ExpansionMenuView)findViewById(R.id.id_expanmenuview);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeMenuData();
            }
        });

        mMenuArray = new String[]{"苹果","西瓜","梨","草莓"};
        //  String[] mMenuArray = new String[]{"苹果"};
        mExpansionMenuView.setMenuDataSource(mMenuArray);

        mExpansionMenuView.setOnMenuItemSelectListener(new ExpansionMenuView.OnMenuItemSelectListener() {

            @Override
            public void selected(String menu,int position) {

                Toast.makeText(MainActivity.this,"menu:"+menu,Toast.LENGTH_SHORT).show();
            }
        });
    }

    void changeMenuData(){

        mMenuArray = new String[]{"帅哥","美女"};
        mExpansionMenuView.setMenuDataSource(mMenuArray);
    }
}
