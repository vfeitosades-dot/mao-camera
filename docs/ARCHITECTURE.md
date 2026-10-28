# Horizon Pocket VR — reconstrução 3.0

## Escolha técnica
Android Java hospeda WebGL/Three.js 0.160.0. Não é uma grade HTML duplicada: o mundo, a geometria, oclusão, raycasts e janelas são objetos WebGL 3D. StereoCamera gera duas matrizes de vista e projeção; cada olho vai para um render target próprio. Um shader aplica distorção radial configurável. A opção evita trocar o pipeline MediaPipe existente, não exige licença/editor Unity e permite compilar em GitHub Actions. Não existe projeto Unity nesta entrega; sua inclusão e CameraX exigem uma migração específica, ainda não feita. Não foram usados OpenXR, SDK Meta ou o head tracker Cardboard, que depende de acelerômetro e giroscópio.

## Contratos implementados
- `hands.js / HandPipeline`: start(maxNumHands), stop(), callback de resultado real MediaPipe, inferenceMs, masks. Um stream traseiro; captura 480x360 preferida / máximo 640x480, 24 FPS máximo, inferência no máximo 20 Hz, modelo lite, confiança .6. Preservados descarte de quadros repetidos, serialização, cancelamento por geração e pausa. Não foi trocado o modelo. A execução continua em WebView; migração da inferência para worker/nativo não foi implementada.
- `motion.js / VisualTracker`: process(gray,w,h,masks,timestamp), reset(), state. Busca de patches 5x5 em grade, deslocamento ±4 pixels, melhor/segundo erro, consenso robusto por mediana. Exclui caixas das mãos. Retorna dx/dy, features, confidence, status e motivo. Não usa OpenCV, LK, RANSAC ou SLAM. Teste sintético não prova pose física.
- `DeviceBridge.java`: diagnostics(), pose(). Sensores consultados no Android, não presumidos. Rotation vector, geomagnetic rotation vector, fallback accel+mag. Matriz remapeada pela orientação da tela; timestamp e acurácia. Registra ao retomar e cancela ao pausar.
- `app.js / VRCore`: cena em metros virtuais, câmeras, render targets, shader radial, resize, IPD, FOV e qualidade. Quaternions internos, recenter e suspensão.
- WorldManager: casa com móveis/plantas/janelas, terraço, observatório e cenário mínimo, geometria procedural original. Sem lightmaps, reflection probes ou AO.
- SpatialInteraction: raycast contra os painéis/objetos, hover, pinça com .32/.5 normalizada pela palma, arrastar barra, fechar/minimizar, seleção central por botão/permanência e controle por toque/teclado/gamepad. Posição da mão não é métrica.
- AppLibrary: biblioteca espacial e dock. Navegador Android, mídia local 2D em textura, pintura 3D, blocos, física simples, ambientes, diagnósticos, comandos locais e ajustes. Não instala/executa aplicativos Quest ou Android dentro do mundo.
- BrowserWindow.java: navegador WebView real e teclado nativo preservados, **janela 2D**. Não é textura VR nem navegação por raycast na página. Nenhuma ponte nativa é exposta às páginas remotas.
- MediaManager: seletor Storage Access Framework, URI escolhida pelo usuário, vídeo 2D como VideoTexture e foto como textura. Sem acesso amplo ao armazenamento. Não copia o arquivo para servidores.
- Settings: preferências/projetos em localStorage, limpar explicitamente. Nenhuma gravação automática de vídeo.

## Orientação
Automático prefere sensor válido com idade <500ms e acurácia não nula. Sem orientação utilizável, tenta AVME se câmera ativa; mantém a última orientação quando o visual falha. Sem câmera/sensor existe toque, teclado e gamepad. O modo manual não simula sensores físicos. O AVME estima somente yaw/pitch relativo pelo deslocamento aparente; translação de câmera pode ser confundida com rotação. Deriva, objetos móveis, textura repetida e câmera bloqueada podem invalidá-lo. Confiança visual é uma heurística limitada a .75, não probabilidade calibrada. Estado visual válido é LIMITED, nunca 6DoF. Sensor e câmera são selecionados, não fundidos por um filtro de estado avançado. Fusão IMU+visual robusta, roll visual, marcadores e posição absoluta estão pendentes.

## Renderização
Duas vistas de uma cena compartilhada, IPD 45–80mm, FOV 45–100°, distância de painel 1–2.8m, shader radial -0.30 a +0.40. A distorção é ajuste manual experimental, sem perfil medido de lentes. O vídeo passthrough é monocular; duas vistas não criam uma segunda câmera física. Os ambientes e objetos virtuais têm disparidade binocular própria. Browser, menus Android de arquivos e ajustes DOM continuam superfícies 2D de recuperação.

## Desempenho e privacidade
Nenhuma fila de inferência. Grades 96x72 para AVME a até 10Hz, desenho e render em requestAnimationFrame. Render ratios .65/1/até1.5; status térmico real Android >=SEVERE reduz para econômico. FPS e duração da inferência medidos, não metas anunciadas como resultados. Artefatos de teste não registram imagens de câmera. Diagnóstico informa temperatura da bateria, que não é temperatura da GPU/CPU. O microfone não é solicitado. Câmera começa apenas pelo botão e para ao pausar/sair; câmera negada não impede o mundo 3D.
