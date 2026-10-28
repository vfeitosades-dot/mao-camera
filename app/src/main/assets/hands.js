// Captura e inferência preservam os limites do sistema de mãos anterior.
(function(global){'use strict';
class HandPipeline{
 constructor(video,onResults,onStatus){this.video=video;this.onResults=onResults;this.onStatus=onStatus;this.running=false;this.epoch=0;this.pending=Promise.resolve();this.callback=0;this.lastVideoTime=-1;this.lastInference=-Infinity;this.interval=1000/20;this.masks=[];this.inferenceMs=0;}
 async start(maxNumHands=1){this.stop();const token=++this.epoch;try{
  await this.pending.catch(()=>{});if(token!==this.epoch)return;
  if(!this.model){this.model=new Hands({locateFile:f=>'/mediapipe/'+f});this.model.onResults(r=>{if(this.running){const now=performance.now();this.inferenceMs=now-this.sent;this.onResults(r);}});}
  await this.model.setOptions({maxNumHands,modelComplexity:0,minDetectionConfidence:.6,minTrackingConfidence:.6});if(token!==this.epoch)return;
  const acquired=await navigator.mediaDevices.getUserMedia({audio:false,video:{facingMode:{exact:'environment'},width:{ideal:480,max:640},height:{ideal:360,max:480},frameRate:{ideal:24,max:24}}});
  if(token!==this.epoch||document.hidden){acquired.getTracks().forEach(t=>t.stop());return;}
  this.stream=acquired;this.video.srcObject=acquired;await this.video.play();if(token!==this.epoch)return;
  this.running=true;this.lastVideoTime=-1;this.lastInference=-Infinity;this.onStatus('Câmera ativa · processamento local');this.schedule(token);
 }catch(e){if(token===this.epoch){this.stop();this.onStatus('Câmera/IA indisponível: '+e.name+'. Use toque ou cursor.');}}}
 schedule(token){if(!this.running||token!==this.epoch)return;const next=now=>this.frame(now,token);this.callback=this.video.requestVideoFrameCallback?this.video.requestVideoFrameCallback(next):requestAnimationFrame(next);}
 async frame(now,token){if(!this.running||token!==this.epoch)return;
 if(this.video.readyState<2||this.video.currentTime===this.lastVideoTime||now-this.lastInference<this.interval){this.schedule(token);return;}
 this.lastVideoTime=this.video.currentTime;this.lastInference=now;try{this.sent=performance.now();this.pending=this.model.send({image:this.video});await this.pending;this.schedule(token);}catch(e){if(token===this.epoch){this.stop();this.onStatus('Falha no detector. Reinicie a câmera; toque continua disponível.');}}}
 stop(){++this.epoch;this.running=false;if(this.video.cancelVideoFrameCallback)this.video.cancelVideoFrameCallback(this.callback);else cancelAnimationFrame(this.callback);if(this.stream){this.stream.getTracks().forEach(t=>t.stop());this.stream=null;}this.video.srcObject=null;}
}
global.PocketHands={HandPipeline};
})(window);
