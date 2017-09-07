package com.example.administrator.customview;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.administrator.customview.widget.RescissibleCheckGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RescissibleCheckGroup group = (RescissibleCheckGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RescissibleCheckGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RescissibleCheckGroup group, @IdRes int checkedId) {
                Toast.makeText(group.getContext(), ((CheckBox) findViewById(checkedId)).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
