package rural.rhiss;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import rural.rhiss.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Handler objhandler = new Handler();

        objhandler.postDelayed(new Runnable() {
                                   public void run() {
                                       Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                       startActivity(intent);
                                       finish();
                                   }
                               }
                , 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
