# 🧾 Proyecto PideYaSuite – Sistema de Gestión de Pedidos (Arquitectura Cliente-Servidor)

Este repositorio contiene el código fuente y la base de datos para el sistema **PideYaSuite**. Este proyecto ha sido diseñado como material educativo y de ejemplo para clases universitarias, demostrando cómo construir e integrar una arquitectura Cliente-Servidor compuesta por aplicaciones de escritorio, múltiples servicios backend y la simulación de componentes de terceros.

El sistema implementa:
* **Gestión de Accesos:** Control dinámico de permisos y opciones de menú basados en roles de usuario.
* **Mantenimientos y Operaciones (CRUD):** Gestión centralizada de productos, categorías, pedidos, compras y clientes (naturales y jurídicos).
* **Integración y Tolerancia a Fallos:** Conexión a un simulador de pasarela de pagos (Visa) que implementa escenarios probabilísticos de éxito y error para pruebas de resiliencia.

---

## 📁 Estructura del Repositorio

El proyecto está dividido en cuatro grandes módulos principales:

### ☕ 1_BackEnd (Lógica de Negocio y API)
Contiene los servicios principales del sistema, desarrollados con **Java 17** y **Maven**.
* **PermisoYaBackEnd:** Micro-componente encargado de autorizar y distribuir los accesos a los menús según el perfil del usuario autenticado.
* **PideYaBackEnd:** Servicio core que expone toda la lógica de negocio y transacciones (productos, pedidos, clientes).

### 🔌 2_Service (Simulación de Terceros)
Contiene componentes diseñados para emular integraciones con sistemas externos.
* **VisaPago:** Backend independiente que simula una pasarela de pagos. Implementa una lógica estocástica donde el 80% de las transacciones son exitosas y el 20% fallan, permitiendo a los estudiantes programar el manejo de excepciones y reintentos.

### 💻 3_FrontEnd (Interfaz de Usuario)
Contiene la aplicación cliente con la que interactúan los usuarios finales.
* **PideYaDesktop:** Interfaz Gráfica de Usuario (GUI) construida de forma nativa con **Java Swing**. Actúa como el orquestador visual, consumiendo los servicios de permisos, gestión y pagos.

### 🗄️ 4_DataBase (Persistencia)
Contiene la infraestructura de datos y el modelado del sistema.
* Aloja los diagramas entidad-relación y los scripts SQL necesarios para crear la estructura de las bases de datos **PideYa** y **PermisoYa** en **MySQL**.

---

## ⚠️ Reglas de Oro (Para el entorno de desarrollo)

Para garantizar que el ecosistema local funcione correctamente, el orden de ejecución es fundamental:

1.  **La Base de Datos manda:** Los scripts SQL deben ejecutarse primero en tu gestor de base de datos para crear la estructura y los datos semilla (seed data).
2.  **Los Servicios van primero:** Antes de abrir la aplicación de escritorio, debes asegurarte de que los tres proyectos backend (`PermisoYaBackEnd`, `PideYaBackEnd` y `VisaPago`) estén compilados y ejecutándose.
3.  **Aislamiento de Puertos:** Al correr múltiples servicios en Java simultáneamente, asegúrate de que cada uno esté configurado para escuchar en un puerto HTTP distinto y evitar colisiones de red local.

---

## 🚀 Guía de Ejecución Paso a Paso

### 🗄️ Fase 0: Preparar la Base de Datos
El entorno de datos debe ser lo primero en inicializarse.
1. Abre tu gestor de base de datos MySQL (ej. MySQL Workbench o DBeaver).
2. Localiza los archivos SQL en la carpeta `4_DataBase`.
3. Ejecuta los scripts para generar las bases de datos `PideYa` y `PermisoYa`.

### ☕ Fase 1: Iniciar el Ecosistema Backend (Java 17)
Preparamos los servicios y simuladores para recibir peticiones.
1. Importa los proyectos de la carpeta `1_BackEnd` a tu IDE de preferencia.
2. Compila las dependencias con Maven (`mvn clean install`) y ejecuta las clases principales de ambos servicios.
3. Importa el proyecto `VisaPago` de la carpeta `2_Service` y ejecútalo.
*(Verifica en las consolas de tu IDE que los tres servicios estén activos y sin errores).*

### 💻 Fase 2: Ejecutar el Frontend Cliente (Java Swing)
Iniciamos la interfaz de usuario para operar el sistema.
1. Importa el proyecto `PideYaDesktop` desde la carpeta `3_FrontEnd`.
2. Descarga las dependencias con Maven.
3. Ejecuta la clase `Main` (o su equivalente de inicio) para lanzar la aplicación de escritorio.

---

## 🛠️ Stack Tecnológico

* **Backend y Servicios:** Java 17, Maven
* **Frontend:** Java Swing
* **Base de Datos:** MySQL
* **Arquitectura:** Sistemas Distribuidos / Cliente-Servidor

---

## 👤 Autor
**Autor:** [Henry Wong](https://github.com/hwongu)  

---

## 📜 Licencia

Este proyecto está protegido por copyright © 2025 **Henry Wong**.  
Está permitido su uso únicamente con fines **educativos y académicos** en el marco de cursos universitarios.  
**Queda prohibido su uso en entornos de producción o con fines comerciales.**

---

## ⚠️ Nota Académica

Este repositorio es un recurso de ejemplo diseñado específicamente para prácticas en clases. No está optimizado para ambientes reales ni cumple con todas las medidas de seguridad y escalabilidad requeridas en aplicaciones comerciales.

---

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-Build-C71A22?style=for-the-badge&logo=apachemaven)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql)
