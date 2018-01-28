package cn.edu.xidian.fingerprinttest;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_finger;
    private FingerprintManager mfingerprintManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_finger = findViewById(R.id.btn_finger);

        btn_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置按钮颜色为灰色
                btn_finger.setText("开始指纹识别");
                btn_finger.setBackgroundColor(Color.parseColor("#8a8a8a"));

                //获取系统服务对象
                mfingerprintManager = (FingerprintManager)getSystemService(Context.FINGERPRINT_SERVICE);

                /**************************设备功能检测*****************************/
                //检测是否有指纹识别的硬件
                if (!mfingerprintManager.isHardwareDetected()){
                    Toast.makeText(MainActivity.this,"您手机没有指纹识别设备",Toast.LENGTH_SHORT).show();
                    return ;
                }
                //检测设备是否处于安全状态中
                KeyguardManager keyguardManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
                if (!keyguardManager.isKeyguardSecure()) {
                    //如果不是处于安全状态中，跳转打开安全保护（锁屏等）
                    return ;
                }
                //检测系统中是否注册的指纹
                if (!mfingerprintManager.hasEnrolledFingerprints()){
                    //没有录入指纹，跳转到指纹录入
                    Toast.makeText(MainActivity.this,"没有录入指纹！",Toast.LENGTH_SHORT).show();
                    return ;
                }

                /**************************开始指纹识别*****************************/
                mfingerprintManager.authenticate(null,cancellationSignal,0,myAuthCallback,null);

            }
        });
    }

    //初始化cancellationSignal类
    private CancellationSignal cancellationSignal = new CancellationSignal();
    //初始化MyAuthCallback
    private FingerprintManager.AuthenticationCallback myAuthCallback = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            btn_finger.setText("识别成功");
            btn_finger.setBackgroundColor(Color.parseColor("#4fa5d5"));
            Toast.makeText(MainActivity.this,"识别成功！",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();

            Toast.makeText(MainActivity.this,"识别失败，请重试！",Toast.LENGTH_SHORT).show();

        }
    };
}
