#!/bin/bash

# Script para iniciar la demo de WebFlux vs Spring Web
# Autor: Demo para alumnos
# Fecha: $(date)

echo "🚀 Iniciando Demo WebFlux vs Spring Web"
echo "========================================"

# Verificar si Docker/Podman está instalado
if ! command -v podman &> /dev/null; then
    if ! command -v docker &> /dev/null; then
        echo "❌ Error: No se encontró Podman ni Docker instalado"
        echo "Por favor instala Podman o Docker para continuar"
        exit 1
    else
        echo "🐳 Usando Docker"
        DOCKER_CMD="docker"
    fi
else
    echo "🟢 Usando Podman"
    DOCKER_CMD="podman"
fi

# Verificar si Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java no está instalado"
    echo "Por favor instala Java 21 o superior"
    exit 1
fi

# Verificar versión de Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "❌ Error: Java 21 o superior es requerido"
    echo "Versión actual: $(java -version 2>&1 | head -n 1)"
    echo "Por favor actualiza a Java 21 o superior"
    exit 1
fi

echo "✅ Java encontrado: $(java -version 2>&1 | head -n 1)"

# Verificar si Gradle está disponible
if ! command -v ./gradlew &> /dev/null; then
    echo "❌ Error: Gradle wrapper no encontrado"
    echo "Asegúrate de estar en el directorio raíz del proyecto"
    exit 1
fi

echo "✅ Gradle wrapper encontrado"

# Detener contenedores existentes si los hay
echo "🛑 Deteniendo contenedores existentes..."
$DOCKER_CMD-compose down 2>/dev/null || true

# Iniciar MongoDB
echo "🗄️  Iniciando MongoDB..."
$DOCKER_CMD-compose up -d mongodb

# Esperar a que MongoDB esté listo
echo "⏳ Esperando a que MongoDB esté listo..."
sleep 10

# Verificar que MongoDB esté funcionando
echo "⏳ Verificando que MongoDB esté funcionando..."
for i in {1..15}; do
    if $DOCKER_CMD exec demo-mongodb mongosh --eval "db.runCommand('ping')" > /dev/null 2>&1; then
        echo "✅ MongoDB está funcionando correctamente"
        break
    fi
    if [ $i -eq 15 ]; then
        echo "❌ Error: MongoDB no está respondiendo después de 30 segundos"
        echo "Revisa los logs con: $DOCKER_CMD-compose logs mongodb"
        exit 1
    fi
    echo "⏳ Esperando... ($i/15)"
    sleep 2
done

# Inicializar la base de datos
echo "📊 Inicializando base de datos con datos de ejemplo..."
$DOCKER_CMD exec demo-mongodb mongosh --file /docker-entrypoint-initdb.d/init.js

# Compilar la aplicación
echo "🔨 Compilando la aplicación..."
./gradlew clean build -x test

if [ $? -ne 0 ]; then
    echo "❌ Error en la compilación"
    exit 1
fi

echo "✅ Aplicación compilada exitosamente"

# Iniciar la aplicación
echo "🚀 Iniciando la aplicación Spring Boot..."
echo "La aplicación estará disponible en: http://localhost:8080"
echo ""
echo "📋 Endpoints disponibles:"
echo "  • WebFlux (Reactivo): http://localhost:8080/api/v1/reactive/expenses"
echo "  • Spring Web (Tradicional): http://localhost:8080/api/v1/traditional/expenses"
echo "  • Comparación: http://localhost:8080/api/v1/comparison/performance-test"
echo "  • MongoDB Express: http://localhost:8081 (admin/password123)"
echo ""
echo "🔄 Para ejecutar pruebas de rendimiento:"
echo "  curl http://localhost:8080/api/v1/comparison/performance-test"
echo "  curl http://localhost:8080/api/v1/comparison/stress-test"
echo ""
echo "⏹️  Para detener la aplicación: Ctrl+C"
echo "🗑️  Para limpiar contenedores: $DOCKER_CMD-compose down"

# Ejecutar la aplicación
./gradlew bootRun
