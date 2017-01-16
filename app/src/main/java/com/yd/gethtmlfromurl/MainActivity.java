package com.yd.gethtmlfromurl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private WebView mWebView;
    final Context myApp = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    /* An instance of this class will be registered as a JavaScript interface */
    public class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void showHTML(String html)
        {
            TextView txtView = (TextView) ((Activity)myApp).findViewById(R.id.textview_insertHTML);
            txtView.setText(html);

            /* for test
            new AlertDialog.Builder(myApp)
                    .setTitle("HTML code")
                    .setMessage(html)
                    .setPositiveButton(android.R.string.ok, null)
                    .setCancelable(false)
                    .create()
                    .show();
            */
        }
    }

    public void getHTML(View view){

        EditText ed = (EditText) findViewById(R.id.edittext_siteurl);
        String urlText = ed.getText().toString();

        mProgressDialog = ProgressDialog.show(this, "", "Getting html code...");

        mWebView = (WebView)findViewById(R.id.webview_html);
        // JavaScript must be enabled
        mWebView.getSettings().setJavaScriptEnabled(true);

        // Register a new JavaScript interface called HTMLOUT
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        // WebViewClient must be set BEFORE calling loadUrl!
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                // This call inject JavaScript into the page which just finished loading.
                mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                mProgressDialog.dismiss();
            }
        });

        // set URL address right
        urlText = setURL(urlText);
        // load a web page */
        mWebView.loadUrl(urlText);

    }

    /*
    *   Checking input string for correct transfer protocol (http or https)
    *   @param  str  String of URL, that we will show in WebView
    *   @return     String with corrected beginning (type of protocol)
    */
    public String setURL(String str){
        boolean b1 = str.startsWith("http://");
        boolean b2 = str.startsWith("https://");
        if(!b1 && !b2){
            str = "http://" + str;
        }
        return str;
    }

}
