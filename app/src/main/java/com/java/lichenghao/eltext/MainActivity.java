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
import android.view.WindowMetrics;
import android.widget.Toast;

import java.util.Random;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    GLSurfaceView glSurfaceView;
    GLRender glRender;
    StringBuffer ActionBarText = new StringBuffer();
    int text_max_length = 40;

    private void show_text(){
        int textLength = ActionBarText.length();
        if(textLength > text_max_length){
            this.setTitle((CharSequence)("..." + ActionBarText.subSequence(textLength - text_max_length ,textLength)));
        }else{
            this.setTitle((CharSequence)ActionBarText);
        }
    }


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
                Toast.makeText(getApplicationContext(),R.string.app_name,Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_bar_length:
                Toast.makeText(getApplicationContext(),"info",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        glSurfaceView = (GLSurfaceView)findViewById(R.id.gl_surface);
        glSurfaceView.setEGLContextClientVersion(2);
        glRender = new GLRender(this);
        glSurfaceView.setRenderer(glRender);
        ActionBarText = new StringBuffer("not connected!");
        show_text();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if(action == MotionEvent.ACTION_UP){
            Random r = new Random();
            ActionBarText.append(r.nextInt(10));
        }
        show_text();
        return super.dispatchTouchEvent(ev);
    }


    }