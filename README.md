# 🧾 PideYaSuite – Proyecto Académico de Ingeniería de Software

Este repositorio contiene una solución completa de ejemplo compuesta por múltiples módulos Java desarrollados con **Java 17** y **Maven**, diseñados para fines educativos en cursos de Ingeniería de Software. El proyecto está organizado en carpetas según el tipo de componente:

---

## 📁 Estructura del proyecto

### 1️⃣ `1_BackEnd/`

Contiene dos proyectos de backend:

- **PermisoYaBackEnd**: Servicio que gestiona los accesos a menús según los roles de usuario. Permite determinar qué opciones de menú están habilitadas para cada usuario.
- **PideYaBackEnd**: Servicio completo de gestión que incluye operaciones sobre productos, categorías, pedidos, compras y clientes (tanto naturales como empresas).

Ambos proyectos están desarrollados con **Java 17** y **Maven**.

---

### 2️⃣ `2_Service/`

Contiene el módulo:

- **VisaPago**: Backend que simula una conexión a un servicio de pago tipo Visa. El método principal tiene una lógica en la que el **80% de las veces el servicio responde correctamente**, y el **20% de las veces falla**, simulando un entorno realista.

También desarrollado en **Java 17** con **Maven**.

---

### 3️⃣ `3_FrontEnd/`

Contiene la aplicación:

- **PideYaDesktop**: Interfaz gráfica de usuario construida con **Java Swing**, que se comunica con los servicios:
  - `PermisoYaBackEnd`
  - `PideYaBackEnd`
  - `VisaPago`

Esta aplicación también utiliza **Java 17** y está gestionada con **Maven**.

---

### 4️⃣ `4_DataBase/`

Incluye:

- Modelos de base de datos.
- Scripts SQL para la creación de las bases de datos **PideYa** y **PermisoYa**, utilizando **MySQL** como sistema gestor.

---

## 👤 Autor

**Henry Wong**  
Docente de Ingeniería de Software  
Universidad de Lima – Facultad de Ingeniería de Sistemas

---

## 📜 Licencia

Este proyecto está protegido por copyright © 2025 **Henry Wong**.  
Está permitido su uso únicamente con fines **educativos y académicos** en el marco de cursos universitarios.  
**Queda prohibido su uso en entornos de producción o con fines comerciales.**

---

## ⚠️ Nota

Este repositorio es un recurso de ejemplo para prácticas en clase. No está optimizado para ambientes reales ni cumple con todas las medidas de seguridad y escalabilidad requeridas en aplicaciones comerciales.

