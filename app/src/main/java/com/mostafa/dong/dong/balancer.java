package com.mostafa.dong.dong;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class balancer extends AppCompatActivity {
    int N=0;
    String[] Names;
    Double[] Paid;
    Double[] Used;
    Double[] Balance;

    class Exchange
    {
        private int from,to;
        private double value;
        public Exchange(int _from, int _to, double _value)
        {
            from=_from; to=_to; value=_value;
        }
        public String getExpression()
        {
            return ""; //TODO
        }
        public void Reverse()
        {
            int swap=from;
            from=to;
            to=swap;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forceRTLIfSupported();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer);
        setTitle("تراز افراد");
        //todo ****
        try {
            ActionBar bar = getActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.headr)));
        }
        catch (Exception e)
        {}

        decodeAndAnalyzeData(getIntent());
        putData();
        configBtns();
    }

    //RTL Config
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    void decodeAndAnalyzeData(Intent x)
    {
        //GeneralData: n (int)
        //DataForEachRow: 1. Price 2. Name 3. Buyer Name 4. (Int) Users
        if(x.getExtras()==null)
            return;
        N=x.getIntExtra("N",0);
        if(N==0)
            return;
        Names=new String[6];
        for(int i=0;i<N;i++)
            Names[i]=x.getStringExtra(Integer.toString(i));
        int n=x.getIntExtra("n",0);
        if(n==0)
            return;
        Paid=new Double[6];
        Used=new Double[6];
        Balance=new Double[6];
        for(int i=0;i<6;i++)
        {
            Paid[i]=0d;
            Used[i]=0d;
            Balance[i]=0d;
        }
        for(int i=0;i<n;i++)
        {
            double _price = x.getDoubleExtra("iP"+String.format("%03d", i),0);
            if(_price==-0.01)
                _price=0;
            int _users = x.getIntExtra("iU"+String.format("%03d", i),0);
            String _buyer = x.getStringExtra("iB"+String.format("%03d", i));
            String _name = x.getStringExtra("iN"+String.format("%03d", i));
            int uid=findUserId(_buyer);
            if(uid==-1)
                continue;
            Paid[uid]+=_price;
            boolean[] c = new boolean[6];
            for (int j = 0; j < 6; j++)
                c[j] = (_users % Math.pow(2, j + 1)) >= Math.pow(2, j);
            int m=0;
            for(int j=0;j<6;j++)
                if(c[j])
                    m++;
            if(m==0)
            {
                m=1;
                c[uid]=true;
            }
            double up=_price/m;
            for(int j=0;j<6;j++)
                if(c[j])
                    Used[j]+=up;

        }
        for(int j=0;j<6;j++)
            Balance[j]=Paid[j]-Used[j];
//        //Checksum TODO
    }
    void putData()
    {
        for(int i=0;i<6;i++)
        {
            TextView lbl_name=findView(i,0);
            TextView lbl_paid=findView(i,1);
            TextView lbl_used=findView(i,2);
            TextView lbl_balance=findView(i,3);

            if(i>=N)
            {
                lbl_name.setVisibility(View.GONE);
                lbl_paid.setVisibility(View.GONE);
                lbl_used.setVisibility(View.GONE);
                lbl_balance.setVisibility(View.GONE);
            }
            else
            {
                lbl_name.setVisibility(View.VISIBLE);
                lbl_paid.setVisibility(View.VISIBLE);
                lbl_used.setVisibility(View.VISIBLE);
                lbl_balance.setVisibility(View.VISIBLE);

                lbl_name.setText(Names[i]);
                lbl_paid.setText(String.format("%.1f", Paid[i]));
                lbl_used.setText(String.format("%.1f", Used[i]));
                lbl_balance.setText((Balance[i]>0?"+":"")+String.format("%.1f", Balance[i]));
                lbl_balance.setTextDirection(View.TEXT_DIRECTION_LTR);
                lbl_balance.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                lbl_paid.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                lbl_used.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            }
        }
    }
    int findUserId(String x)
    {
        for(int i=0;i<N;i++)
            if(Names[i].equals(x))
                return i;
        return -1;
    }
    TextView findView(int row, int col)
    {
        switch (col)
        {
            case 0:
            {
                switch (row)
                {
                    case 0:
                        return (TextView)findViewById(R.id.ur0);
                    case 1:
                        return (TextView)findViewById(R.id.ur1);
                    case 2:
                        return (TextView)findViewById(R.id.ur2);
                    case 3:
                        return (TextView)findViewById(R.id.ur3);
                    case 4:
                        return (TextView)findViewById(R.id.ur4);
                    case 5:
                        return (TextView)findViewById(R.id.ur5);
                }
                break;
            }
            case 1:
            {
                switch (row)
                {
                    case 0:
                        return (TextView)findViewById(R.id.pd0);
                    case 1:
                        return (TextView)findViewById(R.id.pd1);
                    case 2:
                        return (TextView)findViewById(R.id.pd2);
                    case 3:
                        return (TextView)findViewById(R.id.pd3);
                    case 4:
                        return (TextView)findViewById(R.id.pd4);
                    case 5:
                        return (TextView)findViewById(R.id.pd5);
                }
                break;
            }
            case 2:
            {
                switch (row)
                {
                    case 0:
                        return (TextView)findViewById(R.id.ud0);
                    case 1:
                        return (TextView)findViewById(R.id.ud1);
                    case 2:
                        return (TextView)findViewById(R.id.ud2);
                    case 3:
                        return (TextView)findViewById(R.id.ud3);
                    case 4:
                        return (TextView)findViewById(R.id.ud4);
                    case 5:
                        return (TextView)findViewById(R.id.ud5);
                }
                break;
            }
            case 3:
            {
                switch (row)
                {
                    case 0:
                        return (TextView)findViewById(R.id.bl0);
                    case 1:
                        return (TextView)findViewById(R.id.bl1);
                    case 2:
                        return (TextView)findViewById(R.id.bl2);
                    case 3:
                        return (TextView)findViewById(R.id.bl3);
                    case 4:
                        return (TextView)findViewById(R.id.bl4);
                    case 5:
                        return (TextView)findViewById(R.id.bl5);
                }
                break;
            }
        }
        return (TextView)findViewById(R.id.blh);
    }


    String[] desc_Exs(Exchange[] x)
    {
        String[] r = new String[5];
        for(int i=0;i<5;i++)
            if(x[i]==null)
                r[i]="";
            else
            {
                double _price=x[i].value;
                if(_price==0)
                {
                    r[i]="";
                    continue;
                }
                String p;
                if ((_price*10)%10==0)
                    p=(String.format("%.0f", _price));
                else
                    p=(String.format("%.1f", _price));
                r[i]="\""+Names[x[i].from]+"\""+" به "+"\""+Names[x[i].to]+"\""+" مبلغ "+p+"kt بدهد";
            }
        return r;
    }

    String[] desc_Tbl(String[] _names, Double[] _balance)
    {
        String[] r = new String[5];
        for(int i=0;i<5;i++)
            r[i]="";
        for(int i=0;i<5;i++)
        {
            if(_balance[i]==null)
                continue;
            if(_balance[i]==0)
                continue;
            String p="";
            if ((_balance[i]*10)%10==0)
                p=(String.format("%.0f", Math.abs(_balance[i])));
            else
                p=(String.format("%.1f", Math.abs(_balance[i])));
            if(_balance[i]<0)
                r[i]="\""+_names[i]+"\""+" مبلغ "+p+" kt روی میز بگذارد";
            else
                r[i]="\""+_names[i]+"\""+" مبلغ "+p+" kt از روی میز بردارد";

        }
        return r;
    }


    private void configBtns()
    {
        ((Button)findViewById(R.id.show_ext)).setOnClickListener(btl);
        ((Button)findViewById(R.id.show_rnd)).setOnClickListener(btl);
        ((Button)findViewById(R.id.show_tbl)).setOnClickListener(btl);
    }

    View.OnClickListener btl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getTag().toString()) {
                case "ext":
                {
                    String[] s = desc_Exs(calc_Exact_GT(Names, Balance));
                    Intent u = new Intent(balancer.this, report.class);//*** null
                    for(int i=0;i<5;i++)
                        u.putExtra(Integer.toString(i),"");
                    for(int i=0;i<s.length;i++)
                        u.putExtra(Integer.toString(i),s[i]);
                    balancer.this.startActivity(u);
                    break;
                }
                case "rnd":
                {
                    String[] s = desc_Exs(calc_Rnd_GT(Names, Balance));
                    Intent u = new Intent(balancer.this, report.class);//*** null
                    for(int i=0;i<5;i++)
                        u.putExtra(Integer.toString(i),"");
                    for(int i=0;i<s.length;i++)
                        u.putExtra(Integer.toString(i),s[i]);
                    balancer.this.startActivity(u);
                    break;
                }
                case "tbl":
                {
                    if(N>5)
                    {
                        msg("درحال حاضر امکان نمایش تسویه حساب میزی برای افراد بیشتر از پنج نفر وجود ندارد.\nانشاءالله در صورت انتشار نسخه های بعدی این مورد برطرف خواهد شد.");
                    }
                    else {
                        String[] s = desc_Tbl(Names, Balance);
                        Intent u = new Intent(balancer.this, report.class);//*** null
                        for (int i = 0; i < 5; i++)
                            u.putExtra(Integer.toString(i), "");
                        for (int i = 0; i < s.length; i++)
                            u.putExtra(Integer.toString(i), s[i]);
                        balancer.this.startActivity(u);
                    }
                    break;
                }
            }
        }
    };

    void applyEx(Exchange e)
    {
        if(e==null)
            return;
        if(e.value==0)
            return;
        double v=e.value;
        Balance[e.from]+=v;
        Balance[e.to]-=v;
    }
    void revertEx(Exchange e)
    {
        if(e==null)
            return;
        if(e.value==0)
            return;
        double v=e.value;
        Balance[e.from]-=v;
        Balance[e.to]+=v;
    }




    Exchange[] calc_Exact_GT(String[] _names, Double[] _balance)
    {

        ArrayList<Exchange> r=new ArrayList<Exchange>();

        Double[] xb=new Double[_balance.length];
        for(int i=0;i<N;i++)
            xb[i]=-_balance[i];

        ArrayList<Integer> so=new ArrayList<Integer>();
        ArrayList<Integer> si=new ArrayList<Integer>();
        for(int i=0;i<N;i++)
            if(xb[i]>0){so.add(i);}else{si.add(i);}

        boolean swap_flag=false;
        if(so.size()>si.size())
        {
            ArrayList<Integer> t=so;
            so=si;
            si=t;
            swap_flag=true;
            for(int i=0;i<N;i++)
                xb[i]*=-1;
        }
        //so.size<=si.size -- nso=1,2,3

        int nso=so.size();
        int nsi=si.size();



        if(nso==1)
        {
            for(int i=0;i<nsi;i++)
            {
                r.add(new Exchange(so.get(0),si.get(i),-xb[si.get(i)]));
            }
        }

        else if(nso==2)
        {
            double min=-1;
            int flag=-1;
            for(int y=1;y<(1<<nsi)-1;y++)
            {
                double a=0;
                a+=xb[so.get(0)];
                for(int i=0;i<nsi;i++)
                    if((y&(1<<i))!=0)
                        a+=xb[si.get(i)];

                if(min==-1||Math.abs(min)>Math.abs(a))
                {
                    min=a;
                    flag=y;
                }
            }

            Exchange e=new Exchange(so.get(0),so.get(1),min);
            r.add(e);
            xb[e.from]-=e.value; xb[e.to]+=e.value;

            for(int i=0;i<nsi;i++)
            {
                if((flag&(1<<i))!=0)
                    r.add(new Exchange(so.get(0),si.get(i),-xb[si.get(i)]));
                else
                    r.add(new Exchange(so.get(1),si.get(i),-xb[si.get(i)]));
            }

        }

        else if(nso==3)
        {
            double min=-1;
            int I=-1,J=-1,K=-1;
            for (int i=0;i<3;i++)
                for (int j=0;j<3;j++)
                    for (int k=0;k<3;k++)
                        if (i!=j&&i!=k&&j!=k)
                        {
                            int a=0;
                            a+=Math.abs(so.get(0)+si.get(i));
                            a+=Math.abs(so.get(1)+si.get(j));
                            a+=Math.abs(so.get(2)+si.get(k));
                            if(min==-1||a<Math.abs(min))
                            {
                                min=a;
                                I=i; J=j; K=k;
                            }
                        }
            double[] p=new double[3];
            p[0]=xb[so.get(0)]+xb[si.get(I)];
            p[1]=xb[so.get(1)]+xb[si.get(J)];
            p[2]=xb[so.get(2)]+xb[si.get(K)];
            Exchange e=new Exchange(0,0,0);
            Exchange f=new Exchange(0,0,0);
            if (p[1]<0&&p[2]<0)
            {
                e = new Exchange(so.get(0), so.get(1), -p[1]);
                f = new Exchange(so.get(0), so.get(2), -p[2]);
            }
            else if (p[0]<0&&p[2]<0)
            {
                e = new Exchange(so.get(1), so.get(0), -p[0]);
                f = new Exchange(so.get(1), so.get(2), -p[2]);
            }
            else if (p[0]<0&&p[1]<0)
            {
                e = new Exchange(so.get(2), so.get(0), -p[0]);
                f = new Exchange(so.get(2), so.get(1), -p[1]);
            }
            else if (p[0]<0)
            {
                e = new Exchange(so.get(1), so.get(0), p[1]);
                f = new Exchange(so.get(2), so.get(0), p[2]);
            }
            else if (p[1]<0)
            {
                e = new Exchange(so.get(0), so.get(1), p[0]);
                f = new Exchange(so.get(2), so.get(1), p[2]);
            }
            else if (p[2]<0)
            {
                e = new Exchange(so.get(0), so.get(2), p[0]);
                f = new Exchange(so.get(1), so.get(2), p[1]);
            }
            r.add(e);
            r.add(f);
            xb[e.from]-=e.value; xb[e.to]+=e.value;
            xb[f.from]-=f.value; xb[f.to]+=f.value;

            r.add(new Exchange(so.get(0),si.get(I),xb[so.get(0)]));
            r.add(new Exchange(so.get(1),si.get(J),xb[so.get(1)]));
            r.add(new Exchange(so.get(2),si.get(K),xb[so.get(2)]));


        }


        if(swap_flag) {
            for (int i = 0; i < N; i++)
                xb[i] *= -1;
            for(int i=0;i<r.size();i++) {
                r.get(i).value*=-1;
            }
        }
        for(int i=0;i<r.size();i++) {
            if(r.get(i).value<0)
            {r.get(i).Reverse(); r.get(i).value*=-1;}
        }

        Exchange[] R=new Exchange[5];
        for(int i=0;i<5;i++)
            R[i]=new Exchange(0,0,0);
        for(int i=0;i<r.size();i++)
            R[i]=r.get(i);

        //validating R
//        for(int i=0;i<5;i++)
//           applyEx(R[i]);
//        double Q=0;
//        for(int i=0;i<5;i++)
//            Q+=Math.abs(_balance[i]);
//
//        if(Q>0.2)
//        {
//            Exchange[] T = new Exchange[5];
//            for(int i=0;i<5;i++)
//                T[i]=new Exchange(0,0,Q);
//            return T;
//        }
//        for(int i=0;i<5;i++)
//            revertEx(R[i]);

        return R;
    }

    Exchange[] calc_Rnd_GT(String[] _names, Double[] _balance)
    {
        double min=-1; int flag=-1;
        Double[] rbl = new Double[N];
        for(int y=0;y<(1<<N);y++) {
            for (int i = 0; i < N; i++)
                rbl[i] = ((y & (1 << i)) != 0) ? ((double) (Math.ceil(_balance[i] * 2))) / 2
                        : ((double) (Math.floor(_balance[i] * 2))) / 2;
            double dif=0;
            double sum=0;
            for(int i=0;i<N;i++)
                sum+=rbl[i];
            if(sum!=0)
                continue;
            for(int i=0;i<N;i++)
                dif+=Math.abs(_balance[i]-rbl[i]);
            if(min==-1||dif<min)
            {
                min=dif;
                flag=y;
            }
        }
        int y=flag;
        for (int i = 0; i < N; i++)
            rbl[i] = ((y & (1 << i)) != 0) ? ((double) (Math.ceil(_balance[i] * 2))) / 2
                    : ((double) (Math.floor(_balance[i] * 2))) / 2;
        for (int i = 0; i < N; i++)
            Balance[i] = ((y & (1 << i)) != 0) ? ((double) (Math.ceil(_balance[i] * 2))) / 2
                    : ((double) (Math.floor(_balance[i] * 2))) / 2;
        putData();
        return calc_Exact_GT(_names, rbl);
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
}
