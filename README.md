# Mão câmera 1.2

[Baixar APK](https://github.com/vfeitosades-dot/mao-camera/releases/latest)

Android 8+, câmera frontal e internet para carregar o MediaPipe Hands. As imagens são processadas no dispositivo, sem gravação pelo app.

- Câmera frontal obrigatória, imagem espelhada.
- Um quadro aparece ao encontrar por três amostras uma área de baixa variação de cor. Se não aparecer, toque na imagem. Novo quadro reinicia a busca.
- Una polegar e indicador dentro do quadro para segurar e mover. Separe os dedos para soltar.
- Modo VR apresenta a mesma câmera em duas vistas. Use o celular na horizontal; Sair VR retorna à vista normal.

Limitações: a busca de superfície é uma heurística de imagem, pode confundir fundos uniformes e não reconhece planos ou profundidade. O quadro fica em coordenadas da tela e não ancorado ao mundo físico. VR é uma apresentação lado a lado monoscópica, sem rastreamento de cabeça nem estereoscopia. Não use ao caminhar.

Compilação automática pelo GitHub Actions, Gradle 8.9, JDK 17 e SDK 35. Versão de teste assinada com chave de debug; pode exigir desinstalar a anterior. Testes de lógica com câmera simulada; ainda não validado em celular físico.
