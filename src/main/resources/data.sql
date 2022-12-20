/* Populate tables */
INSERT INTO usuarios (id, code, email, nombre, password, fecha_nacimiento, is_admin, blocked) VALUES('1', '32123', 'user@ua', 'Usuario Ejemplo', '123', '2001-02-10', false, false);
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');
