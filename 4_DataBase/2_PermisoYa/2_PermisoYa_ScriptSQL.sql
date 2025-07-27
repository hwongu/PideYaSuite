-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, 
    SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema PermisoYa
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `PermisoYa`;
CREATE SCHEMA IF NOT EXISTS `PermisoYa`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_spanish_ci;
USE `PermisoYa`;

-- -----------------------------------------------------
-- Table `PermisoYa`.`MenuItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `MenuItem` (
  `idMenuItem` INT NOT NULL AUTO_INCREMENT,
  `nombre`    VARCHAR(100) NOT NULL,
  `parentId`  INT NULL,
  `orden`     INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idMenuItem`),
  INDEX (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- -----------------------------------------------------
-- Table `PermisoYa`.`Role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`idRole`),
  UNIQUE INDEX (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- -----------------------------------------------------
-- Table `PermisoYa`.`Role_Menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Role_Menu` (
  `idRole`     INT NOT NULL,
  `idMenuItem` INT NOT NULL,
  PRIMARY KEY (`idRole`,`idMenuItem`),
  INDEX (`idRole`),
  INDEX (`idMenuItem`),
  CONSTRAINT `fk_rm_role`
    FOREIGN KEY (`idRole`)
    REFERENCES `Role` (`idRole`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_rm_menuitem`
    FOREIGN KEY (`idMenuItem`)
    REFERENCES `MenuItem` (`idMenuItem`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- -----------------------------------------------------
-- Poblar Roles
-- -----------------------------------------------------
INSERT INTO `Role` (`nombre`) VALUES
  ('CLIENTE_NATURAL'),
  ('CLIENTE_EMPRESA'),
  ('ADMINISTRADOR');

-- -----------------------------------------------------
-- Poblar Menús principales usando nombres de variables
-- -----------------------------------------------------
INSERT INTO `MenuItem` (`nombre`,`parentId`,`orden`) VALUES
  ('jmGestionarAlmacen',           NULL, 1),
  ('jmGestionarCliente',           NULL, 2),
  ('jmGestionarPedido',            NULL, 3),
  ('jmGestionarPedidoClientes',    NULL, 4),
  ('jfileAyuda',                   NULL, 5);

-- -----------------------------------------------------
-- Capturar IDs de padres en variables
-- -----------------------------------------------------
SET @jmGestionarAlmacen    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmGestionarAlmacen' LIMIT 1);
SET @jmGestionarCliente    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmGestionarCliente' LIMIT 1);
SET @jmGestionarPedido     = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmGestionarPedido' LIMIT 1);
SET @jmGestionarPedidoCli  = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmGestionarPedidoClientes' LIMIT 1);
SET @jfileAyuda            = (SELECT idMenuItem FROM MenuItem WHERE nombre='jfileAyuda' LIMIT 1);

-- -----------------------------------------------------
-- Poblar submenús con nombres de variables
-- -----------------------------------------------------
INSERT INTO `MenuItem` (`nombre`,`parentId`,`orden`) VALUES
  -- Gestionar Almacen hijos
  ('jmiMantCategoria',        @jmGestionarAlmacen, 1),
  ('jmiMantProducto',         @jmGestionarAlmacen, 2),

  -- Gestionar Cliente hijos
  ('jmiMantClienteNatural',   @jmGestionarCliente, 1),
  ('jmiMantClienteEmpresa',   @jmGestionarCliente, 2),

  -- Gestionar Pedido hijos
  ('jmiRealPedido',           @jmGestionarPedido, 1),
  ('jmiRealizarPago',         @jmGestionarPedido, 2),
  ('jmiImprimirCompras',      @jmGestionarPedido, 3),

  -- Gestionar Pedido Clientes hijos
  ('jmiRealizarPedidoCliente',        @jmGestionarPedidoCli, 1),
  ('jmiRealizarPagoPedidoCliente',    @jmGestionarPedidoCli, 2),
  ('jmiImprimirCompraPedidoCliente',  @jmGestionarPedidoCli, 3),

  -- Ayuda hijo
  ('jmiAcercaDe',             @jfileAyuda, 1);

-- -----------------------------------------------------
-- Capturar IDs de submenús en variables
-- -----------------------------------------------------
SET @roleNat     = (SELECT idRole     FROM Role     WHERE nombre='CLIENTE_NATURAL' LIMIT 1);
SET @roleEmp     = (SELECT idRole     FROM Role     WHERE nombre='CLIENTE_EMPRESA'  LIMIT 1);
SET @roleAdmin   = (SELECT idRole     FROM Role     WHERE nombre='ADMINISTRADOR'     LIMIT 1);

SET @gmPedidoCli = @jmGestionarPedidoCli;
SET @rpPedidoCli = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiRealizarPedidoCliente'       LIMIT 1);
SET @rpagoCli    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiRealizarPagoPedidoCliente'   LIMIT 1);
SET @impCli      = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiImprimirCompraPedidoCliente' LIMIT 1);
SET @ayuda       = @jfileAyuda;
SET @acercade    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiAcercaDe'                      LIMIT 1);

SET @gmAlm      = @jmGestionarAlmacen;
SET @mantCat    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiMantCategoria'      LIMIT 1);
SET @mantProd   = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiMantProducto'       LIMIT 1);

SET @gmCliente  = @jmGestionarCliente;
SET @mantCNat   = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiMantClienteNatural'  LIMIT 1);
SET @mantCEmp   = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiMantClienteEmpresa'  LIMIT 1);

SET @gmPedido   = @jmGestionarPedido;
SET @realPed    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiRealPedido'            LIMIT 1);
SET @pagoPed    = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiRealizarPago'         LIMIT 1);
SET @impCompras = (SELECT idMenuItem FROM MenuItem WHERE nombre='jmiImprimirCompras'      LIMIT 1);

-- -----------------------------------------------------
-- Role_Menu para CLIENTE_NATURAL
-- -----------------------------------------------------
INSERT INTO `Role_Menu` (`idRole`,`idMenuItem`) VALUES
  (@roleNat, @gmPedidoCli),
  (@roleNat, @rpPedidoCli),
  (@roleNat, @rpagoCli),
  (@roleNat, @impCli),
  (@roleNat, @ayuda),
  (@roleNat, @acercade);

-- -----------------------------------------------------
-- Role_Menu para CLIENTE_EMPRESA
-- -----------------------------------------------------
INSERT INTO `Role_Menu` (`idRole`,`idMenuItem`) VALUES
  (@roleEmp, @gmPedidoCli),
  (@roleEmp, @rpPedidoCli),
  (@roleEmp, @rpagoCli),
  (@roleEmp, @impCli),
  (@roleEmp, @ayuda),
  (@roleEmp, @acercade);

-- -----------------------------------------------------
-- Role_Menu para ADMINISTRADOR (todos menos Pedido Clientes)
-- -----------------------------------------------------
INSERT INTO `Role_Menu` (`idRole`,`idMenuItem`) VALUES
  -- Almacen
  (@roleAdmin, @gmAlm),
  (@roleAdmin, @mantCat),
  (@roleAdmin, @mantProd),

  -- Cliente
  (@roleAdmin, @gmCliente),
  (@roleAdmin, @mantCNat),
  (@roleAdmin, @mantCEmp),

  -- Pedido
  (@roleAdmin, @gmPedido),
  (@roleAdmin, @realPed),
  (@roleAdmin, @pagoPed),
  (@roleAdmin, @impCompras),

  -- Ayuda
  (@roleAdmin, @ayuda),
  (@roleAdmin, @acercade);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
