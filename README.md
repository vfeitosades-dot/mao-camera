# Mão câmera

App Android simples que usa a câmera frontal e desenha 21 pontos e as conexões da mão em tempo real, com MediaPipe Hands. Detecta até duas mãos.

## Baixar

Abra [Releases](https://github.com/vfeitosades-dot/mao-camera/releases/latest) e baixe `mao-camera.apk`. Autorize a instalação e permita o uso da câmera. Android 8 ou superior, Android System WebView atualizado e internet para carregar o detector. As imagens são processadas no dispositivo; não são gravadas nem enviadas a um servidor pelo app.

## Compilar

O GitHub Actions compila e publica o APK automaticamente após um push. Também é possível abrir no Android Studio com JDK 17 ou executar `gradle assembleDebug` com Gradle 8.9 e SDK Android 35.

Versão de teste assinada com chave de debug. Ainda não validada em celular físico. Atualizações geradas em execuções diferentes podem exigir desinstalar a versão anterior.
