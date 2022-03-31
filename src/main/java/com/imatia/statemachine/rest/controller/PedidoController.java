package com.imatia.statemachine.rest.controller;

import com.imatia.statemachine.entity.OrderTrackings;
import com.imatia.statemachine.service.PedidoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class PedidoController {

    public PedidoController(PedidoService service) {
        super();
        this.service = service;
    }
    private final PedidoService service;

    @PostMapping(path = "/order/tracking/", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OrderTrackings insertPedido(@RequestBody OrderTrackings pedidos) {
        service.getOrders(pedidos);
        return pedidos;
    }

}
