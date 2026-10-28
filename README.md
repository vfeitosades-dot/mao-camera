# Mão câmera 1.4 — cubo 3D e assistente offline

[Baixar APK](https://github.com/vfeitosades-dot/mao-camera/releases/latest)

Android 8+, câmera frontal e detector MediaPipe Hands incluído no APK, sem necessidade de internet inclusive no primeiro uso. As imagens são processadas no dispositivo, sem gravação pelo app.

- Câmera frontal obrigatória, imagem espelhada.
- Um cubo aparece ao encontrar por três amostras uma área de baixa variação de cor. Se não aparecer, toque na imagem. Novo cubo reinicia a busca.
- Una polegar e indicador dentro do cubo para segurar e mover. Separe os dedos para soltar.
- Modo VR apresenta a mesma câmera em duas vistas. Use o celular na horizontal; Sair VR retorna à vista normal.

Limitações: a busca de superfície é uma heurística de imagem, pode confundir fundos uniformes e não reconhece planos ou profundidade. O cubo fica em coordenadas da tela e não ancorado ao mundo físico. VR é uma apresentação lado a lado monoscópica, sem rastreamento de cabeça nem estereoscopia. Não use ao caminhar.

Compilação automática pelo GitHub Actions, Gradle 8.9, JDK 17 e SDK 35. Versão de teste assinada com chave de debug; pode exigir desinstalar a anterior. Testes de lógica com câmera simulada; ainda não validado em celular físico.


O APK inclui JavaScript, WebAssembly, dados e modelos do pacote @mediapipe/hands 0.4.1675469240. O workflow testa o detector no Chrome bloqueando recursos externos antes de compilar. O app não solicita permissão INTERNET. Para instalar esta atualização pode ser necessário desinstalar a anterior.


O cubo usa geometria 3D com projeção em perspectiva e faces sombreadas. Segure com pinça, gire o pulso e aproxime a mão para mudar rotação e escala. A escala é uma estimativa pelo tamanho da mão na imagem, não uma medida física de profundidade. O assistente combina o detector neural MediaPipe com regras locais para orientar pinça, mão aberta/fechada e ausência de mão. Não é um chatbot generativo. Continua offline, inclusive no primeiro uso. VR mantém duas vistas monoscópicas.
