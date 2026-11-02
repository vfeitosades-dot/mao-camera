# Hand Tracking VR 13 — APK Android
Esta versão Android conserva o conteúdo do arquivo `Hand Tracking VR (13).html`, com caminhos para os recursos MediaPipe instalados no APK.
O projeto é independente da versão antiga e está em `hand-tracking-v13/`.

## Funções
- Câmera traseira com rastreamento de até duas mãos (MediaPipe Tasks, fallback Hands).
- Visor VR Box em duas metades, correção radial simples por WebGL e orientação por imagem/gravid​ade.
- Painéis flutuantes, dock, biblioteca, galeria, minijogo Bolhas e navegação via iframe.
- Bundles, WASM e modelo de mãos incluídos no APK, sem baixar detector a cada execução. Sites externos exigem Internet.

## Instalação
Vá a GitHub Releases e baixe **HandTrackingVR-13.apk** da release **Hand Tracking VR 13 - APK**.
Permita a câmera ao iniciar. Coloque o celular horizontalmente, toque **VR**, e para sair use toque duplo.
A captura da câmera não é vídeo estéreo real; ela é repetida para os dois olhos. Não substitui rastreamento 6DoF.
Teste sentado e com a câmera desobstruída.

## Compilação
O workflow `.github/workflows/hand-tracking-v13-apk.yml` baixa os recursos com versões fixadas e compila usando JDK17, Gradle 8.9, Android SDK 35 e AGP 8.6.1.
O APK de debug é assinado automaticamente e não foi testado em dispositivo físico aqui.

## Licenças
MediaPipe é distribuído sob Apache 2.0. Mantenha os avisos e licenças de cada pacote no trabalho derivado.
