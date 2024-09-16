-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS mi_base_datos;
USE mi_base_datos;

-- Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    userName VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(255),
    saldoPuntos INT DEFAULT 0,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla acciones
CREATE TABLE IF NOT EXISTS acciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    puntos INT
)

--TablaCanjes
CREATE TABLE IF NOT EXISTS canjes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    recompensa_id INT,
    fecha_canje TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (recompensa_id) REFERENCES recompensas(id)
);

--Tabla Acciones_Usuario
CREATE TABLE IF NOT EXISTS acciones_usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    accion_id BIGINT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (accion_id) REFERENCES acciones(id)
);

-- Tabla Recompensas
CREATE TABLE IF NOT EXISTS recompensas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    puntos_necesarios INT NOT NULL
);

---Triggers
DELIMITER //
CREATE TRIGGER verificar_saldo_antes_de_canjear
BEFORE INSERT ON canjes
FOR EACH ROW
BEGIN
    DECLARE saldo_actual INT;
    DECLARE puntos_necesarios INT;

    -- Consultar el saldo actual de puntos del usuario
    SELECT saldo_puntos INTO saldo_actual
    FROM usuarios
    WHERE id = NEW.usuario_id;

    -- Consultar los puntos necesarios para la recompensa
    SELECT puntos_necesarios INTO puntos_necesarios
    FROM recompensas
    WHERE id = NEW.recompensa_id;

    -- Verificar que el usuario tiene suficientes puntos
    IF saldo_actual < puntos_necesarios THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Saldo insuficiente de puntos para realizar el canje';
    END IF;
END //

CREATE TRIGGER actualizar_saldo_despues_de_canje
AFTER INSERT ON canjes
FOR EACH ROW
BEGIN
    DECLARE puntos_necesarios INT;

    -- Obtener los puntos necesarios para la recompensa canjeada
    SELECT puntos_necesarios INTO puntos_necesarios
    FROM recompensas
    WHERE id = NEW.recompensa_id;

    -- Restar los puntos canjeados del saldo del usuario
    UPDATE usuarios
    SET saldo_puntos = saldo_puntos - puntos_necesarios
    WHERE id = NEW.usuario_id;
END //

DELIMITER ;