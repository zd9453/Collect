package com.zd.collect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zd.collect.activity.CustomSurfaceVideoActivity;
import com.zd.collect.activity.ShapeBgTextViewActivity;
import com.zd.collect.activity.StatesLayoutActivity;
import com.zd.collect.activity.StatesUtilsActivity;
import com.zd.collect.activity.TransformImageViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void jumpTo(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void transformImageView(View view) {
        jumpTo(TransformImageViewActivity.class);
    }

    public void shapeBgTextView(View view) {
        jumpTo(ShapeBgTextViewActivity.class);
    }

    public void statesLayout(View view) {
        jumpTo(StatesLayoutActivity.class);
    }

    public void customSurfaceVideo(View view) {
        jumpTo(CustomSurfaceVideoActivity.class);
    }

    public void statesUtils(View view) {
        jumpTo(StatesUtilsActivity.class);
    }
}
