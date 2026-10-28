# Estado da entrega e fases ainda necessárias

Esta é uma base funcional reconstruída, não a conclusão das seis fases do documento. Não se afirma equivalência ao Meta Horizon OS.

| Área | Implementação nesta versão | Pendente |
|---|---|---|
| Renderizador | Mundo WebGL 3D, dois olhos reais, shader radial, IPD/FOV | Validar lente/FOV/IPD em VR Box; perfis Cardboard; aberração cromática |
| Orientação | Sensores Android disponíveis, fallback geomagnético/accel+mag, quaternions, toque | Validar eixos em aparelhos e orientações; estimador de fusão robusto |
| AVME | Patch flow compartilhado com câmera das mãos, máscaras, consenso, LOST/LIMITED | Rotação geométrica calibrada, RANSAC/OpenCV, teste físico, marcadores, SLAM |
| Mãos | Mesmo modelo/configuração otimizada, esqueleto e articulações esféricas | Modelos de mãos com pele/mesh articulada; gestos avançados e duas mãos para transformar |
| Janelas | Geometria, raycast, hover, arrastar barra, fechar/minimizar | Redimensionar com alças, pin/foco avançado, layouts em semicírculo, restauração de minimizadas |
| Biblioteca | Nove entradas funcionais | Favoritos, busca, filtros, pastas e módulos instaláveis |
| Navegador | WebView Android interno, teclado e navegação | Renderizar/interagir com WebView em textura VR; abas/favoritos espaciais |
| Mídia | SAF, foto/vídeo local 2D em tela 3D, play/pause/fechar | 180/360, SBS/top-bottom, controles timeline e validação de codecs |
| Experiências | Pintura com espessura/cor, salvar/carregar/desfazer; blocos e física simples | Cerâmica, alavancas, física completa/colisões robustas, seleção avançada |
| Ambientes | Quatro cenários 3D originais procedurais | Polimento artístico, lightmaps, reflexos, LOD e áudio espacial |
| Assistente | Comandos digitados locais | Voz offline quando disponível; conversa opcional com autorização |
| Diagnóstico | Lista sensores, câmeras, FPS ranges, bateria, térmica, frame rate e inferência | Benchmark guiado, resolução/câmeras simultâneas testadas, exportação diagnóstica |
| Compatibilidade | Recuperação 2D sem câmera/sensor, pausa, recenter, gamepad | Varredura de aparelhos, acessibilidade e tutorial completo |
| Arquitetura | Java + WebGL/Three.js e MediaPipe existentes | Unity/CameraX/C++ não incluídos; avaliar migração após medições |

Não há métricas medidas de 6DoF, latência física, precisão de olhar ou rastreamento em aparelho. O sistema de olhar da versão anterior foi removido, conforme pedido de conservar somente o subsistema de mãos e reconstruir o restante.
