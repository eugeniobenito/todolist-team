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

### Test
Se han añadido los siguientes test: 
- *NavbarTest.java*: Método *registradosContainsNavbar* para comprobar que existe la barra de navegación en esta ruta
- *UsuarioServiceTest.java*: Se han creado 2 métodos, uno para revisar que el método findAll devuelve 0 resultados y otro para comprobar que devuelve el único usuario en la base de datos
- *RegistradosWebTest.java*: Se han creado 2 métodos. El primero comprueba que se muestra la vista y el segundo que se muestran los datos de los 2 usuarios añadidos.