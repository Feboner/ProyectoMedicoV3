# Proyecto Médico Microservicios

## Descripción del contexto o dominio del proyecto
Este proyecto implementa una arquitectura de microservicios para un sistema médico orientado a la gestión de pacientes, pedidos, recetas, envíos y otros procesos clínicos y operativos. La idea es separar responsabilidades por dominio para facilitar mantenimiento, escalabilidad y documentación de cada servicio.

## Nombres de los/las estudiantes
- Apolo Vallejos
- Christian Cabrera

## Listado de microservicios implementados
- ms-auth: autenticación y autorización
- ms-gateway: puerta de entrada y agregación de rutas
- ms-pacientes: gestión de pacientes
- ms-productos: gestión de productos y catálogo
- ms-pedidos: gestión de pedidos médicos
- ms-recetas: gestión de recetas médicas
- ms-envios: gestión de envíos
- ms-inventario: gestión de inventario
- ms-pagos: gestión de pagos
- ms-proveedores: gestión de proveedores
- ms-auditoria: registro y auditoría del sistema

## Rutas principales del Gateway
- /api/v1/auth/**
- /api/v1/pacientes/**
- /api/v1/productos/**
- /api/v1/pedidos/**
- /api/v1/recetas/**
- /api/v1/envios/**
- /api/v1/inventario/**
- /api/v1/pagos/**
- /api/v1/proveedores/**
- /api/v1/auditoria/**
- /swagger-center.html

## Enlace a la documentación Swagger
- Gateway Swagger Center: http://localhost:8080/swagger-center.html

## Instrucciones básicas de ejecución local y remota
### Ejecución local
1. Asegúrate de tener Docker y Docker Compose instalados.
2. Desde la raíz del proyecto ejecuta:
   - docker compose up -d --build
3. Luego accede a:
  
   - http://localhost:8080/swagger-center.html

