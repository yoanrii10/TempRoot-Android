# TEMP ROOT — Android

Starter de Android Studio para Android 10+ (API 29+).

## Estado
Esta primera versión implementa diagnóstico local sin ROOT:
- versión Android y API
- fabricante y modelo
- versión del kernel
- ABI soportadas
- lectura de estado SELinux cuando el sistema lo permite
- comprobación básica de rastros de `su` (no concluyente)

## Importante sobre ROOT temporal
Una aplicación ordinaria no puede concederse privilegios ROOT por sí misma.
Tampoco puede garantizar que el ROOT global desaparezca tras reiniciar: eso depende
del método de adquisición, del gestor de privilegios, del kernel y de la cadena de arranque.
Este proyecto no contiene exploits ni modifica particiones de arranque.

## Abrir
1. Descomprime el proyecto.
2. Abre la carpeta `TempRoot-Android` en Android Studio.
3. Espera la sincronización de Gradle.
4. Ejecuta en un dispositivo Android 10 o posterior.

## Próximas etapas
1. Matriz de compatibilidad por dispositivo/kernel.
2. Diagnóstico más fiable de bootloader y Verified Boot mediante interfaces permitidas.
3. Integración opcional con un proveedor de privilegios autorizado ya presente en el dispositivo.
4. Pruebas automatizadas en Android 10–15+.
