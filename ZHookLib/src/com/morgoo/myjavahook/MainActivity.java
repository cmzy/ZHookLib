
package com.morgoo.myjavahook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.morgoo.zhooklib.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
        final TextView text = (TextView) findViewById(R.id.text1);
        HookTest.main(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (msg.obj instanceof String) {
                        text.setText(text.getText() + "\r\n\r\n" + msg.obj);
                    }
                }
            }
        });
        text.setText("haha");

    }
   
}
