package com.demo.dms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aodianyun.dms.android.DMS;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.ConnectActionListener;

import java.io.UnsupportedEncodingException;

import static com.demo.dms.R.id.btn;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String dms_Subkey = "sub_20269daf9d9304f283697d44df484128";

    private String dms_Pubkey = "pub_e44deacf527e84bd0b8cd319b50fa40b";
    private String dms_topic = "yuxing";

    private Button mbnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mbnt = (Button) findViewById(btn);
        mbnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DMS.publish(dms_topic, "推送消息".getBytes("UTF-8"), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken iMqttToken) {
                            Log.i("TAG", "onSuccess");
                        }

                        @Override
                        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                            Log.i("TAG", "onFailure");
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        initDMS();
    }

    private void initDMS() {
        //初始化
        DMS.init(getApplication(), dms_Pubkey, dms_Subkey, new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            //s :话题名称
            //mqttMessage :消息内容
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), mqttMessage.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        //链接
        try {
            DMS.connect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    try {
                        if (dms_topic == null)
                            return;
                        //关注话题
                        DMS.subscribe(dms_topic, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken iMqttToken) {
                                Toast.makeText(getApplicationContext(), "关注话题成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
}
