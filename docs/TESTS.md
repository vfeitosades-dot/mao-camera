# Verificação

O workflow executa `scripts/check-offline.cjs`, salva `test-results.json`, prévias mono/móvel/estéreo e compila o APK. O relatório é gerado pela execução real. Modelo MediaPipe recebe imagem sintética sem acesso externo; isso verifica assets e inferência, não detecção de mãos reais.

Testes automáticos: inicialização WebGL, disparidade/matrizes por olho, AVME com deslocamento sintético e imagem bloqueada, preferências persistentes, ambiente, pintura, blocos, modelo offline e ausência de exceções JavaScript. Compilação Java/Android é separada.

## Verificação obrigatória em aparelho — não executada aqui
1. Instalar e abrir; negar câmera deve manter o mundo operável.
2. Consultar diagnóstico. Comparar sensores listados com Android, sem assumir giroscópio.
3. Ativar câmera traseira; abrir mãos e pinçar. Conferir desligamento em pausa.
4. Testar AVME em ambiente texturizado, iluminado e parado; mover celular devagar. Cobrir câmera deve gerar LOST e parar a atualização visual.
5. Comparar orientation sensor/visual/manual, medir deriva e testar eixos em portrait/landscape.
6. Inserir em VR Box com câmera desobstruída, ajustar IPD/FOV/distorção e verificar conforto sentado. Sem giroscópio/magnetômetro, controles alternativos devem funcionar.
7. Abrir navegador e URL real. Teclado interno/Android e retorno ao mundo. Navegador continua 2D.
8. Abrir vídeo/foto pelo SAF, pausar/reproduzir, verificar codecs e liberação.
9. Pintar, salvar, reiniciar, carregar; repetir com blocos.
10. Medir FPS, duração de inferência, energia e térmica durante uso real; testar redução de qualidade.

## Resultados do documento original
Testes 1–18 que dependem de Android, hardware, movimento físico, mãos reais, lentes e navegação real continuam pendentes. Resultados sintéticos do CI não os substituem. Não marcar a plataforma completa como validada antes dessa bateria.
