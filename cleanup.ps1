# Script de limpieza para la demo de WebFlux vs Spring Web
# Autor: Demo para alumnos

Write-Host "🧹 Limpiando Demo WebFlux vs Spring Web" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow

# Verificar si Docker/Podman está instalado
$dockerCmd = $null
if (Get-Command podman -ErrorAction SilentlyContinue) {
    $dockerCmd = "podman"
} elseif (Get-Command docker -ErrorAction SilentlyContinue) {
    $dockerCmd = "docker"
} else {
    Write-Host "❌ Error: No se encontró Podman ni Docker instalado" -ForegroundColor Red
    exit 1
}

# Detener y eliminar contenedores
Write-Host "🛑 Deteniendo contenedores..." -ForegroundColor Yellow
& $dockerCmd-compose down

# Eliminar volúmenes
Write-Host "🗑️  Eliminando volúmenes..." -ForegroundColor Yellow
& $dockerCmd volume rm demo-webflux-mongodb_mongodb_data 2>$null

# Eliminar redes
Write-Host "🌐 Eliminando redes..." -ForegroundColor Yellow
& $dockerCmd network rm demo-webflux-mongodb_demo-network 2>$null

# Limpiar build de Gradle
Write-Host "🧹 Limpiando build de Gradle..." -ForegroundColor Yellow
& .\gradlew.bat clean

# Eliminar archivos temporales
Write-Host "🗂️  Eliminando archivos temporales..." -ForegroundColor Yellow
if (Test-Path "build") { Remove-Item -Recurse -Force "build" }
if (Test-Path ".gradle") { Remove-Item -Recurse -Force ".gradle" }

Write-Host "✅ Limpieza completada" -ForegroundColor Green
Write-Host "Para volver a ejecutar la demo, usa: .\start-demo.ps1" -ForegroundColor Cyan
