# MS CampusLab BFF (Backend For Frontend)

Microservicio de orquestación y mediación entre el cliente frontend y los microservicios de dominio (como el catálogo). Su propósito es centralizar, transformar y simplificar el flujo de peticiones hacia la interfaz de usuario.

## 🛠️ Tecnologías Utilizadas

* **Java** 21+ / Java 25
* **Spring Boot** 4.x / 3.x
* **Spring Web / WebFlux / RestTemplate / WebClient** (Consumo de servicios HTTP internos)
* **Jackson** (Manipulación de estructuras JSON)
* **Apache Maven** (Gestor de dependencias y construcción)

## 📋 Requisitos Previos

1. **JDK 21** o superior instalado.
2. **Maven** instalado (o usar el wrapper `./mvnw` / `mvnw.cmd`).
3. El microservicio **`ms-campuslab-catalog`** corriendo en el puerto `8082`.

## 🚀 Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd ms-campuslab-bff
Configurar los endpoints de los servicios de dominio:
Revisa las URLs objetivo en src/main/resources/application.yaml o application.properties para apuntar hacia ms-campuslab-catalog (ej. http://localhost:8082).

Compilar el proyecto:

Bash
mvn clean compile
Ejecutar la aplicación:

Bash
mvn spring-boot:run
El BFF estará disponible por defecto en: http://localhost:8080