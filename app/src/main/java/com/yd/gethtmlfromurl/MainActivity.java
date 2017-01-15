package com.yd.gethtmlfromurl;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private WebView mWebView;
    private TextView mTextViewHTML;
    private String mString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewHTML = (TextView) findViewById(R.id.textview_insertHTML);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getHTML(View view){

        EditText ed = (EditText) findViewById(R.id.edittext_siteurl);
        String urlText = ed.getText().toString();

        class MyJavaScriptInterface
        {
            @JavascriptInterface
            @SuppressWarnings("unused")
            public void showHTML(String html) {
                mString = html;
                mTextViewHTML.setText(html);
            }
        }

        mProgressDialog = ProgressDialog.show(MainActivity.this, "", "Loading from URL");
        mWebView = (WebView) findViewById(R.id.webview_html);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.getJavaScriptEnabled();
        /* Register a new JavaScript interface called HTMLOUT */
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                mProgressDialog.dismiss();
            /* This call inject JavaScript into the page which just finished loading. */
                //mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                //mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                mWebView.loadUrl("javascript:HTMLOUT.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });
        mWebView.loadUrl(urlText);

/*
        mWebView.evaluateJavascript("(function(){return window.document.body.showHTML})();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        mString = html;
                        mTextViewHTML.setText(html);
                    }
                });
*/

        //mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        //mTextViewHTML.setText(webView.toString());
        String test = mTextViewHTML.getText().toString();

    }

}
