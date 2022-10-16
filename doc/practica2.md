# 002 Menú de navegación
Para el menú de navegación se ha implementado una navbar de Bootstrap. Para hacerlo posible, se ha añadido lo siguiente:

### Vistas
1. **navbarDefault.html:** Contiene la barra de navegación para los usuarios que no se han logeado. En esta aparece el botón de registro, login y el de acerca de.
2. **navbarLogged.html:** Contiene la barra de navegación para usuarios que sí se han logeado. En esta se muestra el nombre del usuario, un botón hacia el perfil, un enlace a su lista de tareas, un botón de cerrar sesión y el de cuenta.
3. **navbar.html:** Es el archivo que contiene la lógica de la invocación. Si detecta que contiene la variable usuario mostrará navbarLogged si no navbarDefault. 
La condición es la siguiente:```th:if="${usuario == null}"```

Para invocar la barra de navegación de cualquier template será suficiente con insertar la template navbar.html.

```<div th:insert="navbar :: navbar"></div>```

Esto lo encontramos abajo de la etiqueta *body* de todas las páginas que contienen la vista de navegación.

### Controladores
Se ha pasado la variable usuario en el controlador del /about

### Tests
1. *NavbarTest.java :* Se han comprobado que las páginas que solicitaba la historia de usuario contienen la barra de navegación. Para ello se ha hecho uso del mock para hacer las peticiones y analizar el código html si contiene el div con id del navbar logged o default, o ninguno, según el caso.
>  this.mockMvc.perform(get(url))
.andExpect((content().string(allOf(containsString("<div id=\"navbar-menu\""),
containsString("<div id=\"logged-navbar\"")))));

# 003 Listado de usuarios
Para mostrar la vista de usuarios se añadido un nuevo controlador, una vista y un método en la clase usuarioService.
### Vistas
Se ha añadido la vista listadoRegistrados. En ella se incluye la barra de navegación. Para mostrar los usuarios se ha realizado una tabla y gracias a *Thymeleaf* y la instrucción
```th:each``` se ha modelado la información a través de la variable *usuarios* que recibe el modelo.

### Servicios
Se ha añadido el método *findAll* a la clase *UsuarioService*. Este método simplemente llama al método findAll del repositorio de la clase Usuario que implementa el framework y devuelve un objeto Iterable con todos los usuarios.

### Controladores
Se ha añadido la clase *UserController*, en ella se ha implementado el método 
``` GET /registrados```. El controlador hace uso del método UserService.findAll() y envía a la vista listaRegistrados los usuarios. Además si hay un usuario logeado, le envía la información a la vista también

### Tests
Se han añadido los siguientes test: 
- *NavbarTest.java*: Método *registradosContainsNavbar* para comprobar que existe la barra de navegación en esta ruta
- *UsuarioServiceTest.java*: Se han creado 2 métodos, uno para revisar que el método findAll devuelve 0 resultados y otro para comprobar que devuelve el único usuario en la base de datos
- *RegistradosWebTest.java*: Se han creado 2 métodos. El primero comprueba que se muestra la vista y el segundo que se muestran los datos de los 2 usuarios añadidos.

# 004 Listar detalles de usuario
Para listar los detalles del usuario se han añadido 1 vista y un controlador. Para linkear la página de /registrados con la de detalles se ha modificado la vista.

### Vistas
1. Se ha añadido la nueva vista *detallesUsuario* donde a partir de un usuario introducido en el modelo muestra el id, nombre, email, fecha y tareas de un usuario en concreto.
2. Se ha añadido un hipervínculo en la plantilla *listaRegistrados* en la tabla para linkear cada usuario a su página de detalles ```
   <a th:href="@{/registrados/{id}(id=${usuario.id})}">```

### Controladores
Se ha añadido a la clase *UserController.java* el método *detalles usuario* que responde a la petición ````GET /registrados/{id}````, donde obtiene el usuario por la id y envía a la plantilla toda la información en la variable usuarioDetalles. Si el usuario con ese id no existe, se producirá una excepción de tipo *UsuarioNotFoundException"

### Excepciones
Se ha creado la excepción *UsuarioNotFoundException* que devuelve un código de estado http 404 not found.

### Tests
Se han creado los siguientes test en la clase *RegistradosWebTest*:
1. Cuando no existe el usuario la respuesta es un mensaje 404
2. Se comprueba que de un usuario registrado se muestra toda la información excepto la contraseña, incluidas las tareas.

# 005 Usuario administrador
Se pretende crear un usuario administrador, que se pueda crear desde el registro y que al iniciar sesión se acceda a /registrados
### Modelo
El modelo Usuario ahora incorpora la variable booleana isAdmin
> private boolean isAdmin;

### Vistas
Se ha modificado la vista de *registroForm*, ya que a través del parámetro del modelo *existsAnyAdmin* mostrará o no el checkbox del formulario. ```` th:if="${!existsAnyAdmin}" ````
### Data
La clase RegistroData ha sido modificada. Ahora contiene un nuevo parámetro isAdmin, que será el que se vincule en la template con el checkbox. Además este template se ha inicializado a *false* debido de que si no encuentra en la plantilla la checkbox interprete que es falso.
### Controladores
- Ahora el controlador que controla el ````GET /registro````, hace uso del método de la clase *UsuarioService.existsAnyAdmin()*, y lo inyecta en la template.
- El controlador `````POST /registro````` obtiene el elemento isAdmin agregado al data del formulario y lo asocia al usuario.
- El controlador ````POST /login```` revisa si el usuario que se está logeando es administrador y si es así lo reedirige a ```/registrados```
### Servicios
La clase UsuarioService ahora incorpora el metodo existsAnyAdmin que devolverá *true* en caso de que exista un usuario con la variable isAdmin = true en la base de datos y false al contrario.
### Repositorio
La clase UsuarioRepository implementa un método findByIsAdmin que a partir del parámetro si es true o false devuelve una lista con usuarios con los cuales cumple la condición. Este método sirve para ser llamado desde el UsuarioService y comprobar existen usuarios con el isAdmin true.
### Tests
- *UsuarioTest.java:* Se añaden 2 métodos donde comprueban el método de UserRepository.findByIsAdmin devuelve una lista vacía o con el usuario según si existe un usuario administrador en base de datos
- *UsuarioServiceTest.java:* Se añade 1 método para comprobar que el método existsAnyAdmin devuelve lo esperado
- *RegistroWebTest.java:* Se crea la clase y se añaden dos test para revisar si el checkbox se oculta cuando existe un administrador. Esto se hace gracias al texto del checkbox ``El usuario será administrador``
- *UsuarioWebTest.java*: Se añade un método para comprobar que se hace la reedirección a la página ````/registrados```` si el usuario que se logea es administrador.
