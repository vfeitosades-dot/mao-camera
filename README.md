# Orbit 2.0 — interface espacial, navegador interno e olhar experimental

[Baixar APK](https://github.com/vfeitosades-dot/mao-camera/releases/latest)

Interface redesenhada com vidro escuro, janelas arredondadas, cartões e barra flutuante. Referências visuais consultadas: https://www.uploadvr.com/quest-3-v85-ptc-can-turn-any-surface-into-a-virtual-keyboard/ e https://www.techmeme.com/240625/p20 . Design próprio inspirado em interfaces VR; não é Meta Quest.

Câmera traseira para mãos e desenho. Abra Navegador para usar sites dentro do app; botão ⌨ abre teclado flutuante com letras, números, símbolos, Shift, apagar e espaço. Digita no endereço ou no campo da página selecionado. Internet é necessária para sites; o navegador não recebe ponte JavaScript nativa e nega permissões de câmera das páginas. Algumas páginas podem exigir teclado do sistema, recursos ou autenticação não suportados pelo WebView.

Modo Olhar alterna para câmera frontal. Toque Ativar se necessário, depois Calibrar olhar: olhe para cada ponto mantendo a cabeça imóvel enquanto coleta. Faça cinco capturas. Estimativa por posição da íris, calibração pessoal e suavização; rejeita olhos fechados, ausência de rosto e deslocamentos da cabeça. O cursor apenas indica direção e não clica automaticamente. Não é eye tracking de precisão e não foi validado em usuários. Não funciona se a câmera não puder ver os olhos (por exemplo, celular em visor que cobre o rosto). Câmera traseira e frontal não são usadas simultaneamente.

O modelo MediaPipe FaceMesh detecta face e íris; a estimativa de olhar é uma camada experimental deste app. Detectar íris por si só não determina o alvo do olhar: https://research.google/blog/mediapipe-iris-real-time-iris-tracking-depth-estimation/ . Não foi treinado um novo modelo de gaze nem medida acurácia. A calibração vale só enquanto posição e iluminação forem mantidas. Dados não são enviados pelo detector.

Modelos de mãos e rosto incluídos no APK, offline desde o primeiro uso. VR continua monoscópico; navegador é uma janela Android única, não duas páginas estereoscópicas. Desenho usa profundidade aproximada pelo tamanho da mão, sem ancoragem no mundo.

Android 8+ com WebView atualizado. CI verifica modelos em Chrome com rede externa bloqueada, cálculos de calibração, interação, desenho e compilação Android. Não substitui testes no aparelho. Chave debug pode exigir desinstalar a versão anterior.
