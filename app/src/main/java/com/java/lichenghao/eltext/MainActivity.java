package com.java.lichenghao.eltext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity implements Controller{

    GLSurfaceView glSurfaceView;
    GLRender glRender;
    StringBuffer upperTextContent = new StringBuffer();
    TextView upperText;
    final String TAG = "MainActivity";
//    int text_max_length = 150;
//
//
//
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
//                Toast.makeText(getApplicationContext(),R.string.app_name,Toast.LENGTH_SHORT).show();
                findViewById(R.id.extra_text).setVisibility(View.GONE);
                break;

            case R.id.action_bar_length:
                Toast.makeText(getApplicationContext(),"info",Toast.LENGTH_SHORT).show();
                findViewById(R.id.extra_text).setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.trainButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"train",Toast.LENGTH_SHORT).show();
            }
        });
//        show_text();
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


    @Override
    public void setAppTitle(String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTitle(title);
            }
        });
    }

    @Override
    public void setUpperText(String text) {
        if(this.upperTextContent == null){
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
    public void appendUpperText(Character character) {
        if(this.upperTextContent == null){
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
    public void setBeginTime(long beginTime) {
        if(this.glRender == null){
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
    public void setRedBlock(int blockId) {
        if(this.glRender == null){
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
    public void setRedPoint(int blockId) {
        if(this.glRender == null){
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
    public void GLSwitch(boolean turnON) {
        if(this.glRender == null){
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
}