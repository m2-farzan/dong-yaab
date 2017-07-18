package com.mostafa.dong.dong;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class users extends AppCompatActivity {
    Intent first;
    int N=0;
    String[] Names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //RTL
        forceRTLIfSupported();
        //Layout load
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        //Title set
        setTitle("تغییر افراد");
        //Read intent data
        Names=new String[6];
        for (int i=0;i<6;i++)
            Names[i]="";
        first=getIntent();
        Bundle data=first.getExtras();
        if(data!=null) {
            N = data.getInt("N", 0);
            for(int i=0;i<N;i++)
                Names[i]=data.getString(Integer.toString(i),Integer.toString(i));
        }
        ui_setupTable();
        //***
        Button btn_add=((Button)findViewById(R.id.btn_add));
        btn_add.setOnClickListener(alis);
        //Removers
        for(int i=0;i<6;i++) {
            getRemoveBtn(i).setOnClickListener(rem);
        }
    }


    //RTL Config
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    //Action Bar Load
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usersmenu, menu);
        return true;
    }

    //Menu Response
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accept: {
                Intent u = new Intent(this, MainActivity.class);
                u.putExtras(first);
                //write intent
                u.putExtra("N", N);
                if(N!=0)
                {
                    for(int i=0;i<N;i++)
                    {
                        u.putExtra(Integer.toString(i), Names[i]);
                    }
                }
                //***
                u.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(u);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void ui_setupTable()
    {
        for(int i=0;i<6;i++)
        {
            Button b=getRemoveBtn(i);
            TextView t=getLabel(i);
            if(i>=N)
            {
                b.setVisibility(View.GONE);
                t.setVisibility(View.GONE);
            }
            else
            {
                t.setText(Names[i]);
                b.setVisibility(View.VISIBLE);
                t.setVisibility(View.VISIBLE);
            }
        }
    }

    public Button getRemoveBtn(int x)
    {
        View u=findViewById(R.id.btn_rem_u0);
        switch (x)
        {
            case 0:
            {u= findViewById(R.id.btn_rem_u0); break;}
            case 1:
            { u= findViewById(R.id.btn_rem_u1); break;}
            case 2:
            {  u= findViewById(R.id.btn_rem_u2); break;}
            case 3:
            {  u= findViewById(R.id.btn_rem_u3); break;}
            case 4:
            {  u= findViewById(R.id.btn_rem_u4); break;}
            case 5:
            {    u= findViewById(R.id.btn_rem_u5); break;}
        }
        return ((Button)u);
    }
    public TextView getLabel(int x)
    {
        View u=findViewById(R.id.txt_u0);
        switch (x)
        {
            case 0:
            { u= findViewById(R.id.txt_u0);break;}
            case 1:
            { u= findViewById(R.id.txt_u1);break;}
            case 2:
            {  u= findViewById(R.id.txt_u2);break;}
            case 3:
            {  u= findViewById(R.id.txt_u3);break;}
            case 4:
            {  u= findViewById(R.id.txt_u4);break;}
            case 5:
            {  u= findViewById(R.id.txt_u5);break;}
        }
        return ((TextView) u);
    }


    View.OnClickListener alis=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add();
        }
    };
    public void add()
    {
        if(N==6)
        {
            msg("درحال حاضر امکان داشتن بیش از شش فرد وجود ندارد.\nانشاءالله در صورت انتشار نسخه های بعدی این مورد برطرف خواهد شد.");
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String name="";

        builder.setTitle("نام فرد جدید");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("افزودن", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_user(input.getText().toString());
            }
        });
        builder.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
    public void add_user(String name)
    {
        for(int i=0;i<N;i++)
            if(Names[i].equals(name))
            {
                msg("نام تکراری است");
                return;
            }
        Names[N]=name;
        refine_checks_add(N);
        N++;
        ui_setupTable();
    }

    View.OnClickListener rem=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(int i=0;i<6;i++)
                if(((Button)view)==getRemoveBtn(i)) {
                    remove(i);
                    break;
                }
        }
    };

    void remove(int x)
    {
        for(int i=x;i<N-1;i++)
            Names[i]=Names[i+1];
        refine_checks_remove(x);
        N--;
        ui_setupTable();
    }

    void refine_checks_remove(int x)
    {
        int n=first.getIntExtra("n",0);
        for(int t=0;t<n;t++) {
            int u = first.getIntExtra("iU" + String.format("%03d", t), 0);
            boolean[] c = new boolean[6];
            for (int i = 0; i < 6; i++)
                c[i] = (u % Math.pow(2, i + 1)) >= Math.pow(2, i);
            boolean[] cp = new boolean[6];
            for(int i=0; i<x;i++)
                cp[i]=c[i];
            for(int i=x;i<5;i++)
                cp[i]=c[i+1];
            u=0;
            for(int i=0;i<6;i++)
                if(cp[i])
                    u+=Math.pow(2,i);
            first.putExtra("iU"+String.format("%03d", t),u);
        }
    }
    void refine_checks_add(int x)
    {
        int n=first.getIntExtra("n",0);
        for(int t=0;t<n;t++) {
            int u = first.getIntExtra("iU" + String.format("%03d", t), 0);
            boolean[] c = new boolean[6];
            for (int i = 0; i < 6; i++)
                c[i] = (u % Math.pow(2, i + 1)) >= Math.pow(2, i);
            boolean[] cp = new boolean[6];
            for(int i=0; i<x;i++)
                cp[i]=c[i];
            c[x]=false;
            for(int i=x;i<5;i++)
                cp[i+1]=c[i];
            u=0;
            for(int i=0;i<6;i++)
                if(cp[i])
                    u+=Math.pow(2,i);
            first.putExtra("iU"+String.format("%03d", t),u);
        }
    }

    void msg(String m)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(m);
        dlgAlert.setTitle("");
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("بستن",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.show();
    }
}