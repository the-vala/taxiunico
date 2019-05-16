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
* Versión: 1.0 - Fecha de última actualización: 24/04/2019
* Versión: 2.0 - Fecha de última actualización: 26/04/2019
* Versión: 3.0 - Fecha de última actualización: 14/05/2019


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

## Modificaciones y caracteristicas v0.1
1. Agregar integración con Firebase.
2. Agregar ventana para agregar un metodo de pago.
3. Agregar ventana de perfil de usuario.
4. Agregar ventana de encuesta de satisfación. 
5. Agregar ventana de registro e iniciar sesión.
6. Agregar metodo de recuperar contraseña.

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
