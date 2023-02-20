package com.java.lichenghao.eltext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.java.lichenghao.eltext.control.Controller;
import com.java.lichenghao.eltext.control.ControllerInterface;
import com.java.lichenghao.eltext.control.StopController;
import com.java.lichenghao.eltext.control.TestController;
import com.java.lichenghao.eltext.control.TrainController;

public class MainActivity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;
    GLRender glRender;
    StringBuffer upperTextContent = new StringBuffer();
    TextView upperText;
    private int mode = 0; //0 home 1 train 2 test
    final String TAG = "MainActivity";

    private volatile int lastControllerId = 0;
    private volatile Controller lastController = null;

    private Controller getController(int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(lastController != null){
                    lastController.stopThread();
                }
                lastControllerId++;
                if(type == 0){//train
                    lastController = new TrainController(controllerInterface, lastControllerId);
                }else if(type == 1){//test
                    lastController = new TestController(controllerInterface, lastControllerId);
                }else if(type == 2){//stop
                    lastController = new StopController(controllerInterface, lastControllerId);
                }

            }
        });
        return lastController;
    }

    ControllerInterface controllerInterface = new ControllerInterface() {
        @Override
        public void setAppTitle(String title, int id) {
            if(id != lastControllerId)return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTitle(title);
                }
            });
        }

        @Override
        public void setUpperText(String text, int id) {
            if(id != lastControllerId)return;
            if(upperTextContent == null){
                Log.e("TAG", "null upperTextContent!!");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upperTextContent = new StringBuffer(text);
                    upperText.setText(upperTextContent);
                }
            });
        }

        @Override
        public void appendUpperText(Character character, int id) {
            if(id != lastControllerId)return;
            if(upperTextContent == null){
                Log.e("TAG", "null upperTextContent!!");
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upperTextContent.append(character);
                    upperText.setText(upperTextContent);
                }
            });
        }

        @Override
        public void setBeginTime(long beginTime, int id) {
            if(id != lastControllerId)return;
            if(glRender == null){
                Log.e("TAG", "null glRenderer!!");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    glRender.setBeginTime(beginTime);
                }
            });
        }

        @Override
        public void setRedBlock(int blockId, int id) {
            if(id != lastControllerId)return;
            if(glRender == null){
                Log.e("TAG", "null glRenderer!!");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    glRender.setRedBlock(blockId);
                }
            });
        }

        @Override
        public void setRedPoint(int blockId, int id) {
            if(id != lastControllerId)return;
            if(glRender == null){
                Log.e("TAG", "null glRenderer!!");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    glRender.setRedPot(blockId);
                }
            });
        }

        @Override
        public void GLSwitch(boolean turnON, int id) {
            if(id != lastControllerId)return;
            if(glRender == null){
                Log.e("TAG", "null glRenderer!!");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    glRender.setRender_noting(!turnON);
                }
            });
        }

        @Override
        public void HideExtraViews(boolean hide, int id) {
            if(id != lastControllerId)return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(hide){
                        findViewById(R.id.extra_text).setVisibility(View.GONE);
                    }else{
                        findViewById(R.id.extra_text).setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    };

//    private void show_text(){
//        int textLength = ActionBarText.length();
//        if(textLength > text_max_length){
//            upperText.setText((CharSequence)("..." + ActionBarText.subSequence(textLength - text_max_length ,textLength)));
//        }else{
//            upperText.setText((CharSequence)ActionBarText);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.change:
                changeFlashingMode();
                break;
            case R.id.connect:
                Toast.makeText(getApplicationContext(),"尚未实现",Toast.LENGTH_SHORT).show();
//                findViewById(R.id.extra_text).setVisibility(View.GONE);
                break;

            case R.id.back:
//                Toast.makeText(getApplicationContext(),"info",Toast.LENGTH_SHORT).show();
//                findViewById(R.id.extra_text).setVisibility(View.VISIBLE);
                close();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && mode != 0) {
            close();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && mode != 0) {
            close();
            return false;
        }
        return super.onKeyUp(keyCode,event);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        glSurfaceView = (GLSurfaceView)findViewById(R.id.gl_surface);
        glSurfaceView.setEGLContextClientVersion(2);
        glRender = new GLRender(this);
        glSurfaceView.setRenderer(glRender);
//        ActionBarText = new StringBuffer("not connected!");
        findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
                if(mode != 0){
                    close();
                }
                getController(1).start();
                mode = 2;
            }
        });

        findViewById(R.id.trainButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode != 0){
                    close();
                }
                getController(0).start();
                mode = 1;
            }
        });


        upperText = findViewById(R.id.upperText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void changeFlashingMode(){
        if(glRender.getDoBlocksFlash()){
            glRender.setDoBlocksFlash(false);
            this.setTitle((CharSequence)"char");
        }else{
            glRender.setDoBlocksFlash(true);
            this.setTitle((CharSequence)"block");
        }
    }

    private void close(){
        getController(2).start();
        mode = 0;
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int action = ev.getAction() & MotionEvent.ACTION_MASK;
//        if(action == MotionEvent.ACTION_UP){
//            Random r = new Random();
//            ActionBarText.append(r.nextInt(10));
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }



}