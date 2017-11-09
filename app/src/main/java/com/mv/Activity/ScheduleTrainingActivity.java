package com.mv.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mv.R;
import com.mv.Utils.Constants;
import com.mv.databinding.ActivityScheduleTrainingBinding;


public class ScheduleTrainingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityScheduleTrainingBinding binding;
    private ImageView img_back, img_list, img_logout;
    private TextView toolbar_title;
    private RelativeLayout mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_training);
        binding.setActivity(this);
        initViews();
    }

    public void onLayoutMTTraining() {
        Intent intent;
        intent = new Intent(ScheduleTrainingActivity.this, WebViewActivity.class);
        intent.putExtra(Constants.URL, "http://dev-mulyavardhan.cs57.force.com/");
        intent.putExtra(Constants.TITLE, "New MT Training");
        startActivity(intent);
    }


    private void initViews() {
        setActionbar(getString(R.string.Schedule_training));
    }

    private void setActionbar(String Title) {
        mToolBar = (RelativeLayout) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(Title);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        img_logout = (ImageView) findViewById(R.id.img_logout);
        img_logout.setVisibility(View.GONE);
        img_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

}
