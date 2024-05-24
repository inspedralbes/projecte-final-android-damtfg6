# 📱 Projecte Final Android - DAMTFG6

## 👥 Membres de l'equip
- Albert Coral
- Jonathan Martin
- Àngel Camps

## 🌐 Repositori del Projecte
[GitHub - projecte-final-android-damtfg6](https://github.com/inspedralbes/projecte-final-android-damtfg6)

---

## 📝 Descripció del Projecte
Aquest projecte és una aplicació Android desenvolupada com a part del projecte final del curs de Desenvolupament d'Aplicacions Multiplataforma (DAM). L'aplicació té com a objectiu proporcionar funcionalitats específiques detallades en els següents apartats.

## 🚀 Funcionalitats Principals
- **Autenticació d'Usuaris:** Registre i inici de sessió per a usuaris.
- **Gestió de Contingut:** Creació, actualització i eliminació de continguts personalitzats per l'usuari.
- **Notificacions:** Enviament de notificacions push per mantenir els usuaris informats.
- **Interacció en Temps Real:** Comunicació entre usuaris mitjançant sockets per a funcionalitats en temps real.
- **Estadístiques i Rànquing:** Visualització de dades estadístiques i rànquing basats en l'activitat de l'usuari.

## 📂 Estructura del Projecte general
- **app:** Conté el codi de l'aplicació Android.
  - **src:** Directori principal del codi font.
  - **main:**
    - **java:**
      - **com.example.app:**
        - **MainActivity:** Classe principal que gestiona l'inici de l'aplicació i la navegació principal.
        - **LoginActivity:** Classe per a la gestió del registre i inici de sessió dels usuaris.
        - **DashboardActivity:** Classe que mostra el tauler de control amb les estadístiques i funcionalitats principals.
      - **com.example.app.adapters:**
        - **UserAdapter:** Adaptador per gestionar la visualització dels usuaris en llistes.
        - **ContentAdapter:** Adaptador per gestionar la visualització del contingut creat pels usuaris.
      - **com.example.app.models:**
        - **User:** Classe que defineix l'estructura de dades per a un usuari.
        - **Content:** Classe que defineix l'estructura de dades per al contingut creat per l'usuari.
      - **com.example.app.services:**
        - **NotificationService:** Servei per gestionar l'enviament de notificacions push.
        - **RealTimeService:** Servei per gestionar la comunicació en temps real utilitzant Socket.IO.
      - **com.example.app.utils:**
        - **FirebaseUtils:** Utilitats per a la integració amb Firebase (autenticació, base de dades).
        - **NetworkUtils:** Utilitats per a la gestió de connexions de xarxa i peticions HTTP.
    - **res:** Recursos de l'aplicació, com ara dissenys, imatges, etc.
- **gradle:** Scripts de configuració per al sistema de construcció Gradle.
- **.idea:** Configuració específica de l'IDE.

## 🔧 Tecnologies Utilitzades
- **Java:** Llenguatge principal de programació per al desenvolupament de l'aplicació Android.
- **XML:** Utilitzat per als dissenys d'interfície d'usuari.
- **Firebase:** Per a l'autenticació, base de dades en temps real i notificacions push.
- **Socket.IO:** Per a la comunicació en temps real.

## 🚀 Començar
Per començar amb el projecte, seguiu aquests passos:

1. Clonar el repositori amb la comanda `git clone https://github.com/inspedralbes/projecte-final-android-damtfg6.git`.
2. Obrir el projecte a Android Studio.
3. Instal·lar les dependències utilitzant Gradle.
4. Configurar Firebase amb les credencials necessàries.
5. Executar l'aplicació en un emulador o dispositiu Android real.

## 🛡️ Seguretat
Aquest projecte no té una política de seguretat específica encara. Si descobreixes alguna vulnerabilitat, si us plau obre una issue al repositori per informar-nos.

---

## 📞 Contacte
Per qualsevol dubte o comentari, si us plau contacta amb nosaltres a través del repositori de GitHub.

---

Pots trobar més informació i actualitzacions al repositori oficial: [projecte-final-android-damtfg6](https://github.com/inspedralbes/projecte-final-android-damtfg6).
