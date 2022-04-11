package com.imatia.statemachine.service;

import com.imatia.statemachine.config.Estados;
import com.imatia.statemachine.config.Eventos;
import com.imatia.statemachine.config.Pedido;
import com.imatia.statemachine.config.PedidoRepository;
import com.imatia.statemachine.entity.OrderTrackings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PedidoService {

    @Autowired
    StateMachine<Estados, Eventos> stateMachine;
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void getOrders(OrderTrackings orderTrackings){
        for(Pedido order : orderTrackings.getOrderTrackings()){

            Integer actual_status = pedidoRepository.findTrackingStatusId((int)order.getOrderId());

            //FECHA DE GUARDADO EN BASE DE DATOS
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date currentDate = new Date();
            order.setActual_date(currentDate);

            //SI YA ESTA REGISTRADO EN LA BD
            if (actual_status != null){
                //SI EL ESTADO ENTRANTE ES UNO DE LOS ESTADOS EXISTENTES Y TRANSITABLES
                if((order.getTrackingStatusId()==Estados.EN_REPARTO.getId() || order.getTrackingStatusId()==Estados.INCIDENCIA_EN_ENTREGA.getId() ||
                    order.getTrackingStatusId()==Estados.ENTREGADO.getId()) && actual_status!=Estados.ENTREGADO.getId()){

                    if(order.getTrackingStatusId()==Estados.EN_REPARTO.getId() && actual_status!=Estados.EN_REPARTO.getId() && actual_status!=Estados.ENTREGADO.getId()){
                        stateMachine.sendEvent(Eventos.REPARTO);
                    }else if(order.getTrackingStatusId()==Estados.INCIDENCIA_EN_ENTREGA.getId() && actual_status!=Estados.INCIDENCIA_EN_ENTREGA.getId() &&
                            actual_status!=Estados.ENTREGADO.getId()){
                        stateMachine.sendEvent(Eventos.INCIDENCIA);
                    }else if(order.getTrackingStatusId()==Estados.ENTREGADO.getId() && actual_status!=Estados.ENTREGADO.getId()){
                        stateMachine.sendEvent(Eventos.ENTREGADO);
                    }
                    order.setTrackingStatusId(stateMachine.getState().getId().getId());
                    pedidoRepository.save(order);

                }else{
                    System.out.println("Transición a estado no permitido o estado inexistente");
                }

            }else{
                //EL ORDER ID DE ESE PEDIDO NO EXISTE TODAVÍA EN EL SISTEMA
                pedidoRepository.save(order);
            }

        }

    }

}
