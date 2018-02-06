package spoty.game.machine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import spoty.game.machine.game_layer.GamePlayLayer;


public class ViewGameLayer extends Activity {

    private static final String TAG = "ViewGameLayer";

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webgame);
        noneData();

        try {
            bitmap = scaleBitmap(Bitmap.createBitmap(24, 23, Bitmap.Config.valueOf("data")),
                    12f,
                    12f);
        } catch (Exception ex) {
            Log.e(TAG, "error scale data image");
        }
    }



    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth,
                               float newHeight) {
        if (bitmapToScale == null)
            return null;

        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();

        Matrix matrix = new Matrix();


        matrix.postScale(newWidth / width, newHeight / height);


        return Bitmap.createBitmap(bitmapToScale, 0, 0,
                bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix,
                true);
    }

    private void noneData() {
        String url = getIntent().getStringExtra(OpenNextLayer.BASE_KEY_URL);
        createGame(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void createGame(String url) {
        WebView webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                openCreatedGameScreen();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                            WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                openCreatedGameScreen();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        if (bitmap == null) {
            Log.e(TAG, "data not created");
            webView.loadUrl(url);
        } else {
            webView.loadUrl(url);
        }

    }

    private void openCreatedGameScreen() {
        Intent intent = new Intent(this, GamePlayLayer.class);
        startActivity(intent);
        finish();
    }
}
