package com.java.lichenghao.eltext.shape;


import static android.opengl.GLES20.glLineWidth;

import android.opengl.GLES20;
import android.util.Log;
import android.widget.GridLayout;

import java.nio.FloatBuffer;
import java.util.Date;

public class GLSquare {
    // 顶点着色器的脚本
    String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +         //接收传入的转换矩阵
                    " attribute vec4 vPosition;" +      //接收传入的顶点
                    " void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition ;" +  //矩阵变换计算之后的位置
                    " }";

    // 顶点着色器的脚本
//    String vertexShaderCode =
//            " attribute vec4 vPosition;" +     // 应用程序传入顶点着色器的顶点位置
//                    " void main() {" +
//                    "     gl_Position = vPosition;" +  // 此次绘制此顶点位置
//                    " }";

    // 片元着色器的脚本
    String fragmentShaderCode =
            " precision mediump float;" +  // 声明float类型的精度为中等(精度越高越耗资源)
                    " uniform vec4 vColor;" +       // 接收传入的颜色
                    " void main() {" +
                    "     gl_FragColor = vColor;" +  // 给此片元的填充色
                    " }";

    private FloatBuffer vertexBuffer;  //顶点坐标数据要转化成FloatBuffer格式
    private FloatBuffer outerVertexBuffer;  //顶点坐标数据要转化成FloatBuffer格式





    // 数组中每3个值作为一个坐标点
    static final int COORDS_PER_VERTEX = 3;
    //坐标数组

    private float squareCoords[] = {
            0f, 1f, 0f, // top left
            1f, 1f, 0f,  // top right
            0f, 0f, 0f, // bottom left
            1f, 0f, 0f  // bottom right
    };

    private final float outerSize = 0.05f;

    private float outerSquareCoords[] = {
            1 + outerSize, 1 + outerSize, 0f,  // top right
            -outerSize, 1 + outerSize, 0f, // top left
            -outerSize, -outerSize, 0f, // bottom left
            1 + outerSize, -outerSize, 0f  // bottom right
    };




    //顶点个数，计算得出
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per mVertex



    //当前绘制的顶点位置句柄
    private int vPositionHandle;
    //片元着色器颜色句柄
    private int vColorHandle;
    //变换矩阵句柄
    private int mMVPMatrixHandle;
    //这个可以理解为一个OpenGL程序句柄
    private  int mProgram;

    //变换矩阵
    private float[] mvpMatrix = {
            1f,0f,0f,0f,
            0f,1f,0f,0f,
            0f,0f,1f,0f,
            0f,0f,0f,1f
    };

    public GLSquare(){

    }

    public void set(float x , float y , float size) {
        // 坐标系：
        // 以区域高度为2个单位长度。中心为（0，0）.
        // 绘制左下为(x,y), 边长是高度的0.5*size倍的正方形
        for(int i =0; i < vertexCount; i++){
            squareCoords[3*i] *= size;
            squareCoords[3*i] += x;
            squareCoords[3*i+1] *= size;
            squareCoords[3*i+1] += y;
            outerSquareCoords[3*i] *= size;
            outerSquareCoords[3*i] += x;
            outerSquareCoords[3*i+1] *= size;
            outerSquareCoords[3*i+1] += y;
        }


        /** 1、数据转换，顶点坐标数据float类型转换成OpenGL格式FloatBuffer，int和short同理*/
        vertexBuffer = GLUtil.floatArray2FloatBuffer(squareCoords);
        outerVertexBuffer = GLUtil.floatArray2FloatBuffer(outerSquareCoords);


        /** 2、加载编译顶点着色器和片元着色器*/
        int vertexShader = GLUtil.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLUtil.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        /** 3、创建空的OpenGL ES程序，并把着色器添加进去*/
        mProgram = GLES20.glCreateProgram();

        // 添加顶点着色器到程序中
        GLES20.glAttachShader(mProgram, vertexShader);

        // 添加片段着色器到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);

        /** 4、链接程序*/
        GLES20.glLinkProgram(mProgram);

    }


    public void draw(float ratio, boolean isRed, float grey) {

        float mColor[] = {grey, grey, grey, grey};
        mvpMatrix[0] = mvpMatrix[5] / ratio;

        // 将程序添加到OpenGL ES环境
        GLES20.glUseProgram(mProgram);

        /***1.获取句柄*/
        // 获取顶点着色器的位置的句柄（这里可以理解为当前绘制的顶点位置）
        vPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 获取片段着色器的vColor句柄
        vColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 获取变换矩阵的句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        /**2.设置数据*/
        // 启用顶点属性，最后对应禁用
        GLES20.glEnableVertexAttribArray(vPositionHandle);

        //准备正方形坐标数据
        GLES20.glVertexAttribPointer(vPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        // 设置绘制正方形的颜色，给vColor 这个变量赋值
        GLES20.glUniform4fv(vColorHandle, 1, mColor, 0);
        // 将投影和视图转换传递给着色器，可以理解为给uMVPMatrix这个变量赋值为mvpMatrix
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        /** 3.绘制正方形，4个顶点*/
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);


        if(isRed){
            //准备红色外框坐标数据
            GLES20.glVertexAttribPointer(vPositionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    vertexStride, outerVertexBuffer);
            // 设置绘制正方形的颜色，给vColor 这个变量赋值
            float red[] = {1f, 0f, 0f, 0f};

            GLES20.glUniform4fv(vColorHandle, 1, red, 0);

            /** 3.绘制外边框*/
            glLineWidth(5);
            GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);
        }
        // 禁用顶点数组（好像不禁用也没啥问题）
        GLES20.glDisableVertexAttribArray(vPositionHandle);
    }
}
