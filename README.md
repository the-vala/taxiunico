# TaxiUnico

### Objetivo
Taxi Único es una aplicación diseñada y construida para la empresa Transpais. Su objetivo es el de ofrecer un servicio de taxi tipo Uber para complementar el servicio de autobús.

### Descripción
Aplicación móvil que permitirá al	usuario	conectar por medio de una	aplicación móvil a los pasajeros de	autobús Transpais	y a	los conductores de vehículos registrados en nuestro	servicio de Taxi Único quienes desean	ofrecer	un servicio	de autotransporte.

### Características generales
1. Registrarse
2. Iniciar sesión / Cerrar sesión
3. Revisar/actualizr perfil de usuario
4. Inicar un viaje con código de reservación
5. Terminar viaje
6. Checar el historial de viajes pasados y pendientes.
7. Recuperar contraseña por correo de firebase.
8. Agregar forma de pago

### Instrucciones de Instalación

1. Clone o descara el repositorio

  shell
  git clone https://github.com/the-vala/taxiunico.git
  

2. Importa el proyecto en Android Studio o ejecuta el siguiente comando.

  shell
  ./gradlew clean app:assembleArmDebug
  
3. Define tu Google Api key (sigue las siguientes instrucciones).

### Define tu Google Api Key en build.gradle
Find the .gradle folder in home directory.

* Windows: C:\Users\<Your Username>\.gradle
* Mac: /Users/<Your Username>/.gradle
* Linux: /home/<Your Username>/.gradle

Dentro de ese archive debe de estar gradle.properties, créalo si no existe.

Después agrega tu Google Api key como una propiedad. Example:

Google_ApiKey ="my-awesome-api-key"



### Versión
* Versión: 0.1 - Fecha de última actualización: 24/04/2019
* Versión: 0.2 - Fecha de última actualización: 26/04/2019
* Versión: 0.3 - Fecha de última actualización: 14/05/2019

#### Cambios de cada version
##### V0.1
1. Agregar integración con Firebase.
2. Agregar ventana para agregar un metodo de pago.
3. Agregar ventana de perfil de usuario.
4. Agregar ventana de encuesta de satisfación. 
5. Agregar ventana de registro e iniciar sesión.
6. Agregar metodo de recuperar contraseña.
##### V0.2
1. Agregar encuesta para los conductores y solicitar encuesta para los viajeros.
2. Agregar funcionalidad basica para solicitar viajes.
3. Agregar historial de viajes.
4. Agregar la persistencia de usuarios.
5. Migrar artefactos a androidX
6. Agregar mecanismo para diferenciar viajero/conductor.
7. Migracion a co-rutinas
8. Agregar licencia copyright a todas las clases.
##### V0.3
1. Agregar validación para código reservación.
2. Escoder la llave de Google.
3. Agregar validciónes de conexión.
4. Agregar funcionalidad para cancelar el viaje.
5. Agregar comentarios y documentación en codigo.
6. Agregar funcionalidad para recibir encuestas en tiempo real.
7. Agregar comenzar viaje de parte del conductor.
8. Agregar notificación al usuario de viaje iniciado.

### Desarrolladores:

* Diego Ivan Valadez Lozano A00817562 
* Carlos Ivan Cardenas Cardenas A00820062 
* Salvador Barboza A01187752 
* Ariel Méndez Santillan A01020690

### Profesor:

* Ing. Martha Sordia Salinas - msordia@itesm.mx

### Cliente:

No aplica.

### Institución

Este proyecto es hecho para la clase de Proyecto de Desarrollo para Dispositivos Móviles- Versión Android.

Universidad: ITESM
Campus: Campus Monterrey

### License

Copyright (C) 2019 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 
 TaxiUnico Android esta distribuido bajo la licencia Apache 2.0 [license](LICENSE).
