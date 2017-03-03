package mj.wt.wtapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mj.wt.wtapp.R;

public class TestActivity extends AppCompatActivity {

    LinearLayout relativeLayout;
    TextView tvEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        relativeLayout= (LinearLayout) findViewById(R.id.relativelayout);
        tvEnd= (TextView) findViewById(R.id.tvlike_end);
        for (int i = 1; i < 10; i++) {
            View view= LayoutInflater.from(this).inflate(R.layout.zone_tvlike,null);
            TextView tv= (TextView) view.findViewById(R.id.tv_like);
            if (i!=9)
            {
                tv.setText("第"+i+"代火影丶");
            }else {
                tv.setText("第"+i+"代火影");
            }
            relativeLayout.addView(view);
        }
        tvEnd.setText("等9人觉得很赞");

    }
}
