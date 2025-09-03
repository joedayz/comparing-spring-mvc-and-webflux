#!/bin/bash

# Script de limpieza para la demo de WebFlux vs Spring Web
# Autor: Demo para alumnos

echo "🧹 Limpiando Demo WebFlux vs Spring Web"
echo "========================================"

# Verificar si Docker/Podman está instalado
if ! command -v podman &> /dev/null; then
    if ! command -v docker &> /dev/null; then
        echo "❌ Error: No se encontró Podman ni Docker instalado"
        exit 1
    else
        DOCKER_CMD="docker"
    fi
else
    DOCKER_CMD="podman"
fi

# Detener y eliminar contenedores
echo "🛑 Deteniendo contenedores..."
$DOCKER_CMD-compose down

# Eliminar volúmenes
echo "🗑️  Eliminando volúmenes..."
$DOCKER_CMD volume rm demo-webflux-mongodb_mongodb_data 2>/dev/null || true

# Eliminar redes
echo "🌐 Eliminando redes..."
$DOCKER_CMD network rm demo-webflux-mongodb_demo-network 2>/dev/null || true

# Limpiar build de Gradle
echo "🧹 Limpiando build de Gradle..."
./gradlew clean

# Eliminar archivos temporales
echo "🗂️  Eliminando archivos temporales..."
rm -rf build/
rm -rf .gradle/

echo "✅ Limpieza completada"
echo "Para volver a ejecutar la demo, usa: ./start-demo.sh"
