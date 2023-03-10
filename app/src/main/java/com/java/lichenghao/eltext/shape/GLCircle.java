package com.java.lichenghao.eltext.shape;


import android.opengl.GLES20;

import java.nio.FloatBuffer;

public class GLCircle {
    // 顶点着色器的脚本
    String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +         //接收传入的转换矩阵
                    " attribute vec4 vPosition;" +      //接收传入的顶点
                    " void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition ;" +  //矩阵变换计算之后的位置
                    " }";


    // 片元着色器的脚本
    String fragmentShaderCode =
            " precision mediump float;" +  // 声明float类型的精度为中等(精度越高越耗资源)
                    " uniform vec4 vColor;" +       // 接收传入的颜色
                    " void main() {" +
                    "     gl_FragColor = vColor;" +  // 给此片元的填充色
                    " }";

    private FloatBuffer vertexBuffer;  //顶点坐标数据要转化成FloatBuffer格式




    // 数组中每3个值作为一个坐标点
    static final int COORDS_PER_VERTEX = 3;
    //坐标数组

    private float[] circleCoords;



    //顶点个数，構造函數中计算得出
    private int vertexCount ;

    private int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per mVertex

    //颜色数组，rgba， 紅色
    private float[] mColor = {
            1.0f, 0.0f, 0.0f, 0.0f,
    };

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

    public GLCircle(int n){

        vertexCount = n + 2;
        circleCoords = new float[vertexCount * COORDS_PER_VERTEX];
        circleCoords[0] = 0;
        for(int i = 0; i <= n; i++){
            circleCoords[(i+1) * 3] = (float) Math.sin((double) i/(double) n * 2 * Math.acos(-1f));
            circleCoords[(i+1) * 3 + 1] = (float) Math.cos((double) i/(double) n  * 2 * Math.acos(-1f));
            circleCoords[(i+1) * 3 + 2] = 0;
        }
    }

    public void set(float x , float y , float radius) {
        // 坐标系：
        // 以区域高度为2个单位长度。中心为（0，0）.
        // 绘制左下为(x,y), 边长是高度的0.5*size倍的正方形


        for(int i =0; i < vertexCount; i++){
            circleCoords[3*i] *= radius;
            circleCoords[3*i] += x;
            circleCoords[3*i+1] *= radius;
            circleCoords[3*i+1] += y;
        }


        /* 1、数据转换，顶点坐标数据float类型转换成OpenGL格式FloatBuffer，int和short同理*/
        vertexBuffer = GLUtil.floatArray2FloatBuffer(circleCoords);


        /* 2、加载编译顶点着色器和片元着色器*/
        int vertexShader = GLUtil.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLUtil.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        /* 3、创建空的OpenGL ES程序，并把着色器添加进去*/
        mProgram = GLES20.glCreateProgram();

        // 添加顶点着色器到程序中
        GLES20.glAttachShader(mProgram, vertexShader);

        // 添加片段着色器到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);

        /* 4、链接程序*/
        GLES20.glLinkProgram(mProgram);

    }


    public void draw(float ratio,  boolean isRed) {

        if(!isRed){
            mColor[0] = 0f;
        }else{
            mColor[0] = 1f;
        }

        mvpMatrix[0] = mvpMatrix[5] / ratio;

        // 将程序添加到OpenGL ES环境
        GLES20.glUseProgram(mProgram);

        /*1.获取句柄*/
        // 获取顶点着色器的位置的句柄（这里可以理解为当前绘制的顶点位置）
        vPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 获取片段着色器的vColor句柄
        vColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 获取变换矩阵的句柄
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        /*2.设置数据*/
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

        /* 3.绘制正方形，4个顶点*/
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // 禁用顶点数组（好像不禁用也没啥问题）
        GLES20.glDisableVertexAttribArray(vPositionHandle);
    }
}
