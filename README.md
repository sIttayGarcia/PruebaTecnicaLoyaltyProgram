# PruebaTecnicaLoyaltyProgram
Prueba tecnica de programa de lealtad

He realizado la prueba tecnica para la gestion de un programa de lealtad, en un breve resumen del sistema es que he creado los siguientes servicios REST

1. servicio para la creacion y registros de usuarios
2. Login de Usuarios con JWT
3. Servicio para la creacion de las recompensas que se podran adquirir
4. servicio para la creacion de acciones que generaran puntos de recompesas a los usuarios
5. Servicio para el "Canje" de puntos de los usuarios
6. Servicio para conocer el historial de Canjes de los usuarios
7. Servicio para consultar todos los usuarios registrados en el programa de lealtad busqueda por ID o Username
8. Servicio para consultar todos los usuarios registrados en el programa de lealtad usando sistema de paginacion
9. Servicio para consultar el detalle especifico del usuario registrado en el programa de Lealtad

En cada servicio se han agregado validaciones para un correcto y basico funcionamiento
Se han agregado pruebas unitarias utilizando jUnit y Mockito

### Configuración manual

1. Asegúrate de tener MySQL instalado y corriendo.
2. Ejecuta el archivo `schema.sql` en tu cliente MySQL para crear la base de datos y las tablas:

"mysql -u tu_usuario -p < schema.sql"

### Modificar el application.properties 
1. Modificar el archivo application.properties
2. spring.datasource.username=tu_usuario
3. spring.datasource.password=tu_contraseña

### Compilar Proyecto
1- Usar maven para compilar el proyecto y bajar dependencias
"mvn clean install"

### Levantar proyecto
1. Inicializar el proyecto

### Pruebas de funcionalidades
Se ha agregado una coleccion de Postman para probar los servicios Rest 
1. Importar "PruebaBimbo.postman_collection.json"
2. Probar funcionalidades



