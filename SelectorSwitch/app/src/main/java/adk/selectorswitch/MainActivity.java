package adk.selectorswitch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SelectorSwitch selectorSwitch = (SelectorSwitch) findViewById(R.id.selectorBtn);
        selectorSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectorSwitch.selectNextMode();
            }
        });

        selectorSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectorSwitch.selectDefaultMode();
                return true;
            }
        });
    }
}
