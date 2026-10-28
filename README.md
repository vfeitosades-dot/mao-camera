# HORIZON POCKET VR — reconstrução 3.0

[Baixar APK publicado](https://github.com/vfeitosades-dot/mao-camera/releases/latest)

Base reconstruída do zero, preservando o detector de mãos otimizado da versão anterior. Mundo WebGL 3D, duas câmeras estereoscópicas com IPD/FOV/distorção manual, ambientes originais, raycast/pinça, orientação por sensores disponíveis, AVME experimental por imagem e controles alternativos sem giroscópio. Biblioteca com pintura, blocos/física simples, mídias locais, comandos locais, configurações e navegador Android 2D com teclado.

**Entrega por etapas.** Não é uma implementação completa de Meta Quest/Horizon OS. Consulte [estado dos módulos](docs/STATUS.md), [arquitetura](docs/ARCHITECTURE.md) e [testes](docs/TESTS.md). Unity, CameraX, SLAM, fusão avançada, marcadores, navegador em textura VR, cerâmica e vídeos imersivos não estão implementados.

## Usar
- Inicie sem câmera. Arraste fora dos painéis para olhar; toque nos cartões para abrir apps. Biblioteca e dock são objetos da cena 3D.
- Ativar mãos usa a câmera traseira desobstruída. Mesmo MediaPipe lite, .6 de confiança, 24 FPS de captura máxima e inferência até 20Hz. Uma mão como padrão; opção de duas. Faça pinça para selecionar e segure a barra para mover janela. Não fornece posição métrica.
- Ajustes permite sensor/visual/manual, quatro ambientes, qualidade, IPD, FOV e distorção. Automático consulta sensores reais no Android; sem medições confiáveis, a última orientação é mantida. Recentrar está sempre disponível.
- Entrar VR renderiza os olhos separadamente. A distorção não está calibrada para um modelo específico de lentes. Use sentado ou parado; passthrough monocular não detecta obstáculos com segurança.
- Pintura e blocos possuem salvar/carregar locais. Foto/vídeo é escolhido com Storage Access Framework. Navegador abre dentro do app, mas numa janela Android 2D, não num navegador VR espacial.
- Sair de VR: botão no topo ou Escape. Pausa desliga câmera. Enter/espaço seleciona o cursor central, R recentra; setas e gamepad permitem olhar.

## Offline e privacidade
Modelos, Three.js e recursos são incluídos na compilação do APK. O mundo, mãos e aplicativos locais não dependem de internet; sites dependem. Não há captura de microfone nem envio de imagens pelos detectores. Os arquivos escolhidos ficam no aparelho. Nada é enviado automaticamente a uma IA externa.

## Compilar
JDK 17, Gradle 8.9, Android SDK 35. `gradle assembleDebug`. O workflow baixa pacotes fixos Three.js 0.160.0 e MediaPipe Hands 0.4.1675469240 para `assets/`, executa os testes e publica o APK debug. Preserve as licenças incluídas nos pacotes. Para build manual, execute os passos de assets do workflow antes de compilar. Chave de debug pode exigir desinstalar uma versão anterior.

## Referências técnicas
- [Android: sensores de posição](https://developer.android.com/develop/sensors-and-location/sensors/sensors_position)
- [Three.js StereoCamera](https://threejs.org/docs/pages/StereoCamera.html)
- [Cardboard head tracker](https://developers.google.com/cardboard/reference/c/group/head-tracker): não usado por exigir sensores que podem faltar.
- [Meta: componentes de UI](https://developers.meta.com/vr/design/components/): referência, não recursos oficiais incorporados.

Os resultados de CI estão nos artifacts da execução. Nenhum teste em celular/VR Box físico foi executado neste ambiente.
