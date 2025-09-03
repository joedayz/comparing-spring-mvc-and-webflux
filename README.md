# 🚀 Demo WebFlux vs Spring Web - Sistema de Gestión de Gastos

Esta demo está diseñada para mostrar las diferencias de rendimiento y concurrencia entre **Spring WebFlux (Reactivo)** y **Spring Web (Tradicional)** en un escenario real de gestión de gastos.

## 🎯 Objetivos de la Demo

- **Comparar rendimiento** entre WebFlux y Spring Web tradicional
- **Demostrar manejo de concurrencia** en ambos enfoques
- **Mostrar lógica de negocio compleja** con diferentes métodos de pago
- **Visualizar métricas** de rendimiento en tiempo real
- **Proporcionar ejemplos prácticos** para estudiantes

## 🏗️ Arquitectura

### Componentes Principales

1. **Modelos de Datos**
   - `User`: Usuarios del sistema con balance
   - `Category`: Categorías de gastos (Alimentación, Transporte, etc.)
   - `Expense`: Gastos con diferentes métodos de pago

2. **Métodos de Pago**
   - 💰 **CASH**: Procesamiento más rápido (50ms)
   - 💳 **DEBIT_CARD**: Procesamiento medio (150ms)
   - 🏦 **CREDIT_CARD**: Procesamiento lento (200ms)

3. **Servicios**
   - `ReactiveExpenseService`: Implementación reactiva con WebFlux
   - `TraditionalExpenseService`: Implementación tradicional con Spring Web

4. **Controladores**
   - `/api/v1/reactive/*`: Endpoints reactivos
   - `/api/v1/traditional/*`: Endpoints tradicionales
   - `/api/v1/comparison/*`: Endpoints de comparación

## 🚀 Inicio Rápido

### Prerrequisitos

- **Java 21** o superior
- **Podman** o **Docker**
- **Gradle** (incluido en el proyecto)

### Verificación de Java 21

Antes de ejecutar la demo, verifica que tengas Java 21 instalado:

#### Linux/macOS
```bash
chmod +x check-java21.sh
./check-java21.sh
```

#### Windows PowerShell
```powershell
.\check-java21.ps1
```

### Ejecución Automática

#### Linux/macOS
```bash
# Hacer ejecutables los scripts
chmod +x start-demo.sh cleanup.sh

# Ejecutar la demo completa
./start-demo.sh

# Limpiar todo (detener contenedores, limpiar build)
./cleanup.sh
```

#### Windows PowerShell
```powershell
# Configurar política de ejecución (solo la primera vez)
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Ejecutar la demo completa
.\start-demo.ps1

# Limpiar todo (detener contenedores, limpiar build)
.\cleanup.ps1
```

### Ejecución Manual

1. **Iniciar MongoDB**
   ```bash
   # Con Podman
   podman-compose up -d mongodb
   
   # Con Docker
   docker-compose up -d mongodb
   ```

2. **Compilar y ejecutar**
   ```bash
   ./gradlew clean build
   ./gradlew bootRun
   ```

## 📊 Endpoints Disponibles

### WebFlux (Reactivo)
- `GET /api/v1/reactive/expenses` - Listar todos los gastos
- `POST /api/v1/reactive/expenses` - Crear nuevo gasto
- `GET /api/v1/reactive/expenses/{id}` - Obtener gasto por ID
- `GET /api/v1/reactive/expenses/user/{userId}` - Gastos por usuario
- `GET /api/v1/reactive/expenses/payment-method/{method}` - Gastos por método de pago

### Spring Web (Tradicional)
- `GET /api/v1/traditional/expenses` - Listar todos los gastos
- `POST /api/v1/traditional/expenses` - Crear nuevo gasto
- `GET /api/v1/traditional/expenses/{id}` - Obtener gasto por ID
- `GET /api/v1/traditional/expenses/user/{userId}` - Gastos por usuario
- `GET /api/v1/traditional/expenses/payment-method/{method}` - Gastos por método de pago

### Comparación y Pruebas
- `GET /api/v1/comparison/performance-test` - Prueba de rendimiento
- `GET /api/v1/comparison/stress-test` - Prueba de estrés
- `GET /api/v1/comparison/health` - Estado del servicio

### Monitoreo
- `GET /actuator/health` - Salud de la aplicación
- `GET /actuator/metrics` - Métricas de la aplicación
- `GET /actuator/prometheus` - Métricas en formato Prometheus

## 🔬 Pruebas de Rendimiento

### 1. Prueba de Rendimiento Básica
```bash
curl http://localhost:8080/api/v1/comparison/performance-test
```

**Mide:**
- Tiempo de creación de gastos
- Tiempo de consultas masivas
- Tiempo de procesamiento de métodos de pago

### 2. Prueba de Estrés
```bash
curl http://localhost:8080/api/v1/comparison/stress-test
```

**Simula:**
- 100 requests concurrentes
- Comparación de tiempos de respuesta
- Análisis de escalabilidad

### 3. Pruebas Manuales

#### Crear Gasto (WebFlux)
```bash
curl -X POST http://localhost:8080/api/v1/reactive/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "juan.perez",
    "categoryId": "1",
    "amount": 75.50,
    "description": "Gasto de prueba WebFlux",
    "paymentMethod": "CREDIT_CARD",
    "date": "2024-01-20"
  }'
```

#### Crear Gasto (Spring Web)
```bash
curl -X POST http://localhost:8080/api/v1/traditional/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "juan.perez",
    "categoryId": "1",
    "amount": 75.50,
    "description": "Gasto de prueba Spring Web",
    "paymentMethod": "CREDIT_CARD",
    "date": "2024-01-20"
  }'
```

## 📈 Diferencias Clave

