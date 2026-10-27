CREATE DATABASE reservas CHARACTER SET utf8mb4;
USE reservas;

CREATE TABLE usuario (
    id    VARCHAR(20) PRIMARY KEY,
    clave VARCHAR(50) NOT NULL,
    rol   ENUM('ADMIN','FUNCIONARIO') NOT NULL
);

CREATE TABLE funcionario (
    id       VARCHAR(20) PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE categoria (
    id          VARCHAR(20) PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE recurso (
     id           VARCHAR(20) PRIMARY KEY,
     descripcion  VARCHAR(100) NOT NULL,
     categoria_id VARCHAR(20) NOT NULL,
     FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE reserva (
    id             VARCHAR(20) PRIMARY KEY,
    actividad      VARCHAR(100) NOT NULL,
    funcionario_id VARCHAR(20) NOT NULL,
    fecha          DATE NOT NULL,
    hora_inicio    TIME NOT NULL,
    hora_fin       TIME NOT NULL,
    estado         VARCHAR(10) NOT NULL DEFAULT 'ACTIVA',
    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);

CREATE TABLE reserva_recurso (
    reserva_id VARCHAR(20) NOT NULL,
    recurso_id VARCHAR(20) NOT NULL,
    PRIMARY KEY (reserva_id, recurso_id),
    FOREIGN KEY (reserva_id) REFERENCES reserva(id) ON DELETE CASCADE,
    FOREIGN KEY (recurso_id) REFERENCES recurso(id)
);


INSERT INTO usuario (id, clave, rol) VALUES ('ADM-001', '123', 'ADMIN');
INSERT INTO usuario (id, clave, rol) VALUES ('FUN-001', 'FUN-001', 'FUNCIONARIO');
INSERT INTO funcionario (id, nombre, telefono) VALUES ('FUN-001', 'Lucia', '88888888');


INSERT INTO categoria (id, descripcion) VALUES ('CAT-000001', 'SALA DE EVENTOS');
INSERT INTO recurso (id, descripcion, categoria_id) VALUES ('REC-001', 'Sala principal', 'CAT-000001');