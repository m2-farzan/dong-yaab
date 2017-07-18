package com.mostafa.dong.dong;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forceRTLIfSupported();
        //****
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("مراحل تسویه حساب");
        //*****
        loadData(getIntent());
        strikeOption();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    private void loadData(Intent x)
    {
        if(x==null)
            return;
        String[] s=new String[5];
        for(int i=0;i<5;i++)
            if(x.getStringExtra(Integer.toString(i)).equals(""))
                ((TableRow)ti(i).getParent()).setVisibility(View.GONE);
            else
                ti(i).setText(x.getStringExtra(Integer.toString(i)));

    }

    private TextView ti(int x)
    {
        if(x==1)
            return (TextView)findViewById(R.id.x1);
        if(x==2)
            return (TextView)findViewById(R.id.x2);
        if(x==3)
            return (TextView)findViewById(R.id.x3);
        if(x==4)
            return (TextView)findViewById(R.id.x4);
        //if(x==0)
            return (TextView)findViewById(R.id.x0);
    }

    private void strikeOption()
    {
        for(int i=0;i<5;i++)
        {
            ((TableRow)ti(i).getParent()).setTag(false);
            ((TableRow)ti(i).getParent()).setOnClickListener(rowl);
        }
    }
    View.OnClickListener rowl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!(Boolean)view.getTag()) {
                ((TextView)(((TableRow)view).getChildAt(0))).setPaintFlags(((TextView)(((TableRow)view).getChildAt(0))).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((TableRow) view).setBackgroundColor(getResources().getColor(R.color.lgn));
                view.setTag(true);
            }
            else
            {

                ((TextView)(((TableRow)view).getChildAt(0))).setPaintFlags(((TextView)(((TableRow)view).getChildAt(0))).getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                ((TableRow) view).setBackgroundColor(getResources().getColor(R.color.bg));
                view.setTag(false);
            }
        }
    };

}
