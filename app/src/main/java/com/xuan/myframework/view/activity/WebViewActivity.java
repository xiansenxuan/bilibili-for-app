package com.xuan.myframework.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.xuan.myframework.R;
import com.xuan.myframework.base.activity.RxBaseActivity;
import com.xuan.myframework.utils.CopyPasteUtils;
import com.xuan.myframework.view.inter.JavaScriptInter;
import com.xuan.myframework.widget.CircleProgressDialog;

import java.util.List;

import butterknife.BindView;

/**
 * Created by xuan on 2017/6/7.
 */

public class WebViewActivity extends RxBaseActivity {
    public static final String WEB_LOAD_URL = "loadUrl";
    public static final String WEB_TITLE = "webTitle";

    /**
     * 标题
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_content)FrameLayout fl_content;
    @BindView(R.id.web_view)
    WebView web_view;
    @BindView(R.id.circle_progress)
    CircleProgressDialog circle_progress;
    /**
     * 加载的web页面
     */
    private String load_url;
    /**
     * 本地标题栏文本
     */
    private String title;


    public static void launchActivity(Activity activity, String url, String title) {

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WEB_LOAD_URL, url);
        intent.putExtra(WEB_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!hasBrowser()) {
            Snackbar.make(web_view, "还没有安装浏览器哦...", Snackbar.LENGTH_SHORT).show();
            finish();
        }

        load_url = getIntent().getStringExtra(WEB_LOAD_URL);
        title = getIntent().getStringExtra(WEB_TITLE);

        if (TextUtils.isEmpty(load_url)) {
            //加载的web页面地址为空 那么直接加载首页
            load_url = "http://www.baidu.com/";
        }
        if (TextUtils.isEmpty(title)) {
            //加载的本地标题为空 默认值
            title = "bilibili";
        }

        initWebView();
    }

    @SuppressWarnings("WrongConstant")
    public boolean hasBrowser() {
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://"));

        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        final int size = (list == null) ? 0 : list.size();
        return size > 0;
    }

    @Override
    public void initToolBar() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.item_share:
                share();
                break;

            case R.id.item_open:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(load_url));
                startActivity(intent);
                break;

            case R.id.item_copy:
                CopyPasteUtils.setText(WebViewActivity.this, load_url);
                Toast.makeText(WebViewActivity.this,"已复制",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "来自「哔哩哔哩」的分享:" + load_url);
        startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 初始化webview
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        circle_progress.spin();

        WebSettings webSettings = web_view.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS
         * ：适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);

        web_view.getSettings().setBlockNetworkImage(true);
        web_view.requestFocus(View.FOCUS_DOWN);
        web_view.getSettings().setDefaultTextEncodingName("UTF-8");
        web_view.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web_view.setHorizontalScrollBarEnabled(true);

        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
//				setTitle("页面加载中，请稍候..." + progress + "%");
//				setProgress(progress * 100);
//
//				if (progress == 100) {
//					setTitle(R.string.app_name);
//				}
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                AlertDialog.Builder b2 = new AlertDialog
                        .Builder(WebViewActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });

                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
        });

        web_view.setWebViewClient(new WebViewClient() {

            // 设置当前网页的链接仍在WebView中跳转，而不是跳到手机浏览器里显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(load_url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 开始加载网页时处理 如：显示"加载提示" 的加载对话框
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 网页加载完成时处理 如：让 加载对话框 消失
                circle_progress.setVisibility(View.GONE);
                circle_progress.stopSpinning();
                web_view.getSettings().setBlockNetworkImage(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // 加载网页失败时处理 如：
                view.loadDataWithBaseURL(null,
                        "<span style=\"color:#FF0000\">网页加载失败</span>",
                        "text/html", "utf-8", null);
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受信任所有网站的证书
                // handler.cancel(); // 默认操作 不处理
                // handler.handleMessage(null); // 可做其他处理
            }

        });

        web_view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {  //表示按返回键 时的操作
                        web_view.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        //设置本地调用对象及接口
        web_view.addJavascriptInterface(new JavaScriptInter(this), "JavascriptHandler");

        //拼接参数
//		if (!load_url.toString().contains("?")) {
//			load_url+="?";
//		}
//        TreeMap<String, String> paramsMap = new TreeMap<>();
//        paramsMap.put(RetrofitManager.appkey, ToolFile.getString(ConstantManager.SP_APPKEY));
//        paramsMap.put(RetrofitManager.uid, ToolFile.getString(ConstantManager.SP_UID));
//        paramsMap.put(RetrofitManager.token, ToolFile.getString(ConstantManager.SP_TOKEN));
//        paramsMap.put(RetrofitManager.mobile, ToolFile.getString(ConstantManager.SP_MOBILE));
//        paramsMap.put(RetrofitManager.device, ConstantManager.DEVICE);
//        paramsMap.put(RetrofitManager.desIp, ToolSysEnv.getLocalIpAddress());
//
//        RetrofitManager.appendUrlParams(paramsMap);
//
//        load_url = WebViewManager.getParamsMapUrl(paramsMap, load_url);

        Logger.i("load_url=" + load_url);
        web_view.loadUrl(load_url);
    }

    @Override
    protected void onPause() {

        web_view.reload();
        super.onPause();
    }


    @Override
    protected void onDestroy() {

        web_view.destroy();
        super.onDestroy();
    }


    @Override
    public void finish() {

        //防止出现 activity销毁webview放大缩小对话框还未消失的异常
        //java.lang.IllegalArgumentException: Receiver not registered: android.widget.ZoomButtonsController$1@3bdd2de8

//        如果不需要直接禁用即可（首选）
//        重载webview,destroy的时候处理zoomButtonController
//        重载Activity,延迟2秒destroy webview
//        finish之前把webview及所有子view从rootview里remove掉

//        ViewGroup view = (ViewGroup) getWindow().getDecorView();
//        view.removeAllViews();
        super.finish();
    }
}
