# TaxiUnico Android
Aplicación móvil que permitirá al	usuario	conectar por medio de una	aplicación móvil a los pasajeros de	autobús Transpais	y	a	los conductores de vehículos registrados en nuestro	servicio de Taxi Único quienes desean	ofrecer	un servicio	de autotransporte.

## Equipo
Diego Ivan Valadez Lozano A00817562
Carlos Ivan Cardenas Cardenas A00820062
Salvador Barboza A01187752
Ariel Méndez A01020690

## Build Instructions

1. Clone or Download the repository:

  ```shell
  git clone https://github.com/the-vala/taxiunico.git
  ```

2. Import the project into Android Studio **or** build on the command line:

  ```shell
  ./gradlew clean app:assembleArmDebug
  ```
3. Define your Google Api key (follow next instructions).

##  Defining Google Api Key key in build.gradle
Find the .gradle folder in home directory.

* Windows: C:\Users\<Your Username>\.gradle
* Mac: /Users/<Your Username>/.gradle
* Linux: /home/<Your Username>/.gradle

Inside it there would be a file gradle.properties, create it if there isn’t any.

After that, add your key to the file as a property. Example:

Google_ApiKey ="my-awesome-api-key"

## License
TaxiUnico Android esta distribuido bajo la licencia Apache 2.0 [license](LICENSE).
