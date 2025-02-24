# Servidor Web hecho en Java

Este proyecto tiene como objetivo el desarrollo de un servidor web en Java, similar a Apache, capaz de entregar páginas HTML e imágenes en formato PNG. Además, el servidor incluirá un framework IoC (Inversión de Control) que permitirá la construcción de aplicaciones web a partir de POJOs (Plain Old Java Objects).

Como parte de la implementación, se desarrollará una aplicación web de ejemplo utilizando el servidor. El servidor será capaz de atender múltiples solicitudes de manera no concurrente.

Para demostrar las capacidades reflexivas de Java, se implementará un prototipo mínimo que permita la carga dinámica de un bean (POJO) y la generación de una aplicación web basada en él.

## Autor

* **Nicolas Bernal** - Autor y Desarrollador del Proyecto

### Paso a paso para la instalacion, compilacion y ejecucion del proyecto

Lo primero que debes hacer es clonar este repositorio y navegar al directorio donde lo clonaste:

```
git clone https://github.com/NicoBernal19/Taller3AREP.git
```

Construte y tambien compila el proyecto con Maven:

```
mvn clean install
```

Ejecuta la aplicacion:

```
 java -cp target/classes co.edu.eci.arep.webframework.WebFramework
```

El servidor esta hecho para que inicie en el puerto `35000`, una vez ejecutado, ya esta listo para que puedas usarlo y probarlo todo lo que desees.

## Funcionalidades del servidor

El código está estructurado en diferentes archivos para no tener una sola clase llena de codigo, ademas de que es una buena practica y permite que todo este mas ordenado y compacto, hace que sea mas facil extender a posterior el codigo. Tambien organize los controladores y anotaciones en paquetes distintos para que este todo mas organizado.

Entre las funcionalidades que se ofrecen encontramos las siguientes:

- Permite servir HTML, CSS, JS e imágenes.
- Hace uso de las capacidades reflexivas de JAVA

## Pruebas

### Pruebas Unitarias

Para poder ejecutar las pruebas con las que cuenta este proyecto ejecuta el siguiente comando:

```
mvn test
```

A continuacion se encuentran imagenes de la ejecucion de las pruebas:

![imagen](src/main/resources/web/images/1.png)

![imagen](src/main/resources/web/images/2.png)

![imagen](src/main/resources/web/images/3.png)

### Pruebas en tiempo real

Puedes abrir tu navegador y acceder al siguiente enlace, para poder explorar y probar la aplicacion web:

```
http://localhost:35000
```

Una vez abierto el servidor web puedes navegar la pagina, probando las distintas funcionalidades. A continuacion encontramos algunos ejemplos de acciones que se pueden hacer:

![imagen](src/main/resources/web/images/4.png)

![imagen](src/main/resources/web/images/5.png)

![imagen](src/main/resources/web/images/6.png)

Por otro lado, puedes dirigirte al siguiente enlace (o uno similar) para probar la funcionalidad del saludo personalizado con tu nombre:

```
http://localhost:35000/hello?name=nicolas
```

Si prefieres tambien puedes dirigirte a los demas endpoints disponibles para darles un vistazo:

```
http://localhost:35000/random?min=1&max=18 (Puedes cambiar los parametros a tu gusto)
http://localhost:35000/greeting
http://localhost:35000/later
http://localhost:35000/pi
http://localhost:35000/e
```

### Pruebas archivos estáticos

Finalmente, tambien puedes probar a acceder a los archivos estaticos con los que cuenta el proyecto:

```
http://localhost:35000/index.html
http://localhost:35000/static/script.js
http://localhost:35000/static/styles.css
http://localhost:35000/images/realMadrid.png
```
