# Personal Trainer App - MVC Architecture

## Descripción 📱
Personal Trainer App es una aplicación móvil diseñada para la gestión de entrenamientos personales. Esta herramienta permite a los entrenadores gestionar ejercicios, rutinas, clientes y cronogramas de manera eficiente. Desarrollada como parte del curso de Arquitectura de Software utilizando el patrón MVC (Modelo-Vista-Controlador).

## Funcionalidades principales 🏋️‍♂️

### Gestión de Ejercicios
- Crear, editar y eliminar ejercicios
- Asignar imágenes descriptivas
- Organizar por categorías

### Gestión de Rutinas
- Diseñar rutinas personalizadas
- Asociar ejercicios específicos
- Definir objetivos y duración

### Gestión de Clientes
- Registrar y administrar clientes
- Gestionar información personal
- Asignar objetivos específicos

### Cronogramas
- Planificación semanal de entrenamientos
- Asignación de rutinas a clientes
- Seguimiento de progreso

## Arquitectura MVC 🏗️

El proyecto sigue el patrón Modelo-Vista-Controlador para garantizar separación de responsabilidades:

### Modelo (Model)
- `ObjetivoModel`: Gestiona datos y negocio de objetivos
- `ClienteModel`: Gestiona datos y negocio de clientes
- `DetalleClienteModel`: Gestiona datos y negocio de los detalles de clientes
- `EjercicioModel`: Maneja información de ejercicios
- `RutinaModel`: Administra las rutinas de entrenamiento
- `DetalleRutinaModel`: Administra los detalles de rutinas de entrenamiento
- `CronogramaModel`: Controla la planificación

### Vista (View)
- `ObjetivoView`: Interfaz para gestión de objetivos
- `ClienteView`: Interfaz para gestión de clientes
- `EjercicioView`: Pantallas para ejercicios
- `RutinaView`: Visualización de rutinas
- `CronogramaView`: Interfaz de planificación

### Controlador (Controller)
- `ObjetivoController`: Controlador para gestión de clientes
- `ClienteController`: Controlador para gestión de clientes
- `EjercicioController`: Controla operaciones de ejercicios
- `RutinaController`: Maneja la gestion de rutinas
- `CronogramaController`: Gestiona los cronogramas

## Tecnologías utilizadas 💻

- **Lenguaje principal**: Java
- **Plataforma**: Android
- **Base de datos**: SQLite
- **IDE**: Android Studio
- **Control de versiones**: Git/GitHub

## Instalación y configuración ⚙️

### Prerrequisitos
- Android Studio instalado
- JDK 11 o superior
- Dispositivo Android o emulador configurado

### Pasos para ejecutar el proyecto
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Miguel-Machuca/entrenador-personal-arquitectura-mvc.git
   ```
2. Abrir el proyecto en Android Studio
3. Sincronizar las dependencias Gradle
4. Ejecutar en emulador o dispositivo físico

## Estructura del proyecto 📂

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/personal_trainer/
│   │   │   ├── controllers/      # Controladores MVC
│   │   │   ├── models/           # Modelos de datos y Lógica de Negocio
│   │   │   ├── views/            # Vistas y actividades
│   │   │   └── utils/            # Utilitarios
│   │   └── res/                  # Recursos (layouts, drawables, etc.)
│   └── test/                     # Pruebas unitarias
```

## Autor ✍️
**Miguel Angel Machuca Yavita**  