### WebFlux (Reactivo)
- ✅ **No bloqueante**: Maneja múltiples requests en un solo thread
- ✅ **Escalable**: Mejor rendimiento con alta concurrencia
- ✅ **Eficiente en memoria**: Menor uso de recursos del sistema
- ✅ **Streaming**: Procesamiento de datos en tiempo real
- ❌ **Curva de aprendizaje**: Más complejo de entender y debuggear

### Spring Web (Tradicional)
- ✅ **Familiar**: Patrón MVC estándar
- ✅ **Fácil de debuggear**: Flujo secuencial claro
- ✅ **Compatible**: Funciona con librerías tradicionales
- ❌ **Bloqueante**: Un thread por request
- ❌ **Menos escalable**: Consume más recursos con alta concurrencia

## 🗄️ Base de Datos

### MongoDB
- **Puerto**: 27017
- **Base de datos**: `expenses_demo`
- **Usuario**: `admin`
- **Contraseña**: `password123`

### MongoDB Express
- **Puerto**: 8081
- **Usuario**: `admin`
- **Contraseña**: `password123`
- **URL**: http://localhost:8081

### Datos de Ejemplo
- **3 usuarios** con diferentes balances
- **5 categorías** de gastos
- **3 gastos** de ejemplo

## 🧹 Limpieza

### Linux/macOS
```bash
chmod +x cleanup.sh
./cleanup.sh
```

### Windows PowerShell
```powershell
.\cleanup.ps1
```

## 📚 Conceptos Clave para Estudiantes

### 1. **Programación Reactiva**
- **Mono**: Representa 0 o 1 resultado
- **Flux**: Representa 0 a N resultados
- **Backpressure**: Control de flujo de datos
- **Operadores**: `map`, `flatMap`, `filter`, etc.

### 2. **No-Blocking I/O**
- **Event Loop**: Procesamiento asíncrono
- **Callbacks**: Manejo de operaciones completadas
- **Streams**: Procesamiento de datos en tiempo real

### 3. **Concurrencia vs Paralelismo**
- **Concurrencia**: Múltiples tareas ejecutándose "simultáneamente"
- **Paralelismo**: Múltiples tareas ejecutándose en paralelo real

### 4. **Patrón Publisher-Subscriber**
- **Publisher**: Emite eventos/datos
- **Subscriber**: Consume eventos/datos
- **Subscription**: Controla el flujo de datos

### 5. **Características de Java 21**
- **Virtual Threads**: Hilos ligeros para mejor concurrencia
- **Pattern Matching**: Mejor manejo de tipos y estructuras
- **Record Patterns**: Patrones para records inmutables
- **String Templates**: Interpolación de strings mejorada
- **Sequenced Collections**: Acceso ordenado a colecciones

## 🔍 Análisis de Logs

### Logs de WebFlux
```
Endpoint reactivo: Creando gasto para usuario: juan.perez
Creando gasto reactivo para usuario: juan.perez
Validando solicitud de gasto para usuario: juan.perez
Procesando método de pago: CREDIT_CARD
Procesando tarjeta de crédito por monto: 75.50
Gasto creado con ID: 507f1f77bcf86cd799439011
```

### Logs de Spring Web
```
Endpoint tradicional: Creando gasto para usuario: juan.perez
Creando gasto tradicional para usuario: juan.perez
Validando solicitud de gasto para usuario: juan.perez
Procesando método de pago: CREDIT_CARD
Procesando tarjeta de crédito por monto: 75.50
Gasto creado con ID: 507f1f77bcf86cd799439012
```

## 🎓 Actividades para Estudiantes

### 1. **Análisis de Rendimiento**
- Ejecutar pruebas de rendimiento
- Comparar tiempos de respuesta
- Analizar uso de memoria y CPU

### 2. **Modificación de Lógica de Negocio**
- Agregar nuevos métodos de pago
- Implementar validaciones adicionales
- Crear nuevos endpoints

### 3. **Debugging y Monitoreo**
- Usar logs para rastrear flujo de datos
- Monitorear métricas con Actuator
- Analizar comportamiento bajo carga

### 4. **Escalabilidad**
- Aumentar número de requests concurrentes
- Monitorear comportamiento del sistema
- Comparar límites de ambos enfoques

## 🐛 Troubleshooting

### Problemas Comunes

1. **MongoDB no inicia**
   ```bash
   # Verificar logs
   podman-compose logs mongodb
   
   # Verificar puertos
   netstat -tulpn | grep 27017
   ```

2. **Error de compilación**
   ```bash
   # Limpiar y recompilar
   ./gradlew clean build
   
   # Verificar versión de Java
   java -version
   
   # Verificar que sea Java 21
   java -version 2>&1 | grep "version" | cut -d'"' -f2 | cut -d'.' -f1
   ```

3. **Error de conexión a MongoDB**
   ```bash
   # Verificar que MongoDB esté corriendo
   podman exec demo-mongodb mongosh --eval "db.runCommand('ping')"
   ```

4. **Conflicto entre WebFlux y Spring Web**
   ```
   Error: The bean 'requestMappingHandlerMapping' could not be registered
   ```
   
   **Solución**: Ver archivo `TROUBLESHOOTING.md` para detalles completos.
   
   **Solución rápida**:
   ```properties
   # En application.properties
   spring.main.allow-bean-definition-overriding=true
   ```

## 📞 Soporte

Para dudas o problemas:
- Revisar logs de la aplicación
- Verificar configuración de MongoDB
- Consultar documentación de Spring WebFlux

## 📄 Licencia

Este proyecto es para fines educativos. Libre de uso y modificación.

---

**¡Disfruta explorando las diferencias entre WebFlux y Spring Web! 🎉**
