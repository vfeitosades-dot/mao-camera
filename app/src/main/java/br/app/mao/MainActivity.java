package br.app.mao;
import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.webkit.*;
import android.net.Uri;
import java.io.*;
public class MainActivity extends Activity {
 private WebView web;private DeviceBridge device;private PermissionRequest cameraRequest;private ValueCallback<Uri[]> fileRequest;
 public void onCreate(Bundle state){super.onCreate(state);device=new DeviceBridge(this);open();}
 private void open(){web=new WebView(this);setContentView(web);WebSettings settings=web.getSettings();settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);settings.setMediaPlaybackRequiresUserGesture(false);settings.setAllowFileAccess(false);settings.setAllowContentAccess(true);web.addJavascriptInterface(device,"PocketDevice");
 web.setWebViewClient(new WebViewClient(){
 public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request){Uri target=request.getUrl();if("https".equals(target.getScheme())&&"app.local".equals(target.getHost()))return false;if(request.isForMainFrame()&&("https".equals(target.getScheme())||"http".equals(target.getScheme())))BrowserWindow.show(MainActivity.this,target.toString());return true;}
 public WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request){Uri u=request.getUrl();String path=u.getPath();if(!"https".equals(u.getScheme())||!"app.local".equals(u.getHost())||path==null||path.contains("..")||!path.matches("/[a-zA-Z0-9_./-]+"))return empty(403,"Forbidden");String mime=path.endsWith(".html")?"text/html":path.endsWith(".js")?"application/javascript":path.endsWith(".wasm")?"application/wasm":path.endsWith(".json")?"application/json":"application/octet-stream";try{return new WebResourceResponse(mime,path.endsWith(".html")||path.endsWith(".js")||path.endsWith(".json")?"UTF-8":null,getAssets().open(path.substring(1)));}catch(IOException e){return empty(404,"Not Found");}}
 });
 web.setWebChromeClient(new WebChromeClient(){
 public void onPermissionRequest(PermissionRequest request){if(!"https".equals(request.getOrigin().getScheme())||!"app.local".equals(request.getOrigin().getHost())){request.deny();return;}boolean video=false;for(String resource:request.getResources())if(PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(resource))video=true;if(!video){request.deny();return;}if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});else{if(cameraRequest!=null)cameraRequest.deny();cameraRequest=request;requestPermissions(new String[]{Manifest.permission.CAMERA},1);}}
 public void onPermissionRequestCanceled(PermissionRequest request){if(request==cameraRequest)cameraRequest=null;}
 public boolean onShowFileChooser(WebView view,ValueCallback<Uri[]> callback,FileChooserParams params){if(fileRequest!=null)fileRequest.onReceiveValue(null);fileRequest=callback;Intent pick=new Intent(Intent.ACTION_OPEN_DOCUMENT);pick.addCategory(Intent.CATEGORY_OPENABLE);pick.setType("*/*");pick.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/*","video/*"});try{startActivityForResult(pick,2);}catch(android.content.ActivityNotFoundException e){fileRequest.onReceiveValue(null);fileRequest=null;}return true;}
 });web.loadUrl("https://app.local/index.html");}
 private WebResourceResponse empty(int status,String reason){return new WebResourceResponse("text/plain","UTF-8",status,reason,java.util.Collections.emptyMap(),new ByteArrayInputStream(new byte[0]));}
 public void onRequestPermissionsResult(int code,String[] permissions,int[] result){super.onRequestPermissionsResult(code,permissions,result);if(cameraRequest!=null){if(result.length>0&&result[0]==PackageManager.PERMISSION_GRANTED)cameraRequest.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});else cameraRequest.deny();cameraRequest=null;}}
 protected void onActivityResult(int code,int result,Intent data){super.onActivityResult(code,result,data);if(code==2&&fileRequest!=null){fileRequest.onReceiveValue(result==RESULT_OK&&data!=null?new Uri[]{data.getData()}:null);fileRequest=null;}}
 protected void onPause(){if(device!=null)device.pause();if(web!=null){web.evaluateJavascript("window.Pocket&&Pocket.pause()",null);web.onPause();}super.onPause();}
 protected void onResume(){super.onResume();if(device!=null)device.resume();if(web!=null)web.onResume();}
 protected void onDestroy(){if(device!=null)device.pause();if(cameraRequest!=null)cameraRequest.deny();if(fileRequest!=null)fileRequest.onReceiveValue(null);if(web!=null)web.destroy();super.onDestroy();}
}
