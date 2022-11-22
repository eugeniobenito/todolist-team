# Docker Hub
``` 
https://hub.docker.com/r/sergioaluua/mads-todolist-equipo15
```
# 012 Gestión de equipos por administradores
En esta historia se pretende que los administradores de la aplicación puedan eliminar a usuarios de un grupo. Anteriormente, para echar a un usuario era necesario eliminar el grupo entero, con esto no haría falta.
### Controladores
- *EquipoController*: Se ha actualizado el middleware `comprobarUsuarioLogeado`. Su antigua función era revisar que el id del usuario logeado coincidia con el parámetro que se le pasaba. Se utiliza en las funciones donde se accede a recursos de los usuarios, de esta forma comprobamos que si no es el mismo lanzamos una excepción con código `HTTP 401 Unauthorized`. Ahora si el usuario logeado es administrador no lanzamos la excepción y le permitimos acceder al recurso.
```java
private void comprobarUsuarioLogeado(Long idUsuario) {
    Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
    if (!managerUserSession.isUsuarioLogeado())
        throw new UsuarioNoLogeadoException();
    Long idUser = managerUserSession.usuarioLogeado();
    Usuario u = usuarioService.findById(idUser);
    if(!u.getIsAdmin()) {
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }
}
```

De esta forma conseeguimos que el administrador pueda acceder al controlador `DELETE /equipos/{equipoId}/usuarios/{usuarioId}` que elimina el usuario del equipo.

### Vistas
- *detallesEquipo*: Se ha añadido en la vista un botón solo si el administrador está logeado para cada usuario de la tabla. Esto se hace gracias al código de Thymeleaf
```html
<td th:if="${usuario.isAdmin}">
    <button class="btn btn-danger btn-xs" onmouseover="" style="cursor: pointer;"
            th:onclick="'del(\'/equipos/' + ${equipo.id} + '/usuarios/' + ${usuarioEquipo.id} + '\')'">Eliminar del equipo</button>
</td>
```

La vista quedaría para usuarios sería así:
![](img/detallesUser.png)
Y para administradores:
![](img/detallesAdmin.png)
