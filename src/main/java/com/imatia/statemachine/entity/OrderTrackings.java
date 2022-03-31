package com.imatia.statemachine.entity;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import com.imatia.statemachine.config.Pedido;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement()
public class OrderTrackings {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "orderTracking")
    private List<Pedido> orderTrackings = new ArrayList<>();

    public OrderTrackings() {
    }

    public OrderTrackings(List<Pedido> orderTrackings) {
        this.orderTrackings = orderTrackings;
    }

    public List<Pedido> getOrderTrackings() {
        return orderTrackings;
    }

    public void setOrderTrackings(List<Pedido> orderTrackings) {
        this.orderTrackings = orderTrackings;
    }
}
