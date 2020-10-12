package com.zd.collect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zd.collect.activity.ShapeBgTextViewActivity;
import com.zd.collect.activity.StatesLayoutActivity;
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

    public void TransformImageView(View view) {
        jumpTo(TransformImageViewActivity.class);
    }

    public void ShapeBgTextView(View view) {
        jumpTo(ShapeBgTextViewActivity.class);
    }

    public void StatesUtils(View view) {
        jumpTo(StatesLayoutActivity.class);
    }


}
