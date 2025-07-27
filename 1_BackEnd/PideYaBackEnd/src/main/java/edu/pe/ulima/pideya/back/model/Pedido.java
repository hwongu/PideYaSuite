/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.pe.ulima.pideya.back.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author hwong
 */
public class Pedido {

    private Integer idPedido;
    private LocalDateTime fechaPedido;
    private Double montoTotal;
    private List<DetallePedido> detallePedido;
    private Cliente cliente;

    public Pedido() {
    }

    public Pedido(Integer idPedido, LocalDateTime fechaPedido, Double montoTotal, Cliente cliente) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.montoTotal = montoTotal;
        this.cliente = cliente;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetallePedido> getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(List<DetallePedido> detallePedido) {
        this.detallePedido = detallePedido;
    }
    
    

}
