const http = require('node:http');
const fs = require('node:fs');
const path = require('node:path');
const assert = require('node:assert/strict');
const { chromium } = require('playwright');
(async () => {
 const root = path.resolve('app/src/main/assets');
 const external = [];
 const server = http.createServer((req, res) => {
  const name = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);
  const file = path.resolve(root, '.' + (name === '/' ? '/index.html' : name));
  if (!file.startsWith(root + path.sep)) { res.writeHead(403).end(); return; }
  const mime = file.endsWith('.wasm') ? 'application/wasm' : file.endsWith('.js') ? 'application/javascript' : file.endsWith('.html') ? 'text/html' : 'application/octet-stream';
  fs.readFile(file, (error, data) => { if(error)res.writeHead(404).end();else res.writeHead(200, {'Content-Type':mime}).end(data); });
 });
 await new Promise(resolve => server.listen(0,'127.0.0.1',resolve));
 let browser;
 try {
  browser = await chromium.launch({channel:'chrome',headless:true,args:['--use-gl=angle','--use-angle=swiftshader','--enable-unsafe-swiftshader']});
  const page = await browser.newPage();
  const origin = 'http://127.0.0.1:' + server.address().port;
  await page.route('**/*',route => {
   const url = route.request().url();
   if(url.startsWith(origin + '/') || url.startsWith('blob:') || url.startsWith('data:'))return route.continue();
   external.push(url); return route.abort();
  });
  await page.goto(origin + '/index.html');
  const panelChecks=await page.evaluate(()=>{
   Object.defineProperty(video,'videoWidth',{value:640,configurable:true});Object.defineProperty(video,'videoHeight',{value:480,configurable:true});
   resetQuad();const bounds=panelBounds();
   if(hitButton(bounds.x+bounds.w*.18,bounds.y+bounds.h*.6)!==0)throw Error('Botão site inválido');
   if(hitButton(bounds.x+bounds.w*.48,bounds.y+bounds.h*.6)!==1)throw Error('Botão desenho inválido');
   action(1);if(mode!=='desenho')throw Error('Modo desenho não abriu');
   const hand=Array.from({length:21},()=>({x:.5,y:.55}));
   hand[0]={x:.5,y:.85};hand[9]={x:.5,y:.45};hand[4]={x:.89,y:.9};hand[8]={x:.91,y:.9};
   gesture(hand);hand[4].x=.91;hand[8].x=.93;gesture(hand);
   if(strokes.length!==1||strokes[0].length<2)throw Error('Traço não registrado');
   hand[4].x=.1;gesture(hand);if(activeStroke!==null)throw Error('Traço não terminou');
   const p=project({x:.2,y:.2,z:.3},640,480);if(!Number.isFinite(p.x))throw Error('Projeção inválida');
   action(2);if(strokes.length)throw Error('Limpar falhou');
   paintScene(ctx,640,480);advise();
   resetQuad();delete video.videoWidth;delete video.videoHeight;return true;
  });assert.equal(panelChecks,true);
  const result = await page.evaluate(async () => {
   const detector = new Hands({locateFile:f=>'/mediapipe/'+f});
   detector.setOptions({maxNumHands:1,modelComplexity:0,minDetectionConfidence:.6,minTrackingConfidence:.6});
   let received=false;
   detector.onResults(()=>{received=true});
   const image=document.createElement('canvas');image.width=480;image.height=360;
   image.getContext('2d').fillRect(0,0,480,360);
   await detector.send({image});
   await detector.close();
   return received;
  });
  assert.equal(result,true,'O detector deve processar uma imagem sem rede externa.');
  assert.deepEqual(external,[],'Nenhum recurso externo pode ser solicitado.');
  const manifest=fs.readFileSync('app/src/main/AndroidManifest.xml','utf8');
  assert(!manifest.includes('android.permission.INTERNET'));
  console.log('PASSOU: detector inicializado e imagem processada sem rede externa; app sem permissão INTERNET.');
 } finally {
  if(browser)await browser.close();
  await new Promise(resolve=>server.close(resolve));
 }
})().catch(error=>{console.error(error);process.exitCode=1});
