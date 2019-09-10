package ru.android.colledgemap;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private OpenGLSurfaceView mGLView;
    private OpenGLRenderer mRenderer = new OpenGLRenderer(this);

    private float near = 1f;
    private float mAngle = 0f;
    private int checkLevel = 1;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Check();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    public void Check() {
        mGLView = new OpenGLSurfaceView(this);
        NearZoom(near);

        setContentView(R.layout.activity_main);
        mGLView = (OpenGLSurfaceView) findViewById(R.id.glSurfaceView);

        mGLView.setEGLContextClientVersion(2);

        mGLView.setRenderer(mRenderer);

        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        FuncLayer();


    }

    public void FuncLayer(){
        TextView LevelPos = (TextView) findViewById(R.id.textView);
        LevelPos.setText(String.format("Zoom =%.1f", near));

        TextView SwitchLevel = (TextView) findViewById(R.id.textSwitch);

        Button zoomIn = (Button) findViewById(R.id.button2);
        Button zoomOut = (Button) findViewById(R.id.button3);
        Button zoomDef = (Button) findViewById(R.id.buttonDef);

        Button RotateR = (Button) findViewById(R.id.buttonRotateRight);
        Button RotateL = (Button) findViewById(R.id.buttonRotateLeft);
        Button InfoDegree = (Button) findViewById(R.id.buttonDegree);

        RotateR.setText(">");
        RotateL.setText("<");

        InfoDegree.setText(String.format((int)mAngle+"°"));

        RotateL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAngle -= 45;
                mRenderer.getAngle(mAngle);
                if (mAngle==-360f){
                    mAngle=0;
                }
                Check();
            }
        });

        RotateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAngle += 45;
                mRenderer.getAngle(mAngle);
                if (mAngle==360f){
                    mAngle=0;
                }
                Check();
            }
        });

        InfoDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAngle = 0;
                mRenderer.getAngle(mAngle);
                Check();
            }
        });

        zoomDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                near = 1f;
                Check();
            }
        });

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (near >= 0.0f && near <= 4f) {
                    near += 0.5f;
                    NearZoom(near);
                }
                Check();

            }
        });

        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (near >= 0.5f && near <= 4.1f) {
                    near -= 0.5f;
                    NearZoom(near);
                }
                Check();

            }
        });

        Button btnUp = (Button) findViewById(R.id.button4);
        Button btnDwn = (Button) findViewById(R.id.button5);

        if (checkLevel==1){
            btnDwn.setEnabled(false);
        } else if (checkLevel==4){
            btnUp.setEnabled(false);
        }

        if (near<=0.5f){
            zoomOut.setEnabled(false);
        } else if (near>=4.0f){
            zoomIn.setEnabled(false);
        }

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLevel>=1 && checkLevel<4) {
                    getLevel(checkLevel++);
                }
                Check();
            }
        });

        btnDwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLevel> 1 && checkLevel<=4) {
                    getLevel(checkLevel--);
                }
                Check();
            }
        });

        if (checkLevel >= 1 && checkLevel <= 4) {
            switch (checkLevel) {
                case 0:
                    SwitchLevel.setText("Этаж " + checkLevel);
                    break;
                case 1:
                    SwitchLevel.setText("Этаж " + checkLevel);
                    break;
                case 2:
                    SwitchLevel.setText("Этаж " + checkLevel);
                    break;
                case 3:
                    SwitchLevel.setText("Этаж " + checkLevel);
                    break;
                default:
                    SwitchLevel.setText("Этаж " + checkLevel);
                    break;
            }
        }
    }

    public float NearZoom(float nm) {
        float n = nm;
        mRenderer.getNear(n);
        return n;
    }

    public int getLevel(int level){
        int v_level = level;
        mRenderer.getCheckLevel(v_level);
        return v_level;
    }
}
