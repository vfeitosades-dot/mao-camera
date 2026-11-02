package br.vfeitosades.handtrackingvr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

public final class MainActivity extends Activity {
    private WebView web;
    private PermissionRequest cameraRequest;
    private static final String HOST = "app.local";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        immersive();
        web = new WebView(this);
        web.setBackgroundColor(0xff05070d);
        WebSettings s=web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest request) {
                Uri u=request.getUrl();
                if(isLocal(u))return false;
                if(!request.isForMainFrame())return false;
                if("https".equals(u.getScheme())) {
                    try { startActivity(new Intent(Intent.ACTION_VIEW,u)); } catch(Exception ignored) {}
                }
                return true;
            }
            @Override public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest request) {
                Uri uri=request.getUrl();
                if(!isLocal(uri))return null;
                String p=uri.getEncodedPath();
                if(p==null || !p.matches("/[a-zA-Z0-9_./-]+") || p.contains(".."))
                    return fail(403,"Forbidden");
                String name=p.substring(1);
                if(name.isEmpty())name="index.html";
                String mime="application/octet-stream";
                if(name.endsWith(".html"))mime="text/html";
                else if(name.endsWith(".mjs")||name.endsWith(".js"))mime="text/javascript";
                else if(name.endsWith(".css"))mime="text/css";
                else if(name.endsWith(".wasm"))mime="application/wasm";
                else if(name.endsWith(".json"))mime="application/json";
                else if(name.endsWith(".task"))mime="application/octet-stream";
                try {
                    return new WebResourceResponse(mime,mime.startsWith("text/")?"UTF-8":null,getAssets().open(name));
                } catch(IOException ex) { return fail(404,"Not Found"); }
            }
        });
        web.setWebChromeClient(new WebChromeClient(){
            @Override public void onPermissionRequest(PermissionRequest request){
                Uri origin=request.getOrigin();
                boolean camera=false,other=false;
                for(String resource:request.getResources()) {
                    if(PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource))camera=true;
                    else other=true;
                }
                if(!isLocal(origin)||!camera||other){request.deny();return;}
                runOnUiThread(()->{
                    if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
                        request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                    else {
                        if(cameraRequest!=null)cameraRequest.deny();
                        cameraRequest=request;
                        requestPermissions(new String[]{Manifest.permission.CAMERA},42);
                    }
                });
            }
            @Override public void onPermissionRequestCanceled(PermissionRequest request){
                if(cameraRequest==request)cameraRequest=null;
            }
        });
        setContentView(web);
        web.loadUrl("https://app.local/index.html");
    }
    private static boolean isLocal(Uri u) {
        return u!=null&&"https".equals(u.getScheme())&&HOST.equals(u.getHost());
    }
    private static WebResourceResponse fail(int code,String message) {
        return new WebResourceResponse("text/plain","UTF-8",code,message,Collections.emptyMap(),new ByteArrayInputStream(new byte[0]));
    }
    private void immersive() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
    @Override public void onWindowFocusChanged(boolean focus){
        super.onWindowFocusChanged(focus);if(focus)immersive();
    }
    @Override public void onRequestPermissionsResult(int code,String[] permissions,int[] results){
        super.onRequestPermissionsResult(code,permissions,results);
        if(code==42&&cameraRequest!=null) {
            if(results.length>0&&results[0]==PackageManager.PERMISSION_GRANTED)
                cameraRequest.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
            else cameraRequest.deny();
            cameraRequest=null;
        }
    }
    @Override protected void onPause(){
        if(web!=null){
            web.evaluateJavascript("try{var v=document.getElementById('v');if(v&&v.srcObject){v.srcObject.getTracks().forEach(t=>t.stop());v.srcObject=null;}}catch(e){}",null);
            web.onPause();
        }
        super.onPause();
    }
    @Override protected void onResume(){super.onResume();if(web!=null)web.onResume();}
    @Override protected void onDestroy(){
        if(cameraRequest!=null){cameraRequest.deny();cameraRequest=null;}
        if(web!=null){web.loadUrl("about:blank");web.destroy();web=null;}
        super.onDestroy();
    }
}
