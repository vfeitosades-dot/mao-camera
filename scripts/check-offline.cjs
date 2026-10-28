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
  const cubeChecks=await page.evaluate(()=>{
   place(.5,.5);
   const mesh=cubeMesh(640,480);
   if(mesh.length!==8||!mesh.every(p=>Number.isFinite(p.x)&&Number.isFinite(p.y)))throw Error('Projeção inválida');
   if(!hitCube(.5,.5,640,480)||hitCube(.01,.01,640,480))throw Error('Área de interação incorreta');
   Object.defineProperty(video,'videoWidth',{value:640,configurable:true});Object.defineProperty(video,'videoHeight',{value:480,configurable:true});
   const hand=Array.from({length:21},()=>({x:.5,y:.55}));
   hand[0]={x:.5,y:.85};hand[9]={x:.5,y:.45};hand[5]={x:.35,y:.55};hand[17]={x:.65,y:.55};hand[4]={x:.49,y:.5};hand[8]={x:.51,y:.5};
   if(classifyGesture(hand)!=='pinça'||classifyGesture(null)!=='ausente')throw Error('Classificação de gesto incorreta');
   gesture(hand);if(!grabbed)throw Error('Pinça não pegou cubo');
   hand[0].y=.95;hand[9].y=.35;hand[4].x=.59;hand[8].x=.61;hand[5].y=.65;
   gesture(hand);
   if(quad.scale<=1||Math.abs(quad.rz)<.01||Math.abs(quad.x-.6)>.001)throw Error('Transformação 3D não acompanhou mão');
   hand[4].x=.2;hand[8].x=.9;gesture(hand);if(grabbed)throw Error('Cubo não soltou');
   advise(null);if(!assistant.textContent.includes('Mostre a mão'))throw Error('Assistente não orientou usuário');
   resetQuad();delete video.videoWidth;delete video.videoHeight;
   return true;
  });
  assert.equal(cubeChecks,true);
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
