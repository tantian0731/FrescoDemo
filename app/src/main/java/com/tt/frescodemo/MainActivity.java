package com.tt.frescodemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String URL_JPG_DOG =
            "https://ss0.bdstatic.com" +
                    "/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=196405673,1932175745&fm=23&gp=0.jpg";
    SimpleDraweeView iv_test;
    WebView wv;
    Button bt_test0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_test = (SimpleDraweeView) findViewById(R.id.iv_test);
        iv_test.setImageURI(URL_JPG_DOG);
        initWebView();
        bt_test0 = (Button) findViewById(R.id.bt_test0);
        bt_test0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test(URL_JPG_DOG);
            }
        });
    }

    private void test(String url) {
        String localUrl=getFrescoPath(url);
        if (TextUtils.isEmpty(localUrl)){
            return ;
        }
        String imageUrl = "file://"+localUrl;
        String data = "<HTML><IMG src=\""+imageUrl+"\""+"width="+200+"height="+200+"/>";
        wv.loadDataWithBaseURL(imageUrl, data, "text/html", "utf-8", null);
    }

    /**
     * 得到缓存路径
     *
     * @param url
     * @return
     */
    private String getFrescoPath(String url) {
        if (TextUtils.isEmpty(url)){
            return "";
        }
        Uri uri = Uri.parse(url);
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest, this);
        if (!ImagePipelineFactory.getInstance()
                .getMainFileCache().hasKey(cacheKey)) {
            return "";
        }
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainFileCache().getResource(cacheKey);
        File file = ((FileBinaryResource) resource).getFile();
        return file.getAbsolutePath();
    }

    private void initWebView() {
        wv = (WebView) findViewById(R.id.wv);
        WebSettings webSettings = wv.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        wv.setWebViewClient(new WebViewClient());

    }




}
