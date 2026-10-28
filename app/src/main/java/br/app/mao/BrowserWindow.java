package br.app.mao;
import android.app.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.webkit.*;
import android.net.Uri;
import org.json.JSONObject;
public final class BrowserWindow {
 public static void show(Activity activity,String initial){
  Dialog dialog=new Dialog(activity);dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
  LinearLayout root=new LinearLayout(activity);root.setOrientation(1);root.setPadding(12,12,12,12);
  GradientDrawable bg=new GradientDrawable();bg.setColor(Color.rgb(21,27,42));bg.setCornerRadius(28);root.setBackground(bg);
  LinearLayout nav=new LinearLayout(activity);EditText address=new EditText(activity);address.setSingleLine(true);address.setText(initial);address.setTextColor(Color.WHITE);address.setShowSoftInputOnFocus(false);
  Button back=new Button(activity);back.setText("‹");Button keys=new Button(activity);keys.setText("⌨");Button close=new Button(activity);close.setText("×");Button go=new Button(activity);go.setText("Ir");
  for(Button item:new Button[]{back,go,keys,close}){item.setMinWidth(0);item.setMinimumWidth(0);item.setPadding(0,0,0,0);}nav.addView(back,new LinearLayout.LayoutParams(dp(activity,36),dp(activity,48)));nav.addView(address,new LinearLayout.LayoutParams(0,dp(activity,48),1));nav.addView(go,new LinearLayout.LayoutParams(dp(activity,36),dp(activity,48)));nav.addView(keys,new LinearLayout.LayoutParams(dp(activity,36),dp(activity,48)));nav.addView(close,new LinearLayout.LayoutParams(dp(activity,36),dp(activity,48)));root.addView(nav);
  WebView browser=new WebView(activity);WebSettings settings=browser.getSettings();settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);settings.setAllowFileAccess(false);settings.setAllowContentAccess(false);settings.setGeolocationEnabled(false);settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
  CookieManager.getInstance().setAcceptThirdPartyCookies(browser,false);browser.setWebChromeClient(new WebChromeClient(){public void onPermissionRequest(PermissionRequest request){request.deny();}});
  browser.setWebViewClient(new WebViewClient(){
   public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest req){String scheme=req.getUrl().getScheme();return !"https".equals(scheme)&&!"http".equals(scheme);}
   public void onPageFinished(WebView view,String url){address.setText(url);}
  });
  root.addView(browser,new LinearLayout.LayoutParams(-1,0,1));
  LinearLayout keyboard=new LinearLayout(activity);keyboard.setOrientation(1);keyboard.setPadding(8,8,8,8);keyboard.setBackgroundColor(Color.rgb(36,44,63));keyboard.setVisibility(View.GONE);
  boolean[] shift={false};
  for(String row:new String[]{"1234567890","qwertyuiop","asdfghjkl","zxcvbnm","./:-_@?&="}){
   LinearLayout line=new LinearLayout(activity);
   for(char letter:row.toCharArray()){Button key=new Button(activity);key.setText(String.valueOf(letter));key.setPadding(0,0,0,0);key.setMinWidth(0);key.setMinimumWidth(0);
    key.setOnClickListener(v->{String text=shift[0]?String.valueOf(letter).toUpperCase():String.valueOf(letter);insert(browser,address,text,false);});
    line.addView(key,new LinearLayout.LayoutParams(0,dp(activity,40),1));}
   keyboard.addView(line);
  }
  LinearLayout commands=new LinearLayout(activity);
  for(String label:new String[]{"⇧","Espaço","⌫","Enter","Ocultar"}){Button key=new Button(activity);key.setText(label);key.setOnClickListener(v->{if(label.equals("⇧")){shift[0]=!shift[0];key.setText(shift[0]?"⇧ ON":"⇧");}else if(label.equals("Espaço"))insert(browser,address," ",false);else if(label.equals("⌫"))insert(browser,address,"",true);else if(label.equals("Ocultar"))keyboard.setVisibility(View.GONE);else if(address.hasFocus())go.performClick();else browser.evaluateJavascript("(function(){let e=document.activeElement;if(e&&e.form)e.form.requestSubmit();})()",null);});commands.addView(key,new LinearLayout.LayoutParams(0,dp(activity,44),1));}keyboard.addView(commands);root.addView(keyboard);
  Runnable navigate=()->{String url=address.getText().toString().trim();if(!url.startsWith("https://")&&!url.startsWith("http://"))url="https://"+url;Uri uri=Uri.parse(url);if(uri.getHost()!=null&&uri.getHost().contains(".")){browser.loadUrl(url);address.clearFocus();browser.requestFocus();}};
  go.setOnClickListener(v->navigate.run());back.setOnClickListener(v->{if(browser.canGoBack())browser.goBack();});close.setOnClickListener(v->dialog.dismiss());keys.setOnClickListener(v->keyboard.setVisibility(keyboard.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE));
  dialog.setContentView(root);dialog.setOnDismissListener(v->{browser.stopLoading();browser.destroy();});dialog.show();
  Window window=dialog.getWindow();if(window!=null){window.setBackgroundDrawableResource(android.R.color.transparent);window.setLayout((int)(activity.getResources().getDisplayMetrics().widthPixels*.96),(int)(activity.getResources().getDisplayMetrics().heightPixels*.88));window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);}
  browser.loadUrl(initial);
 }
 private static int dp(Activity activity,int n){return (int)(n*activity.getResources().getDisplayMetrics().density);}
 private static void insert(WebView browser,EditText address,String text,boolean delete){
  if(address.hasFocus()){int a=Math.max(0,address.getSelectionStart()),b=Math.max(a,address.getSelectionEnd());if(delete&&a==b&&a>0)a--;address.getText().replace(a,b,text);return;}
  String js="(function(){const e=document.activeElement;if(!e)return;const t="+JSONObject.quote(text)+";if(e.tagName==='INPUT'||e.tagName==='TEXTAREA'){let a=e.selectionStart,b=e.selectionEnd;if(a===null||b===null)return;if("+delete+"&&a===b&&a>0)a--;const setter=Object.getOwnPropertyDescriptor(e.tagName==='INPUT'?HTMLInputElement.prototype:HTMLTextAreaElement.prototype,'value').set;setter.call(e,e.value.slice(0,a)+t+e.value.slice(b));e.setSelectionRange(a+t.length,a+t.length);e.dispatchEvent(new Event('input',{bubbles:true}));}else if(e.isContentEditable){document.execCommand("+JSONObject.quote(delete?"delete":"insertText")+",false,t);}})()";
  browser.evaluateJavascript(js,null);
 }
}
