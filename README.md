
# Índice

1. [⚙ Instalación y Configuración de Git en Windows](#⚙instalación-y-configuración-de-git-en-windows)
   - [Descarga e instalación](#descarga-e-instalación)
   - [Verificación de la instalación](#verificación-de-la-instalación)
   - [Configuración de Git Localmente](#configuración-de-git-localmente)
     - [Configurar tu nombre y correo electrónico en Git](#configurar-tu-nombre-y-correo-electrónico-en-git)

2. [👤 Gestión de licencias en GitLab](#👤-gestión-de-licencias-en-gitlab)
   - [Gestión de ingreso al grupo de seguridad de Active Directory](#gestión-de-ingreso-al-grupo-de-seguridad-de-active-directory)
   - [Crear cuenta en GitLab SaaS](#crear-cuenta-en-gitlab-saas)
   - [Ingreso al namespace `smg-code`](#ingreso-al-namespace-smg-code)
   - [Solicitar permisos en repositorios](#solicitar-permisos-en-repositorios)

3. [📑 Protocolo de Identificación en GitLab](#📑-protocolo-de-identificación-en-gitlab)
   - [Configuración del perfil](#configuración-del-perfil)
   - [Puntos importantes a tener en cuenta](#puntos-importantes-a-tener-en-cuenta)
   - [Configuración Correcta de Usuarios AD y GitLab](#configuración-correcta-de-usuarios-ad-y-gitlab)
   - [Consideraciones Finales](#consideraciones-finales)

4. [🛠 Arreglo de commits y push](#🛠-arreglo-de-commits-y-push)
   - [Configurar tu Nombre y Correo Electrónico en Git](#configurar-tu-nombre-y-correo-electrónico-en-git)
   - [Modificar el Autor de Uno o Varios Commits](#modificar-el-autor-de-uno-o-varios-commits)
   - [Identificar y Resolver Posibles Errores](#identificar-y-resolver-posibles-errores)
   - [Buenas Prácticas](#buenas-prácticas)

5. [🌳 Gitflow - Metodología de Trabajo](#🌳-gitflow---metodología-de-trabajo)
   - [Workflow del Sprint](#workflow-del-sprint)
   - [Workflow de Incidentes](#workflow-de-incidentes)
   - [Resumen del Flujo](#resumen-del-flujo)

6. [🔑 Configuración de Claves SSH en GitLab](#🔑-configuración-de-claves-ssh-en-gitlab)
   - [¿Qué son las claves SSH?](#qué-son-las-claves-ssh)
   - [¿Cómo funciona?](#cómo-funciona)
   - [Beneficios 🔒](#beneficios-🔒)
   - [Configuración de una Clave SSH en GitLab](#configuración-de-una-clave-ssh-en-gitlab)


# ⚙Instalación y Configuración de Git en Windows

## Descarga e instalación

1. Dirígete a la página oficial de Git: [Descargar Git para Windows](https://git-scm.com/download/win) y descarga el paquete de instalación.
2. También podés optar por instalar Git desde la terminal ejecutando el siguiente comando:
   ```sh
   winget install -id Git.Git
   ```
3. El archivo de instalación se descargará automáticamente. Una vez descargado, haz doble clic en el archivo `.exe` para iniciar el proceso de instalación.
4. En el instalador, selecciona las opciones predeterminadas o configura las opciones según tus necesidades.
5. Completa la instalación y cierra el instalador.

## Verificación de la instalación

1. Abre la terminal de Git (Git Bash) o la línea de comandos de Windows (cmd).
2. Ejecuta el siguiente comando para verificar que Git se haya instalado correctamente:
   ```sh
   git --version
   ```
3. Si la instalación fue exitosa, deberías ver una salida similar a:
   ```sh
   git version X.X.X
   ```

## Configuración de Git Localmente

Una vez que Git esté instalado, es necesario configurarlo correctamente para que tus commits sean firmados adecuadamente y coincidan con tu cuenta de GitLab.

### Configurar tu nombre y correo electrónico en Git

Es importante que el nombre completo de tu cuenta en GitLab ("Full Name") coincida con el `user.name` configurado en Git. Esto evitará problemas al hacer `push` de tus cambios.

1. Abre la terminal de Git (Git Bash) y ejecuta los siguientes comandos para configurar tu nombre y correo electrónico globalmente (esto se aplicará a todos tus repositorios):

   ```sh
   git config --global user.name "Tu Nombre y Apellido"
   git config --global user.email "tu.correo@swissmedical.com.ar"
   ```

   > **Nota:** Asegúrate de usar el mismo nombre y correo electrónico que tienes registrado en tu cuenta de GitLab.

2. Verifica que los cambios han sido aplicados correctamente ejecutando:

   ```sh
   git config --global --list
   ```

   Esto debería mostrar tu nombre y correo configurados correctamente.

   > **Nota:** Verifica que no haya espacios innecesarios. GitLab es sensible a mayúsculas y minúsculas.

# 👤 Gestión de licencias en GitLab

## 1. Gestión de ingreso al grupo de seguridad de Active Directory

1. Se debe solicitar el ingreso al grupo de seguridad `GitLab Corporativo` mediante la creación de un ticket en CAU. Accedé al siguiente enlace: [CAU](https://sdmx.swissmedical.com.ar/casm.html#/bui/home).
2. En el buscador, ingresá "grupo de seguridad" y presioná Enter.
3. Seleccioná la opción "Modificación de integrantes".
4. Ingresá el nombre del grupo de seguridad AD: `GitLab Corporativo`.
5. Seleccioná "Agregar integrantes".
6. Ingresá el nombre de usuario de AD asignado por la compañía.
7. El CAU solicitará la aprobación del owner del grupo de seguridad (Walter Vera) vía email.
8. Una vez aprobada la solicitud, el acceso estará habilitado.

## 2. Crear cuenta en GitLab SaaS

1. Ingresá a [GitLab Sign Up](https://gitlab.com/users/sign_up).
2. Completá el formulario de registro siguiendo el protocolo de identificación dentro del namespace.
3. Ingresá una contraseña (no necesariamente la de AD).
4. Confirmá la creación de la cuenta a través del email recibido.
5. Se recomienda activar el 2FA con una aplicación de autenticación de confianza.

## 3. Ingreso al namespace `smg-code`

1. Si la solicitud fue aprobada, ya serás miembro del namespace `smg-code`.
2. Autenticate en el namespace ingresando con tus credenciales SAML SSO.
3. Hacé clic en "Sign in" e ingresá tus credenciales de AD.
4. Si tenés 2FA activado, ingresá el código generado.
5. Si aparece un error 404, ya estarás autenticado en el namespace, pero necesitás solicitar acceso a los repositorios.

## 4. Solicitar permisos en repositorios

1. Para comprobar si estás en el grupo de seguridad AD `GitLab Corporativo`, ejecutá en terminal:
   ```sh
   net user userAD /domain
   ```
2. Ingresá a la solicitud `walle` y completá el formulario.
3. En el Paso 1:
   - Título sugerido: `GitLab - Alta de usuario`.
   - Seleccioná la unidad de negocio.
   - Tarea: `Git`.
   - Hacé clic en "Siguiente".
4. En el Paso 2:
   - Ingresá el enlace a la tarea de Jira (obligatorio).
   - Ingresá la(s) ruta(s) de los repositorios completas.
   - Ingresá el/los emails corporativos.
   - Ingresá el tipo de permisos:
     - **Guest**: Solo visibilidad.
     - **Reporter**: Para seguimiento sin contribución de código.
     - **Developer**: Permite contribuciones de código.
     - **Maintainer**: Administración y aprobaciones de código.

# 📑Protocolo de Identificación en GitLab

### Configuración del perfil

1. Iniciar sesión en GitLab.
2. Hacer clic en la imagen de perfil (arriba a la izquierda).
3. Seleccionar "Edit profile".
4. Ir a la sección "Full name" e ingresar el nombre y apellido (respetando mayúsculas y minúsculas).
5. Ir a la sección "Account" e ingresar el `username`.
   - ⚠ El `username` debe coincidir con el de AD.
   - Si está tomado, agregar `.smg` al final (Ejemplo: usuarioAD.smg).
6. En la sección "Profile", actualizar el avatar con una foto profesional.

### Puntos importantes a tener en cuenta

- 🔡 **Coincidencia exacta**: El `username` de GitLab debe coincidir con el de AD en mayúsculas y minúsculas.
- 🚫 **Evitar apodos o modificaciones**: No usar apodos, números o puntos adicionales.
- ➕ **En caso de conflicto**: Si el `username` ya está en uso, agregar `.smg` al final.
  - **Ejemplo correcto**: `juperez.smg`

A continuación, se mostrarán ejemplos prácticos de errores comunes al colocar el `username`.

### Configuración Correcta de Usuarios AD y GitLab

Para garantizar la correcta integración con Azure AD SAML en GitLab, los nombres de usuario deben estar configurados de manera consistente entre ambas plataformas. A continuación, se presentan ejemplos de configuraciones correctas e incorrectas.

### Ejemplos Prácticos:

#### Usuario AD: `juperez`
**GitLab:** `JuPerez`

❌ **Mal configurado:** Contiene mayúsculas que no están en AD.

---

#### Usuario AD: `juperez`
**GitLab:** `juperez99`

❌ **Mal configurado:** Contiene números que no están en AD.

---

#### Usuario AD: `juperez`
**GitLab:** `ju.perez`

❌ **Mal configurado:** Contiene un punto que no está en AD.

---

#### Usuario AD: `juperez`
**GitLab:** `juperez`

✅ **Correcto:** AD y GitLab coinciden exactamente.

---

#### Usuario AD: `juperez`
**GitLab:** `juperez.smg`

✅ **Correcto:** Este formato es válido si el username ya está tomado.

---

#### Usuario AD: `juperez`
**GitLab:** `ElLocoPerez`

❌ **Mal configurado:** No corresponden sobrenombres o apodos.

---

### Consideraciones Finales
- Se recomienda que el nombre de usuario en GitLab sea idéntico al de AD.
- Si el username ya está tomado en GitLab, se puede agregar un sufijo corporativo (ej. `.smg`).
- No se deben utilizar mayúsculas, números adicionales, puntos o caracteres especiales que no existan en AD.
- No se permiten sobrenombres, apodos o nombres creativos en la configuración oficial.

Siguiendo estas pautas, se asegura una integración fluida y sin inconvenientes. ✅

# 🛠Arreglo de commits y push

Este instructivo te guiará paso a paso para:
- Configurar correctamente tu nombre y correo electrónico en Git.
- Modificar el autor de uno o varios commits existentes.
- Identificar y resolver posibles errores relacionados con las push rules configuradas en el repositorio.

## 1. Configurar tu Nombre y Correo Electrónico en Git
Es fundamental que el nombre completo de tu cuenta en GitLab ("Full Name") coincida con el `user.name` configurado en Git. Esto evitará problemas al realizar push de tus cambios.

### Configuración Global
Abre la terminal de Git (Git Bash) y ejecuta los siguientes comandos para configurar tu nombre y correo electrónico globalmente (esto se aplicará a todos tus repositorios):

```sh
 git config --global user.name "Tu Nombre y Apellido"
 git config --global user.email "tu.correo@swissmedical.com.ar"
```

**Nota:** Asegúrate de usar el mismo nombre y correo electrónico que tienes registrado en tu cuenta de GitLab.

### Configuración Local (Opcional)
Si deseas configurar un nombre y correo electrónico específicos para un repositorio, usa:

```sh
 git config --local user.name "Tu Nombre y Apellido"
 git config --local user.email "tu.correo@swissmedical.com.ar"
```

---

## 2. Modificar el Autor de Uno o Varios Commits
Si necesitas corregir el autor de commits anteriores, sigue estos pasos:

### Cambiar el Autor de un Solo Commit
1. Identifica el hash del commit que deseas modificar:

```sh
 git log
```

2. Modifica el autor usando `--amend`:

```sh
 git commit --amend --author="Nuevo Nombre <nuevo.correo@swissmedical.com.ar>"
```

3. Realiza un push con fuerza para actualizar el historial remoto:

```sh
 git push --force
```

### Cambiar el Autor de Múltiples Commits
1. Usa `git rebase` para reescribir el historial:

```sh
 git rebase -i HEAD~N  # Reemplaza N con la cantidad de commits a modificar
```

2. En el editor que se abre, cambia `pick` por `edit` en los commits a modificar.

3. Para cada commit seleccionado, ejecuta:

```sh
 git commit --amend --author="Nuevo Nombre <nuevo.correo@swissmedical.com.ar>"
 git rebase --continue
```

4. Realiza un push con fuerza:

```sh
 git push --force
```

---

## 3. Identificar y Resolver Posibles Errores

### Errores Comunes

#### 1. **Reject unverified users**
- **Mensaje:** "The committer email is not verified."
- **Solución:** Verifica que el correo electrónico configurado en Git esté registrado y verificado en GitLab:
  - Ve a *Settings > Emails* en tu cuenta de GitLab.
  - Agrega y verifica el correo electrónico si es necesario.

#### 2. **Reject inconsistent username**
- **Mensaje:** "The commit author name is inconsistent with your GitLab account name."
- **Solución:** Asegúrate de que el `user.name` en Git coincida exactamente con el nombre configurado en GitLab. Reconfigura tu nombre:

```sh
 git config --global user.name "Tu Nombre y Apellido"
```

#### 3. **Check whether the commit author is a GitLab user**
- **Mensaje:** "Commit author is not a valid GitLab user."
- **Solución:** Verifica que el correo electrónico del autor esté asociado a una cuenta de GitLab existente.

---

## 4. Buenas Prácticas
✅ Realiza un `git status` frecuentemente para confirmar que tus configuraciones son correctas.

✅ Utiliza `git log --pretty=full` para inspeccionar detalles completos de los commits.

🚫 Evita realizar `push --force` en ramas compartidas sin previo aviso al equipo.

---
Con esta guía, podés asegurarte de mantener una configuración correcta y resolver problemas comunes relacionados con las push rules del repositorio. ¡Buena suerte! 🚀

# 🌳 Gitflow - Metodología de Trabajo

## Workflow del Sprint

Este documento describe el flujo de trabajo en Git utilizado en el sprint para gestionar el desarrollo, pruebas y despliegue de código de manera eficiente.

### 1️⃣ Rama Feature (Desarrollo de Funcionalidades)
- Se crea una nueva rama desde `Develop` para trabajar en una funcionalidad específica.
- Se realizan cambios y pruebas locales.
- Una vez que la funcionalidad es estable, se fusiona con `Develop`.

### 2️⃣ Rama Develop (Integración y Pruebas Unitarias)
- Aquí se integran los cambios mediante **Squash commits** para mantener un historial limpio.
- Se realizan pruebas unitarias.
- Si se encuentran errores, se hace un **Revert Commit** y se corrige.
- Cuando todo está estable, se realiza el **deploy en DEV y QA**.

### 3️⃣ Rama Release (Preparación para Producción)
- Cuando `Develop` está estable, se fusiona en `Release` mediante **Squash commit**.
- Se realizan pruebas de aceptación.
- Si todo está correcto, se hace el **deploy en Pre** para una validación final.

### 4️⃣ Rama Main (Producción)
- Una vez validado en `Pre`, se fusiona en `Main` con un **Squash commit**.
- Se genera un **tag con la fecha** (`AAAAMMDD`) para marcar la versión en producción.
- Se implementa en **Producción** ✅.

## 📍 Workflow de Incidentes

### 1️⃣ Inicio del Hotfix (Corrección de Incidente)
- Se crea una rama desde `Main` con la descripción del incidente en la sección `HotFix/Descripción`.
- Se realiza un **Squash Commit** para consolidar los cambios antes de integrarlos nuevamente en `Main`.

### 2️⃣ Pruebas y Validaciones
- Se genera un **tag** con el formato `AAAAMMDD` (año, mes, día), indicando la implementación en un entorno de preproducción (`Pre`).
- Se ejecutan **pruebas de aceptación**.
- **Si hay errores**, se sigue trabajando en la rama de Hotfix y se vuelven a realizar commits hasta que todo esté corregido.

### 3️⃣ Integración a Producción
- Una vez aprobado el Hotfix, se realiza un **Git rebase** para integrar los cambios de la rama `HotFix/Descripción` en `Release`.
- Se etiqueta nuevamente (`AAAAMMDD`), indicando que la versión ha sido implementada en producción (`Prod`).
- Se hace otro **Squash Commit** para consolidar el historial y mejorar la trazabilidad.

### 4️⃣ Publicación de una Versión Estable
- Una vez corregidos los incidentes y validados los cambios, se fusionan en la rama `Release`, creando una versión estable del software.

## 📍 Resumen del Flujo
1. Se desarrolla en `Feature` y se fusiona en `Develop`.
2. Se realizan pruebas y, si es estable, se despliega en `DEV y QA`.
3. Se fusiona en `Release`, se prueban los cambios y se despliega en `Pre`.
4. Si todo está validado, se fusiona en `Main` y se despliega en Producción.
5. En caso de incidentes, se sigue el **Workflow de Incidentes**, garantizando correcciones rápidas y seguras.

Este flujo permite mantener un código controlado y estable en cada etapa antes de llegar a producción 🚀.



# 🔑Configuración de Claves SSH en GitLab

## ¿Qué son las claves SSH?
Las claves SSH (Secure Shell) son una forma segura de autenticar y acceder a repositorios Git sin necesidad de ingresar usuario y contraseña cada vez que se contribuye o clona un repositorio. En el contexto de GitLab, estas claves permiten una conexión segura y automatizada entre el equipo local y el repositorio en GitLab.

## ¿Cómo funciona?
### Generación de Claves
Cada usuario genera un par de claves SSH (una pública y una privada) en su dispositivo.

### Clave Pública
La clave pública se agrega a tu perfil en GitLab. Esto autoriza a tu dispositivo a interactuar con el repositorio sin contraseña, ya que GitLab identifica tu clave y otorga acceso.

### Clave Privada
Queda almacenada de forma segura en tu dispositivo y nunca debe compartirse. Esta clave privada es la que permite a tu dispositivo autenticarse en GitLab.

## Beneficios 🔒
- **Mayor Seguridad**: Autenticación sin contraseña, reduciendo el riesgo de acceso no autorizado.
- **Comodidad**: Acceso directo al repositorio sin tener que ingresar credenciales cada vez.
- **Eficiencia**: Facilita operaciones de clonado, push y pull en repositorios sin interrupciones.

## Configuración de una Clave SSH en GitLab
Explicaremos paso a paso cómo crear una clave SSH y configurarla en GitLab.

### 1. Abrir la Terminal
Abre la terminal de tu sistema operativo.

### 2. Generar una Nueva Clave SSH
Ejecuta el siguiente comando en la terminal:
```sh
ssh-keygen -t rsa -b 4096 -C "tu.correo@swissmedical.com.ar"
```
**Explicación:**
- `-t rsa`: Especifica el tipo de clave (RSA).
- `-b 4096`: Indica el número de bits en la clave (4096 es más seguro que 2048).
- `-C "tu.correo@swissmedical.com.ar"`: Comentario opcional (correo electrónico).

### 3. Especificar la Ruta y Nombre del Archivo
Se te pedirá la ubicación donde se guardará la clave. Puedes presionar **Enter** para aceptar la ubicación predeterminada `~/.ssh/id_rsa`.

### 4. Configurar una Contraseña (opcional)
Puedes establecer una contraseña para mayor seguridad o simplemente presionar **Enter** dos veces para omitirla.

### 5. Verificar la Creación de la Clave
Para comprobar que la clave pública se generó correctamente, ejecuta:
```sh
cat ~/.ssh/id_rsa.pub
```

### 6. Copiar la Clave Pública
Copia el contenido de tu clave pública al portapapeles.

### 7. Añadir la Clave Pública a GitLab
1. Inicia sesión en tu cuenta de GitLab.
2. Ve a tu perfil y haz clic en **Preferences** (esquina superior izquierda).
3. Selecciona **SSH Keys** en el menú de la izquierda.
4. Haz clic en **Add new key**.
5. Pega tu clave pública en el campo de texto.
6. Añade un título para identificar la clave (por ejemplo, "Mi laptop personal").
7. Haz clic en **Add key**.

### 8. Probar la Conexión
Para asegurarte de que todo está funcionando correctamente, prueba la conexión SSH con:
```sh
ssh -T git@gitlab.com
```
Si todo está bien, deberías ver un mensaje similar a:
```sh
Welcome to GitLab, @tu_usuario!
```

## 🌟 ¡Listo! Ahora puedes trabajar con GitLab de manera segura y sin contraseñas. 🌟

