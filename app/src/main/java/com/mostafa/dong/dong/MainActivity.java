package com.mostafa.dong.dong;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;
//import android.app.ActionBar;


public class MainActivity extends AppCompatActivity {

    private class vStruct {
        Context o;
        private int id;
        public LinearLayout v;
        private EditText Name;
        private EditText Price;
        private Spinner Buyer;
        private CheckBox[] Check = new CheckBox[6];
        private ImageButton Remov;

        public vStruct(Context owner, TableLayout put, View.OnClickListener remove_handler, int _id) {
            o = owner;
            id = _id;
            LayoutInflater u = (LayoutInflater) owner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View v=u.inflate(R.layout.itm, put);
            v = (LinearLayout) (u.inflate(R.layout.itm, null));
            Name = (EditText) v.findViewById(R.id.name);
            Price = (EditText) v.findViewById(R.id.price);
            Buyer = (Spinner) v.findViewById(R.id.buyer);
            updateCombo(owner);
            Check[0] = (CheckBox) v.findViewById(R.id.c0);
            Check[1] = (CheckBox) v.findViewById(R.id.c1);
            Check[2] = (CheckBox) v.findViewById(R.id.c2);
            Check[3] = (CheckBox) v.findViewById(R.id.c3);
            Check[4] = (CheckBox) v.findViewById(R.id.c4);
            Check[5] = (CheckBox) v.findViewById(R.id.c5);
            for (int i = N; i < 6; i++)
                Check[i].setVisibility(View.INVISIBLE);
            Remov = (ImageButton) v.findViewById(R.id.remov);
            Remov.setOnClickListener(remove_handler);
            Remov.setTag(id);
//            try {
//                Remov.setTag(8,v);
//            }
//            catch (Exception e)
//            {}

            put.addView(v);
        }

        public void setId(int _id) {
            id = _id;
            Remov.setTag(id);
        }

        public String getName() {
            return Name.getText().toString();
        }

        public String getBuyer() {
            return Buyer.getSelectedItem().toString();
        }

        public int getUsers() {
            int r = 0;
            for (int i = 0; i < N; i++)
                if (Check[i].isChecked())
                    r += Math.pow(2, i);
            return r;
        }

        public double getPrice() {
            String s = Price.getText().toString();
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                return -0.01;
            }
        }

        public void setData(String _name, String _buyer, int _users, double _price) {
            Name.setText(_name);
            if (_price == -0.01)
                Price.setText("");
            else if ((_price * 10) % 10 == 0)
                Price.setText(String.format("%.0f", _price));
            else
                Price.setText(String.format("%.1f", _price));
            for (int i = 0; i < 6; i++) {
                boolean c = (_users % Math.pow(2, i + 1)) >= Math.pow(2, i);
                Check[i].setChecked(c);
            }
            //spinner stuff
            String compareValue = _buyer;
            ArrayList<String> u = new ArrayList<String>();
            u.add("");
            for (int i = 0; i < N; i++)
                u.add(Names[i]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(o, android.R.layout.simple_spinner_item, u);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Buyer.setAdapter(adapter);
            if (!compareValue.equals(null)) {
                int spinnerPosition = adapter.getPosition(compareValue);
                Buyer.setSelection(spinnerPosition);
            }
        }

        public void updateChecks() {
            for (int i = 0; i < N; i++)
                Check[i].setVisibility(View.VISIBLE);
            for (int i = N; i < 6; i++)
                Check[i].setVisibility(View.INVISIBLE);
        }

        public void updateCombo(Context owner) {
            ArrayList<String> u = new ArrayList<String>();
            u.add("");
            for (int i = 0; i < N; i++)
                u.add(Names[i]);
            ArrayAdapter<String> t = new ArrayAdapter<String>(owner, android.R.layout.simple_spinner_item, u);
            Buyer.setAdapter(t);
            t.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Buyer.setPrompt("choose");
        }
    }

    int N = 0;
    String[] Names;

    int n = 0;

    SharedPreferences A;

    ArrayList<vStruct> uii;

    int __randomizer__=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forceRTLIfSupported();
        //***
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("دونگ یاب");
//        try {
//            ActionBar bar = getActionBar();
//            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.headr)));
//        }
//        catch (Exception e)
//        {}
        //Read Intent
        Names = new String[6];
        for (int i = 0; i < 6; i++)
            Names[i] = "";

