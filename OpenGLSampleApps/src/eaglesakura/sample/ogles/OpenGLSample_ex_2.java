package eaglesakura.sample.ogles;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * 付録B GLSurfaceViewを脱却する
 * @author SAKURA
 *
 */
public class OpenGLSample_ex_2 extends Activity implements SurfaceHolder.Callback {
    private SurfaceView view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new SurfaceView(this);
        view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        view.getHolder().addCallback(this);

        setContentView(view);
    }

    /**
     * 画面がタッチされた。
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //! 再描画を行わせる
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            repaint();
        }

        return true;
    }

    static void log(String str) {
        Log.i("GLES_SAMPLE", str);
    }

    /**
     * 再描画を行う。
     */
    public void repaint() {
        gl10.glClearColor((float) Math.random(), (float) Math.random(), (float) Math.random(), 1.0f);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

        swapBuffers();
    }

    public void swapBuffers() {
        egl.eglSwapBuffers(eglDisplay, eglSurface);
    }

    /**
     * ウィンドウ幅を取得。
     * @return
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * ウィンドウ高を取得。
     * @return
     */
    public int getWindowHeight() {
        return windowHeight;
    }

    /**
     * 描画対象のholder。
     */
    private SurfaceHolder holder;

    /**
     * EGLインターフェース。
     */
    private EGL10 egl;

    /**
     * GLコンテキスト。
     */
    private EGLContext eglContext = null;
    /**
     * ディスプレイ。
     */
    private EGLDisplay eglDisplay = null;
    /**
     * サーフェイス。
     */
    private EGLSurface eglSurface = null;

    /**
     * コンフィグ情報。
     */
    private EGLConfig eglConfig = null;

    /**
     * GL用インターフェース。
     */
    protected GL10 gl10 = null;

    /**
     * 描画先サーフェイスの幅・高さ
     */
    private int windowWidth = -1, windowHeight = -1;

    /**
     * OpenGL初期化処理を行う。
     */
    public void initializeGL() {
        egl = (EGL10) EGLContext.getEGL();

        //! 描画先ディスプレイ確保
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        //! EGL初期化。
        //! ここでGLESのバージョンを取得できるが、ES1.0が必ず帰ってくるようである。
        {
            int[] version = { -1, -1 };
            if (!egl.eglInitialize(eglDisplay, version)) {
                log("!eglInitialize");
                return;
            }
        }

        //! コンフィグ取得
        {
            EGLConfig[] configs = new EGLConfig[1];
            int[] num = new int[1];

            //! この配列でGLの性能を指定する。
            //! ディスプレイの色深度、Z深度もここで指定するが、
            //! 基本的に2D描画する場合はデフォルトのままでも特に問題ない。
            //! specに対応していない値を入れても初期化が失敗する。
            int[] spec = { EGL10.EGL_NONE //! 終端にはEGL_NONEを入れる
            };
            if (!egl.eglChooseConfig(eglDisplay, spec, configs, 1, num)) {
                log("!eglChooseConfig");
                return;
            }

            eglConfig = configs[0];
        }

        //! レンダリングコンテキスト作成
        {
            //レンダリングコンテキスト作成
            eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, null);
            if (eglContext == EGL10.EGL_NO_CONTEXT) {
                log("glContext == EGL10.EGL_NO_CONTEXT");
                return;
            }
        }
        //! 描画先サーフェイスを作成する
        {
            //! SurfaceHolderに結びつける
            eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, holder, null);

            if (eglSurface == EGL10.EGL_NO_SURFACE) {
                log("glSurface == EGL10.EGL_NO_SURFACE");
                return;
            }
        }

        //! GLESインターフェース取得
        {
            gl10 = (GL10) eglContext.getGL();
        }

        //! サーフェイスとコンテキストを結びつける
        {
            if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                log("!eglMakeCurrent");
                return;
            }
        }

        {
            windowWidth = holder.getSurfaceFrame().width();
            windowHeight = holder.getSurfaceFrame().height();

            gl10.glViewport(0, 0, windowWidth, windowHeight);
        }
    }

    /**
     * GLを休止する。
     */
    public void pauseGL() {
        if (eglSurface != null) {
            egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, eglContext);
            egl.eglDestroySurface(eglDisplay, eglSurface);
            eglSurface = null;
        }
    }

    /**
     * GLの復帰を行う。
     */
    public void resumeGL() {
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, holder, null);
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
    }

    /**
     * OpenGL終了処理を行う。
     */
    public void finalizeGL() {
        //サーフェイス破棄
        if (eglSurface != null) {
            egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            egl.eglDestroySurface(eglDisplay, eglSurface);
            eglSurface = null;
        }

        //レンダリングコンテキスト破棄
        if (eglContext != null) {
            egl.eglDestroyContext(eglDisplay, eglContext);
            eglContext = null;
        }

        //ディスプレイコネクション破棄
        if (eglDisplay != null) {
            egl.eglTerminate(eglDisplay);
            eglDisplay = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //! Toastを利用し、SurfacdChangeが通知されたことを明示する。
        //! GLSurfaceViewと違いToastを利用出来る。
        Toast.makeText(this, "surfaceChanged", Toast.LENGTH_LONG).show();

        //!
        if (gl10 == null) {
            log("initialize start");
            //! GLの初期化を行う
            this.holder = holder;
            initializeGL();
        } else {
            log("resume");
            //! GLの復帰を行う
            resumeGL();
        }

        //! 描画を行う
        repaint();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        log("pause");
        pauseGL();
    }
}
