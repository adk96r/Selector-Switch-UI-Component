package adk.selectorswitch;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SelectorSwitch selectorSwitch = (SelectorSwitch) findViewById(R.id.selectorBtn);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectorSwitch.selectMode(selectorSwitch.getCurrentMode() + 1);
            }
        });
    }
}
