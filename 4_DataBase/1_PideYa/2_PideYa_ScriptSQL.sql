-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema PideYa
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `PideYa` ;

-- -----------------------------------------------------
-- Schema PideYa
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `PideYa` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci ;
USE `PideYa` ;

-- -----------------------------------------------------
-- Table `PideYa`.`Categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`Categoria` (
  `idCategoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NULL,
  PRIMARY KEY (`idCategoria`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`Producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`Producto` (
  `idProducto` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(200) NULL,
  `idCategoria` INT NOT NULL,
  `precio` DECIMAL(10,2) NULL,
  `stock` DECIMAL(10,2) NULL,
  PRIMARY KEY (`idProducto`),
  INDEX `fk_Producto_Categoria_idx` (`idCategoria` ASC) VISIBLE,
  CONSTRAINT `fk_Producto_Categoria`
    FOREIGN KEY (`idCategoria`)
    REFERENCES `PideYa`.`Categoria` (`idCategoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`Cliente` (
  `idCliente` INT NOT NULL AUTO_INCREMENT,
  `fechaCreacion` DATETIME NULL,
  PRIMARY KEY (`idCliente`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`ClienteEmpresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`ClienteEmpresa` (
  `idCliente` INT NOT NULL,
  `razonSocial` VARCHAR(200) NULL,
  `ruc` VARCHAR(45) NULL,
  PRIMARY KEY (`idCliente`),
  INDEX `fk_ClienteEmpresa_Cliente1_idx` (`idCliente` ASC) VISIBLE,
  UNIQUE INDEX `ruc_UNIQUE` (`ruc` ASC) VISIBLE,
  CONSTRAINT `fk_ClienteEmpresa_Cliente1`
    FOREIGN KEY (`idCliente`)
    REFERENCES `PideYa`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`ClienteNatural`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`ClienteNatural` (
  `idCliente` INT NOT NULL,
  `nombre` VARCHAR(200) NULL,
  `apellido` VARCHAR(200) NULL,
  `dni` INT UNSIGNED NULL,
  PRIMARY KEY (`idCliente`),
  INDEX `fk_ClienteNatural_Cliente1_idx` (`idCliente` ASC) VISIBLE,
  CONSTRAINT `fk_ClienteNatural_Cliente1`
    FOREIGN KEY (`idCliente`)
    REFERENCES `PideYa`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `idCliente` INT NOT NULL,
  `correo` VARCHAR(200) NULL,
  `clave` VARCHAR(2000) NULL,
  `tipoUsuario` VARCHAR(100) NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX `fk_Usuario_Cliente1_idx` (`idCliente` ASC) VISIBLE,
  UNIQUE INDEX `correo_UNIQUE` (`correo` ASC) VISIBLE,
  CONSTRAINT `fk_Usuario_Cliente1`
    FOREIGN KEY (`idCliente`)
    REFERENCES `PideYa`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`Pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`Pedido` (
  `idPedido` INT NOT NULL AUTO_INCREMENT,
  `idCliente` INT NOT NULL,
  `fechaPedido` DATETIME NULL,
  `montoTotal` DECIMAL(10,2) NULL,
  PRIMARY KEY (`idPedido`),
  INDEX `fk_Pedido_Cliente1_idx` (`idCliente` ASC) VISIBLE,
  CONSTRAINT `fk_Pedido_Cliente1`
    FOREIGN KEY (`idCliente`)
    REFERENCES `PideYa`.`Cliente` (`idCliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`DetallePedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`DetallePedido` (
  `idPedido` INT NOT NULL,
  `idProducto` INT NOT NULL,
  `precioUnitario` DECIMAL(10,2) NULL,
  `cantidad` DECIMAL(10,2) NULL,
  `subTotal` DECIMAL(10,2) NULL,
  PRIMARY KEY (`idPedido`, `idProducto`),
  INDEX `fk_DetallePedido_Pedido1_idx` (`idPedido` ASC) VISIBLE,
  INDEX `fk_DetallePedido_Producto1_idx` (`idProducto` ASC) VISIBLE,
  CONSTRAINT `fk_DetallePedido_Pedido1`
    FOREIGN KEY (`idPedido`)
    REFERENCES `PideYa`.`Pedido` (`idPedido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DetallePedido_Producto1`
    FOREIGN KEY (`idProducto`)
    REFERENCES `PideYa`.`Producto` (`idProducto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PideYa`.`Compra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `PideYa`.`Compra` (
  `idCompra` INT NOT NULL AUTO_INCREMENT,
  `idPedido` INT NOT NULL,
  `fechaCompra` DATETIME NULL,
  PRIMARY KEY (`idCompra`),
  INDEX `fk_Compra_Pedido1_idx` (`idPedido` ASC) VISIBLE,
  CONSTRAINT `fk_Compra_Pedido1`
    FOREIGN KEY (`idPedido`)
    REFERENCES `PideYa`.`Pedido` (`idPedido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Inserts para datos de ejemplo
-- -----------------------------------------------------

-- Categorías
INSERT INTO `PideYa`.`Categoria` (`nombre`) VALUES
  ('Electrónica'),
  ('Ropa'),
  ('Alimentos'),
  ('Hogar'),
  ('Deportes');

-- Productos (idCategoria 1–5 respectivamente)
INSERT INTO `PideYa`.`Producto` (`nombre`, `idCategoria`, `precio`, `stock`) VALUES
  ('Smartphone Galaxy S21',      1, 799.99, 30.00),
  ('Camiseta Polo Clásica',      2,  49.90,100.00),
  ('Arroz Integral 1 kg',         3,  12.50,200.00),
  ('Juego de Ollas 5 piezas',    4, 150.00, 20.00),
  ('Pelota de Fútbol Profesional',5,  59.99, 75.00);

-- Clientes (6 registros)
INSERT INTO `PideYa`.`Cliente` (`fechaCreacion`) VALUES
  ('2025-07-01 10:00:00'),
  ('2025-07-02 11:30:00'),
  ('2025-07-03 14:45:00'),
  ('2025-07-04 09:15:00'),
  ('2025-07-05 16:20:00'),
  ('2025-07-06 13:00:00');

-- Clientes Naturales (idCliente 1–3)
INSERT INTO `PideYa`.`ClienteNatural` (`idCliente`,`nombre`,`apellido`,`dni`) VALUES
  (1,'Juan','Pérez',12345678),
  (2,'María','González',23456789),
  (3,'Luis','Rodríguez',34567890);

-- Clientes Empresas (idCliente 4–6)
INSERT INTO `PideYa`.`ClienteEmpresa` (`idCliente`,`razonSocial`,`ruc`) VALUES
  (4,'Tech Solutions SAC','20123456789'),
  (5,'Comercial del Norte SAC','20111223344'),
  (6,'Global Services SAC','20555666778');

-- Usuarios para cada cliente
INSERT INTO `PideYa`.`Usuario` (`idCliente`,`correo`,`clave`,`tipoUsuario`) VALUES
  (1,'juan.perez@ulima.edu.pe','clave123','CLIENTE_NATURAL'),
  (2,'maria.gonzalez@ulima.edu.pe','clave456','CLIENTE_NATURAL'),
  (3,'luis.rodriguez@ulima.edu.pe','clave789','CLIENTE_NATURAL'),
  (4,'techsolutions@ulima.edu.pe','empresa123','CLIENTE_EMPRESA'),
  (5,'comercialnorte@ulima.edu.pe','empresa456','CLIENTE_EMPRESA'),
  (6,'globalservices@ulima.edu.pe','empresa789','CLIENTE_EMPRESA');

-- Cliente adicional Henry Wong (idCliente 7)
INSERT INTO `PideYa`.`Cliente` (`fechaCreacion`) VALUES
  ('2025-07-11 12:00:00');

INSERT INTO `PideYa`.`ClienteNatural` (`idCliente`,`nombre`,`apellido`,`dni`) VALUES
  (7,'Henry','Wong',11111111);

INSERT INTO `PideYa`.`Usuario` (`idCliente`,`correo`,`clave`,`tipoUsuario`) VALUES
  (7,'hwong@ulima.edu.pe','admin123','ADMINISTRADOR');


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
