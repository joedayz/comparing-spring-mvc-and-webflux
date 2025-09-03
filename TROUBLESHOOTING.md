# 🐛 Guía de Troubleshooting - Demo WebFlux vs Spring Web

## Problema: Conflicto entre WebFlux y Spring Web

### Error Típico
```
APPLICATION FAILED TO START

Description:
The bean 'requestMappingHandlerMapping', defined in org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration, 
could not be registered. A bean with that name has already been defined in 
org.springframework.web.reactive.config.DelegatingWebFluxConfiguration and overriding is disabled.
```

### Causa del Problema
Este error ocurre porque estamos intentando usar **WebFlux** y **Spring Web MVC** en la misma aplicación. Ambos frameworks tratan de registrar beans con nombres similares, causando conflictos.

### Soluciones Implementadas

#### 1. **Solución Rápida (Ya implementada)**
```properties
# En application.properties
spring.main.allow-bean-definition-overriding=true
```

#### 2. **Configuración Estructurada (Recomendada)**
- **AppConfig.java**: Configuración principal con WebFlux como framework principal
- **WebMvcConfig.java**: Configuración condicional para Spring Web MVC
- **MongoConfig.java**: Configuración específica de MongoDB

### Verificación de la Solución

#### 1. **Verificar que la aplicación inicie**
```bash
./gradlew bootRun
```

#### 2. **Probar endpoints de WebFlux**
```bash
curl http://localhost:8080/api/v1/reactive/expenses
```

#### 3. **Probar endpoints de Spring Web**
```bash
curl http://localhost:8080/api/v1/traditional/expenses
```

### Si el Problema Persiste

#### Opción 1: Usar solo WebFlux
```properties
# En application.properties
spring.main.web-application-type=reactive
```

#### Opción 2: Usar solo Spring Web
```properties
# En application.properties
spring.main.web-application-type=servlet
```

#### Opción 3: Separar en dos aplicaciones
- **Aplicación 1**: Solo WebFlux
- **Aplicación 2**: Solo Spring Web

### Logs de Depuración

Para ver más detalles del problema, habilita logs de Spring:

```properties
# En application.properties
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.boot.autoconfigure=DEBUG
```

### Comandos de Verificación

#### Verificar beans registrados
```bash
curl http://localhost:8080/actuator/beans
```

#### Verificar configuración
```bash
curl http://localhost:8080/actuator/configprops
```

#### Verificar métricas disponibles
```bash
curl http://localhost:8080/actuator/metrics
```

#### Verificar métricas de Prometheus
```bash
curl http://localhost:8080/actuator/prometheus
```

### Configuración de Métricas

#### Configuraciones Modernas (Spring Boot 3.x)
```properties
# Métricas básicas
management.metrics.export.prometheus.enabled=true
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.percentiles.http.server.requests=0.5,0.95,0.99

# Métricas del sistema
management.metrics.enable.jvm=true
management.metrics.enable.process=true
management.metrics.enable.system=true
management.metrics.enable.mongodb=true

# Tags personalizados
management.metrics.tags.application=demo-webflux-mongodb
management.metrics.tags.version=1.0.0
```

#### Configuraciones Deprecadas (NO USAR)
```properties
# ❌ DEPRECATED - NO USAR
management.metrics.web.server.request.autotime.enabled=true
management.metrics.web.server.request.autotime.percentiles=0.5,0.95,0.99
management.metrics.web.server.request.autotime.percentiles-histogram=true
```

### Estructura de Archivos de Configuración

```
src/main/java/pe/joedayz/demo_webflux_mongodb/config/
├── AppConfig.java          # Configuración principal (WebFlux)
├── WebMvcConfig.java       # Configuración de Spring Web MVC
├── MongoConfig.java        # Configuración de MongoDB
└── MetricsConfig.java      # Configuración de métricas y monitoreo
```

### Notas Importantes

1. **WebFlux es la configuración por defecto** en esta demo
2. **Spring Web MVC se ejecuta en modo compatible**
3. **Ambos frameworks comparten la misma base de datos**
4. **Los conflictos se resuelven mediante priorización de beans**

### Recursos Adicionales

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Web MVC Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Boot Auto-configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration)

---

**¡Si sigues teniendo problemas, revisa los logs y verifica que Java 21 esté instalado correctamente!**
