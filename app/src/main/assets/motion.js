(function(global){'use strict';
const median=a=>a.slice().sort((x,y)=>x-y)[Math.floor(a.length/2)]||0;
class VisualTracker {
 constructor(){this.previous=null;this.time=0;this.state={status:'UNAVAILABLE',method:'AVME_PATCH_FLOW',confidence:0,features:0,ageMs:null,reason:'Ative a câmera'};}
 reset(){this.previous=null;this.time=0;}
 process(gray,w,h,masks=[],now=performance.now()){
  const old=this.previous;this.previous=new Uint8Array(gray);const dt=now-this.time;this.time=now;
  let mean=0;for(const n of gray)mean+=n;mean/=gray.length;
  const fail=reason=>Object.assign(this.state,{status:'LOST',confidence:0,features:0,ageMs:0,reason,dx:0,dy:0});
  if(mean<16)return fail('Pouca luz ou câmera obstruída');
  if(!old||old.length!==gray.length||dt>500)return Object.assign(this.state,{status:'RECOVERING',confidence:0,features:0,ageMs:0,reason:'Coletando referência',dx:0,dy:0});
  const moves=[];
  for(let y=9;y<h-9;y+=8)for(let x=9;x<w-9;x+=8){
   if(masks.some(b=>x/w>b.x0&&x/w<b.x1&&y/h>b.y0&&y/h<b.y1))continue;
   const i=y*w+x,gradient=Math.abs(old[i+1]-old[i-1])+Math.abs(old[i+w]-old[i-w]);if(gradient<35)continue;
   let best=Infinity,second=Infinity,bx=0,by=0;
   for(let dy=-4;dy<=4;dy++)for(let dx=-4;dx<=4;dx++){
    let score=0;for(let py=-2;py<=2;py++)for(let px=-2;px<=2;px++)score+=Math.abs(old[(y+py)*w+x+px]-gray[(y+py+dy)*w+x+px+dx]);score/=25;
    if(score<best){second=best;best=score;bx=dx;by=dy;}else if(score<second)second=score;
   }
   if(best<24&&best<second*.92)moves.push({dx:bx,dy:by});
  }
  if(moves.length<8)return fail('Poucas características visuais confiáveis');
  const dx=median(moves.map(p=>p.dx)),dy=median(moves.map(p=>p.dy)),inliers=moves.filter(p=>Math.hypot(p.dx-dx,p.dy-dy)<=1.5);
  if(inliers.length<8||inliers.length/moves.length<.6)return fail('Movimento incoerente ou objetos em movimento');
  return Object.assign(this.state,{status:'LIMITED',method:'AVME_PATCH_FLOW',features:inliers.length,confidence:Math.min(.75,inliers.length/moves.length*.7),ageMs:0,reason:'Orientação relativa aproximada; sem posição ou escala métrica',dx,dy});
 }
}
global.PocketMotion={VisualTracker,median};
})(window);
