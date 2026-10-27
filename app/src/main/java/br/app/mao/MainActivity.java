package br.app.mao;
import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.webkit.*;
import android.net.Uri;
import java.io.*;
public class MainActivity extends Activity {
 private WebView web;
 public void onCreate(Bundle state) {super.onCreate(state); if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.CAMERA},1);else open();}
 public void onRequestPermissionsResult(int request,String[] permissions,int[] results){super.onRequestPermissionsResult(request,permissions,results);if(results.length>0&&results[0]==PackageManager.PERMISSION_GRANTED)open();else{android.widget.TextView text=new android.widget.TextView(this);text.setText("Permita a câmera nas configurações do Android e abra novamente.");setContentView(text);}}
 private void open(){web=new WebView(this);setContentView(web);web.getSettings().setJavaScriptEnabled(true);web.getSettings().setDomStorageEnabled(true);web.getSettings().setMediaPlaybackRequiresUserGesture(false);web.getSettings().setAllowFileAccess(false);web.getSettings().setAllowContentAccess(false);
 web.setWebViewClient(new WebViewClient(){
 public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request){return !request.getUrl().toString().equals("https://app.local/index.html");}
 public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request){Uri u=request.getUrl();if("app.local".equals(u.getHost())){try{if(!"/index.html".equals(u.getPath()))return new WebResourceResponse("text/plain","UTF-8",new ByteArrayInputStream(new byte[0]));return new WebResourceResponse("text/html","UTF-8",getAssets().open("index.html"));}catch(IOException e){return new WebResourceResponse("text/plain","UTF-8",new ByteArrayInputStream(new byte[0]));}}return null;}});
 web.setWebChromeClient(new WebChromeClient(){public void onPermissionRequest(PermissionRequest r){if("https".equals(r.getOrigin().getScheme())&&"app.local".equals(r.getOrigin().getHost())&&checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){for(String resource:r.getResources())if(PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource)){r.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});return;}}r.deny();}});web.loadUrl("https://app.local/index.html");}
 protected void onPause(){if(web!=null){web.evaluateJavascript("stop()",null);web.onPause();}super.onPause();}
 protected void onResume(){super.onResume();if(web!=null)web.onResume();}
 protected void onDestroy(){if(web!=null)web.destroy();super.onDestroy();}
}