# POO-Unidad-3-Actividad-1
Actividad 5: KMeans distribuido

### TODO
1. Implementar data binding para la tabla a analizar
2. Cargar el data model al controller no funciona del todo o más su implementación es problemática
3. Con el cargado de archivos excel y csv a un tipo de dato
4. Implementar los algoritmo de kmeans con sus respectivas pruebas unitarias
5. Dar al usuario la opción de poner la tabla con encabezados o sin encabezados, debido a que hay tablas (xlsx, csv) que no cuentan con *headers* por lo que la primera fila debe ser considera como datos
6. *Agregar al final* Drag & Drop
7. Añadir un botón para seleccionar las 2 columnas a graficar su correlación(Nota: a la gráfica hay que añadirle la línea de regresión y el coeficiente de Pearson)
8. Necesito rediseñar el modelo de tabla, en vez de guardar un objeto TableView creo que puedo hacer un observablelist de olbservablelist's que sea el usado en para la tabla, pero no estoy muy seguro, en mi mente funciona pero tengo que ver bien como se hace el data binding del tableview
9. La tabla funciona pero tiene problemas ya que no supe implementarla bien: ver el listener agregado al header; quizá tengo que rediseñar la tabla, pero también el programa se crashea con tantos datos, quizá es por el mismo error o tengo que ver más eso de performance.
10. Necesito pasar información hacia un pop up y reciber de ese pop up. Despues de esto lo de graficar se hace muy sencillo, porque ya tenemos las variables instanciadas en el modelo.

### Objetivo de la Práctica

El alumno deberá demostrar solura en los siguientes temas de la Programación Orientada a Objetos.
  * Manejo de métodos de procesamiento distribuido
  * Manejo de excepciones y errores.
  * Pruebas Unitarias
  * Interfaces gráficas
  * Paquetes

### Descripción de la práctica.

Se deberá implementar una herramienta que utilice una versión concurrente del Algortmo KMeans++ para la agrupación de datos. Se deberán tener las siguientes características:

  1. Interfáz gráfica para cargar datos y análisis de datos.
  2. Capacidad de generar una gráfica de correlación entre 2 (de las n posibles) variables del problema.
  3. Generación de una gráfica de disperción que permita visualizar el proceso de agrupación de los datos. Se deberá poder guardar la gráfica final como imagen.

### Requerimientos Funcionales
Se deberá hacer uso de los siguientes temas:

  * Se debe implenetar un algortmo KMeans++ paralelo.
  * Manejo de excepciones
  * Excepciones propias
  * Propagación de errores.
  * Pruebas Unitarias
  * Pruebas de integración
  * Notación infija
  * Interfaces gráficas

### Requerimientos no funcionales

  * Se introducirán los datos a través de archivos de texto o archivos de excel.

### Entregables:

  1. Código de la implementación documentado mediante JavaDoc.
  2. Se deberá generar el archivo Jar => **Se subirá a plataforma google classroom**
  3. Se deberá generar un reporte de actividades en formato PDF => **Se subirá a plataforma google classroom**


