# Script para iniciar la demo completa en Windows
# Autor: Demo para alumnos

Write-Host "Iniciando Demo WebFlux vs Spring Web..." -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# Verificar si Docker/Podman esta disponible
$containerCmd = $null
if (Get-Command docker -ErrorAction SilentlyContinue) {
    $containerCmd = "docker"
    Write-Host "Docker encontrado" -ForegroundColor Green
} elseif (Get-Command podman -ErrorAction SilentlyContinue) {
    $containerCmd = "podman"
    Write-Host "Podman encontrado" -ForegroundColor Green
} else {
    Write-Host "Error: No se encontro Docker ni Podman" -ForegroundColor Red
    Write-Host "Por favor instala Docker o Podman primero" -ForegroundColor Yellow
    exit 1
}

# Verificar si Java esta instalado
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Error: Java no esta instalado" -ForegroundColor Red
    Write-Host "Por favor instala Java 21 o superior" -ForegroundColor Yellow
    exit 1
}

# Verificar version de Java
$javaVersion = (java -version 2>&1 | Select-String "version").ToString()
if ($javaVersion -match "version ""(\d+)") {
    $majorVersion = [int]$matches[1]
    if ($majorVersion -lt 21) {
        Write-Host "Error: Java 21 o superior es requerido" -ForegroundColor Red
        Write-Host "Version actual: $javaVersion" -ForegroundColor Yellow
        Write-Host "Por favor actualiza a Java 21 o superior" -ForegroundColor Yellow
        exit 1
    }
}

Write-Host "Java encontrado: $javaVersion" -ForegroundColor Green

# Verificar si Gradle esta disponible
if (-not (Test-Path "gradlew.bat")) {
    Write-Host "Error: Gradle wrapper no encontrado" -ForegroundColor Red
    Write-Host "Asegurate de estar en el directorio raiz del proyecto" -ForegroundColor Yellow
    exit 1
}

Write-Host "Gradle wrapper encontrado" -ForegroundColor Green

# Verificar si MongoDB ya está corriendo
Write-Host "Verificando estado de MongoDB..." -ForegroundColor Cyan
$mongodbRunning = $false

# Verificar si hay un contenedor de MongoDB corriendo
$mongodbContainer = & $containerCmd ps --format "table {{.Names}}" | Select-String "mongodb"
if ($mongodbContainer) {
    Write-Host "MongoDB ya está corriendo" -ForegroundColor Green
    $mongodbRunning = $true
} else {
    # Verificar si el puerto 27017 está en uso
    $portInUse = Get-NetTCPConnection -LocalPort 27017 -ErrorAction SilentlyContinue
    if ($portInUse) {
        Write-Host "Puerto 27017 ya está en uso, MongoDB probablemente está corriendo" -ForegroundColor Green
        $mongodbRunning = $true
    }
}

if (-not $mongodbRunning) {
    Write-Host "MongoDB no está corriendo, iniciando..." -ForegroundColor Yellow
    # Detener contenedores existentes si los hay
    Write-Host "Deteniendo contenedores existentes..." -ForegroundColor Yellow
    & $containerCmd-compose down 2>$null

    # Iniciar MongoDB
    Write-Host "Iniciando MongoDB..." -ForegroundColor Cyan
    & $containerCmd-compose up -d mongodb
} else {
    Write-Host "MongoDB ya está funcionando, continuando..." -ForegroundColor Green
}

# Esperar a que MongoDB este listo (solo si lo iniciamos)
if (-not $mongodbRunning) {
    Write-Host "Esperando a que MongoDB este listo..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10

    # Verificar que MongoDB este funcionando
    Write-Host "Verificando que MongoDB este funcionando..." -ForegroundColor Yellow
    $ready = $false
    for ($i = 1; $i -le 15; $i++) {
        try {
            $pingResult = & $containerCmd exec demo-mongodb mongosh --eval "db.runCommand('ping')" 2>$null
            if ($LASTEXITCODE -eq 0) {
                Write-Host "MongoDB esta funcionando correctamente" -ForegroundColor Green
                $ready = $true
                break
            }
        } catch {
            # Ignorar errores durante la espera
        }
        if ($i -eq 15) {
            Write-Host "Error: MongoDB no respondio despues de 30 segundos" -ForegroundColor Red
            Write-Host "Revisa los logs con: $containerCmd-compose logs mongodb" -ForegroundColor Yellow
            exit 1
        }
        Write-Host "Esperando... ($i/15)" -ForegroundColor Yellow
        Start-Sleep -Seconds 2
    }
} else {
    Write-Host "MongoDB ya esta funcionando, verificando conexion..." -ForegroundColor Yellow
    # Verificar conexion rapida
    try {
        $pingResult = & $containerCmd exec mongodb mongosh --eval "db.adminCommand('ping')" 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "MongoDB esta funcionando correctamente" -ForegroundColor Green
        } else {
            Write-Host "Advertencia: No se pudo verificar MongoDB, pero continuando..." -ForegroundColor Yellow
        }
    } catch {
        Write-Host "Advertencia: No se pudo verificar MongoDB, pero continuando..." -ForegroundColor Yellow
    }
}

# Inicializar la base de datos
Write-Host "Inicializando base de datos con datos de ejemplo..." -ForegroundColor Cyan
& $containerCmd exec demo-mongodb mongosh --file /docker-entrypoint-initdb.d/init.js

# Compilar la aplicacion
Write-Host "Compilando la aplicacion..." -ForegroundColor Cyan
& .\gradlew.bat clean build -x test

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error en la compilacion" -ForegroundColor Red
    exit 1
}

Write-Host "Aplicacion compilada exitosamente" -ForegroundColor Green

# Iniciar la aplicacion
Write-Host "Iniciando la aplicacion Spring Boot..." -ForegroundColor Cyan
Write-Host "La aplicacion estara disponible en: http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "Endpoints disponibles:" -ForegroundColor White
Write-Host "  • WebFlux (Reactivo): http://localhost:8080/api/v1/reactive/expenses" -ForegroundColor White
Write-Host "  • Spring Web (Tradicional): http://localhost:8080/api/v1/traditional/expenses" -ForegroundColor White
Write-Host "  • Comparacion: http://localhost:8080/api/v1/comparison/performance-test" -ForegroundColor White
Write-Host "  • MongoDB Express: http://localhost:8081 (admin/password123)" -ForegroundColor White
Write-Host ""
Write-Host "Para ejecutar pruebas de rendimiento:" -ForegroundColor White
Write-Host "  curl http://localhost:8080/api/v1/comparison/performance-test" -ForegroundColor Cyan
Write-Host "  curl http://localhost:8080/api/v1/comparison/stress-test" -ForegroundColor Cyan
Write-Host ""
Write-Host "Para detener la aplicacion: Ctrl+C" -ForegroundColor Yellow
Write-Host "Para limpiar contenedores: $containerCmd-compose down" -ForegroundColor Yellow

# Ejecutar la aplicacion
& .\gradlew.bat bootRun

