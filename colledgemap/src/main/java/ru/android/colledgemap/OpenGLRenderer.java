package ru.android.colledgemap;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glViewport;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private Context context;

    ObjLoader loader;

    int posHandle, a_normal_Handle, u_lightPosition_Handle, u_modelViewProjectionMatrix_Handle;

    private int programId;

    private float xLightPosition = 0f, yLightPosition = 5f, zLightPosition = 0f;

    private float[] mModelViewProjectionMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];

    private float mAngle;
    private float near;
    private int checkLevel;

    OpenGLRenderer(Context context) {
        this.context = context;
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float far = 15;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 4;
        float eyeY = 4;
        float eyeZ = 4;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void initializeShaderProgram() {
        glClearColor(0f, 0f, 0f, 1f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        if (checkLevel == 0) {
            loader = new ObjLoader(context);
            loader.load(R.raw.first);
        } else if (checkLevel == 1) {
            loader = new ObjLoader(context);
            loader.load(R.raw.sec);
        } else if (checkLevel == 2) {
            loader = new ObjLoader(context);
            loader.load(R.raw.sec);
        } else if (checkLevel == 3) {
            loader = new ObjLoader(context);
            loader.load(R.raw.sec);
        }

        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);

        loader.vertsBuffer.position(0);
        GLES20.glVertexAttribPointer(
                posHandle, 3, GLES20.GL_FLOAT, false, 0, loader.vertsBuffer);
        GLES20.glEnableVertexAttribArray(posHandle);


        a_normal_Handle = GLES20.glGetAttribLocation(programId, "a_normal");
        loader.normsBuffer.position(0);
        GLES20.glVertexAttribPointer(
                a_normal_Handle, 3, GLES20.GL_FLOAT, false, 0, loader.normsBuffer);
        GLES20.glEnableVertexAttribArray(a_normal_Handle);

        u_lightPosition_Handle = GLES20.glGetUniformLocation(programId, "u_lightPosition");

        u_modelViewProjectionMatrix_Handle =
                GLES20.glGetUniformLocation(programId, "u_ModelViewProjectionMatrix");

        GLES20.glUseProgram(programId);
    }

    //Функция создания проекции и отрисовки объекта
    private void draw() {
        glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.rotateM(mModelMatrix, 0, mAngle, 0.0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, mProjectionMatrix, 0, mModelViewProjectionMatrix, 0);
        glUniformMatrix4fv(u_modelViewProjectionMatrix_Handle, 1, false, mModelViewProjectionMatrix, 0);

        GLES20.glUniform3f(u_lightPosition_Handle, xLightPosition, yLightPosition, zLightPosition);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, loader.numFaces * 3, GLES20.GL_UNSIGNED_SHORT, loader.indicesBuffer);
    }

    private void FirstFloor() {
        draw();
    }

    private void SecondFloor() {
        draw();
    }

    private void ThirdFloor() {
        draw();
    }

    private void FourFloor() {
        draw();
    }

    public int getCheckLevel(int v_checkLevel) {
        checkLevel = v_checkLevel;
        return checkLevel;
    }

    public float getAngle(float angle) {
        mAngle = angle;
        return mAngle;
    }

    public float getNear(float v_near) {
        near = v_near;
        return near;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initializeShaderProgram();
        createViewMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        switch (checkLevel) {
            case 0:
                FirstFloor();
                break;
            case 1:
                SecondFloor();
                break;
            case 2:
                ThirdFloor();
                break;
            case 3:
                FourFloor();
                break;
        }
    }
}