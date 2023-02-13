package com.java.lichenghao.eltext;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.java.lichenghao.eltext.shape.GLCircle;
import com.java.lichenghao.eltext.shape.GLSquare;
import com.java.lichenghao.eltext.text.GLText;

public class GLRender implements GLSurfaceView.Renderer  {

    private static final String TAG = "GLRenderer";
    private GLText glText;                             // A GLText Instance
    private Context context;                           // Context (from Activity)

    private int width = 100;                           // Updated to the Current Width + Height in onSurfaceChanged()
    private int height = 400;
    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mVPMatrix = new float[16];

    private GLSquare[] squares = new GLSquare[40];
    private GLCircle[] circles = new GLCircle[40];

    private String[] buttons = {"A", "B", "C", "D","E", "F", "G", "H","I", "J", "K", "L","M", "N", "O", "P",
            "Q", "R", "S", "T","U", "V", "W", "X","Y", "Z", "0", "1","2", "3", "4", "5",
            "6", "7", "8", "9"," ", ",", ".", "?"};

    private float[] characters_x = new float[40];
    private float[] characters_y = new float[40];
    private float[] characters_f = new float[40];
    private float[] characters_p = new float[40];

    private float ratio = 1.0f;
    private long begin_time;
    private int red = -1;
    private boolean has_blocks;


    public void setRed(int n){
        red = n;
    }
    public void setBegin_time(long time){
        begin_time = time;
    }
    public GLRender(Context context)  {
        super();
        this.context = context;                         // Save Specified Context
        has_blocks = true;

    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );

        // Create the GLText
        glText = new GLText(context.getAssets());

        // Load the font from file (set size + padding), creates the texture
        // NOTE: after a successful call to this the font is ready for rendering!
        glText.load( "visible_space.ttf", 100, 0, 0 );  // Create Font
        Log.d(TAG,"Create Font!");


        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        begin_time = System.nanoTime();

        int blocks_in_column = 5;
        int blocks_in_row = 8;
        float ratio_of_block_and_margin = 3.0f;
        float margin = 2.0f / ((blocks_in_column + 1) + blocks_in_column * ratio_of_block_and_margin);
        float block = ratio_of_block_and_margin * margin;
        float step = block + margin;
        float x_offset = - (blocks_in_row / 2) * block - (blocks_in_row / 2 -0.5f) * margin;


        float f = 120;
        //初始化方块
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 8; j ++){
                squares[i*8+j] = new GLSquare();
                squares[i*8+j].set(
                        x_offset + step * j ,
                        1 - step * (i+1),
                        8.0f+ (j+0.2f*i),
                        margin * ratio_of_block_and_margin,
                       0.5f * (i+j)
                );

                circles[i*8+j] = new GLCircle(10);
                circles[i*8+j].set(x_offset + step * j + 0.5f * block,
                        1 - step * (i+1) - 0.5f * margin,
                        0.3f * margin);

              characters_x[i * 8 + j] = (x_offset + step * j +0.15f * step)  *0.5f;
              characters_y[i * 8 + j] = (1 - step * (i+1) - 0.05f * step) * 0.5f;
              characters_f[i * 8 + j] = 8.0f+ (j+0.2f*i);
              characters_p[i * 8 + j] = 0.5f * (i+j);

            }
        }
    }

    private void drawFlashingBLocks(){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //依次绘制
        for(int i = 0; i< 40 ; i++){
            squares[i].draw(ratio, begin_time, (i == red));
            if(i==red)circles[i].draw(ratio, true);
        }
        Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        glText.begin( 0.0f, 0.0f, 0.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color BLUE)
        for(int i = 0; i < 40 ; i++){
            if(i == 36)continue;
            glText.draw(buttons[i],characters_x[i] * height ,characters_y[i]  * height );
        }
        glText.end();                                   // End Text Rendering
    }

    private void drawFlashingLetters(){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //依次绘制
        for(int i = 0; i< 40 ; i++){
//            squares[i].draw(ratio, begin_time, (i == red));

            if(i==red)circles[i].draw(ratio, true);
        }

        Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        // Begin Text Rendering
        glText.begin( 0.0f, 0.0f, 0.0f, 1.0f, mVPMatrix );
        long time =  System.nanoTime();
        for(int i = 0; i < 40 ; i++){
            if(time > begin_time ){
                long t = time - begin_time;
                float k = (float)(
                        Math.sin((characters_p[i] + t * 0.001 * 0.001 * 0.002  * characters_f[i]
                                ) * 3.1415926f) + 1) /2;
                glText.setColor(k,k,k,0);
            }else{
                glText.setColor(0.0f,0.0f,0.0f,0.0f);
            }
            glText.draw(buttons[i],characters_x[i] * height ,characters_y[i]  * height );
        }
        glText.end();                                             // End Text Rendering
    }

    public void onDrawFrame(GL10 unused) {
        if(has_blocks){
            drawFlashingBLocks();
        }else{
            buttons[36] = " ";
            //The real space (ASCII 0x20) in the font we use is a hollow square.
            drawFlashingLetters();
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) { //		gl.glViewport( 0, 0, width, height );

        //Change font size here.
        glText.setScale(height / 730f);

        GLES20.glViewport(0, 0, width, height);
        ratio = (float) width / height;

        // Take into account device orientation
        if (width > height) {
            Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        }
        else {
            Matrix.frustumM(mProjMatrix, 0, -1, 1, -1/ratio, 1/ratio, 1, 10);
        }

        // Save width and height
        this.width = width;                             // Save Current Width
        this.height = height;                           // Save Current Height
        Log.d(TAG,"width:"+width+" height:"+height);

        int useForOrtho = Math.min(width, height);

        Matrix.orthoM(mVMatrix, 0,
                -useForOrtho/2,
                useForOrtho/2,
                -useForOrtho/2,
                useForOrtho/2, 0.1f, 100f);

        Log.d(TAG,"onSurfaceChanged end");
    }
}