        A=getSharedPreferences("data",MODE_PRIVATE);
        Bundle data = getIntent().getExtras();
        Intent gi=getIntent();
        if(data==null)
        {
            gi=loadStateFromFile();
            if(gi!=null)
                data=gi.getExtras();
        }

        if (data != null) {
            N = data.getInt("N", 0);
            for (int i = 0; i < N; i++)
                Names[i] = data.getString(Integer.toString(i), Integer.toString(i));
        }
        //***
        uii = new ArrayList<vStruct>();
        //***
        ui_setupHeaders();
        decodeAndPutData(gi);
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
        inflater.inflate(R.menu.mmenu, menu);
        return true;
    }

    //Menu Response
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.users: {
                //Write Intent
                Intent u = new Intent(this, users.class);
                u.putExtra("N", N);
                if (N != 0) {
                    for (int i = 0; i < N; i++) {
                        u.putExtra(Integer.toString(i), Names[i]);
                    }
                }
                encodeItemData(u);
                this.startActivity(u);
                break;
            }
            case R.id.add: {
                vStruct v = new vStruct(this, (TableLayout) findViewById(R.id.items), removal, n);
                n++;
                uii.add(v);
                __randomizer__=0;
                break;
            }
            case R.id.clear: {
                clear();
                 //debug
                __randomizer__++;
                if(__randomizer__>=5) {
                    randomize();
                    __randomizer__=3;
                }
                break;
            }
            case R.id.sumup: {
                if (N == 0 || n == 0) {
                    msg("تعداد افراد و یا تعداد آیتم ها خالی است");
                    return true;
                }
                Intent u = new Intent(this, balancer.class);
                u.putExtra("N", N);
                if (N != 0) {
                    for (int i = 0; i < N; i++) {
                        u.putExtra(Integer.toString(i), Names[i]);
                    }
                }
                encodeItemData(u);
                this.startActivity(u);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Item Setup
    public void ui_setupHeaders() {
        for (int i = 0; i < 6; i++) {
            TextView lbl = findHeaderLabel(i);
            if (i >= N) {
                lbl.setVisibility(View.INVISIBLE);
            } else {
                lbl.setVisibility(View.VISIBLE);
                lbl.setText(Names[i]);
            }
        }
    }

    public TextView findHeaderLabel(int x) {
        View u = findViewById(R.id.btn_rem_u0);
        switch (x) {
            case 0: {
                u = findViewById(R.id.header_u0);
                break;
            }
            case 1: {
                u = findViewById(R.id.header_u1);
                break;
            }
            case 2: {
                u = findViewById(R.id.header_u2);
                break;
            }
            case 3: {
                u = findViewById(R.id.header_u3);
                break;
            }
            case 4: {
                u = findViewById(R.id.header_u4);
                break;
            }
            case 5: {
                u = findViewById(R.id.header_u5);
                break;
            }
        }
        return ((TextView) u);
    }


    View.OnClickListener removal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int w = (int) view.getTag();
            LinearLayout v = uii.get(w).v;
            n--;
            uii.remove(w);
            ((TableLayout) findViewById(R.id.items)).removeView(v);
            for (int i = 0; i < n; i++)
                uii.get(i).setId(i);
        }
    };

    void clear() {
        if(n==0)
            return;

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا واقعا مایلید لیست خالی شود؟");
        dlgAlert.setTitle("هشدار");
        dlgAlert.setCancelable(true);
        dlgAlert.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dlgAlert.setPositiveButton("بله",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        n = 0;
                        ((TableLayout) findViewById(R.id.items)).removeAllViews();
                        uii.clear();
                    }
                });
        dlgAlert.show();

    }

    void encodeItemData(Intent x) {
        //GeneralData: n (int)
        //DataForEachRow: 1. Price 2. Name 3. Buyer Name 4. (Int) Users
        x.putExtra("n", n);
        for (int i = 0; i < n; i++) {
            x.putExtra("iP" + String.format("%03d", i), uii.get(i).getPrice());
            x.putExtra("iU" + String.format("%03d", i), uii.get(i).getUsers());
            x.putExtra("iB" + String.format("%03d", i), uii.get(i).getBuyer());
            x.putExtra("iN" + String.format("%03d", i), uii.get(i).getName());
        }
    }

    void decodeAndPutData(Intent x) {
        //GeneralData: n (int)
        //DataForEachRow: 1. Price 2. Name 3. Buyer Name 4. (Int) Users
        if(x==null)
            n = 0;
        else
            n = x.getIntExtra("n", 0);
        if (n == 0)
            return;
        for (int i = 0; i < n; i++) {
            double _price = x.getDoubleExtra("iP" + String.format("%03d", i), 0);
            int _users = x.getIntExtra("iU" + String.format("%03d", i), 0);
            String _buyer = x.getStringExtra("iB" + String.format("%03d", i));
            String _name = x.getStringExtra("iN" + String.format("%03d", i));
            vStruct v = new vStruct(this, (TableLayout) findViewById(R.id.items), removal, i);
            v.setData(_name, _buyer, _users, _price);
            uii.add(v);
        }
    }

    void msg(String m) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
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



    void randomize()
    {
        Random r=new Random();
        N=3+r.nextInt(4);//todo:6
//        N=2;
        for(int i=0;i<N;i++)
            Names[i]=Character.toString((char)(65+i));
        n=N+r.nextInt(10);
        ui_setupHeaders();
        for(int i=0;i<n;i++) {
            double _price = 2 * (1 + r.nextInt(10));
            int _buyer = r.nextInt(N);
            int _users = r.nextInt((int) Math.pow(2, N));
            String name = "random item";
            vStruct v = new vStruct(this, (TableLayout) findViewById(R.id.items), removal, i);
            v.setData(name,Names[_buyer],_users,_price);
            uii.add(v);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle Q)
    {
        Intent u = getIntent();
        u.putExtra("N", N);
        if (N != 0) {
            for (int i = 0; i < N; i++) {
                u.putExtra(Integer.toString(i), Names[i]);
            }
        }
        encodeItemData(u);
    }


    public void saveStateToFile()
    {
        SharedPreferences.Editor x = A.edit();

        if(N==0)
            x.clear();

        x.putInt("N",N);
        for(int i=0;i<N;i++)
            x.putString(Integer.toString(i),Names[i]);

        x.putInt("n", n);
        for (int i = 0; i < n; i++) {
            x.putLong("iP" + String.format("%03d", i), Double.doubleToRawLongBits(uii.get(i).getPrice()));
            x.putInt("iU" + String.format("%03d", i), uii.get(i).getUsers());
            x.putString("iB" + String.format("%03d", i), uii.get(i).getBuyer());
            x.putString("iN" + String.format("%03d", i), uii.get(i).getName());
        }

        x.commit();
    }


    Intent loadStateFromFile()
    {
        try {
            if(!A.contains("N"))
                return null;
            Intent r = new Intent(this, MainActivity.class);
//            Debug
//            r.putExtra("N", 2);
//            r.putExtra("0", "Mostafa");
//            r.putExtra("1", "Ali");
//            r.putExtra("n",1);
//            r.putExtra("iP" + String.format("%03d", 0), 12d);
//            r.putExtra("iU" + String.format("%03d", 0), 3);
//            r.putExtra("iB" + String.format("%03d", 0), "Ali");
//            r.putExtra("iN" + String.format("%03d", 0), "Item0");
            int _N=A.getInt("N",0);
            r.putExtra("N", _N);
            for(int i=0;i<_N;i++)
                r.putExtra(Integer.toString(i), A.getString(Integer.toString(i),""));

            int _n=A.getInt("n",0);
            r.putExtra("n", _n);
            for(int i=0;i<_n;i++)
            {
                double _price = Double.longBitsToDouble(A.getLong("iP" + String.format("%03d", i), 0));
                int _users = A.getInt("iU" + String.format("%03d", i), 0);
                String _buyer = A.getString("iB" + String.format("%03d", i),"");
                String _name = A.getString("iN" + String.format("%03d", i),"");
                r.putExtra("iP" + String.format("%03d", i), _price);
                r.putExtra("iU" + String.format("%03d", i), _users);
                r.putExtra("iB" + String.format("%03d", i), _buyer);
                r.putExtra("iN" + String.format("%03d", i), _name);
            }

            return r;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        saveStateToFile();

        Toast.makeText(getApplicationContext(), "اطلاعات فرم ذخیره شد", Toast.LENGTH_SHORT).show();
    }
}