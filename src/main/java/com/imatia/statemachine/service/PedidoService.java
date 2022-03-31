package com.imatia.statemachine.service;

import com.imatia.statemachine.config.Estados;
import com.imatia.statemachine.config.Eventos;
import com.imatia.statemachine.config.Pedido;
import com.imatia.statemachine.config.PedidoRepository;
import com.imatia.statemachine.entity.OrderTrackings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PedidoService {

    @Autowired
    StateMachine<Estados, Eventos> stateMachine;
    private PedidoRepository pedidoRepository;

    //URL BD
    public static final String URL = "jdbc:h2:mem:test";

    //QUERYS
    public static final String SQL1 = "SELECT tracking_status_id AS Status FROM pedido WHERE order_id = '";
    public static final String SQL2 = "' ORDER BY actual_date DESC LIMIT 1";

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void getOrders(OrderTrackings orderTrackings){
        for(Pedido order : orderTrackings.getOrderTrackings()){

            Integer actual_status=0;
            try{
                String SQL = SQL1 + order.getOrderId() + SQL2;
                Connection connection = DriverManager.getConnection(URL,"sa","password");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);

                while (rs.next()) {
                    actual_status = Integer.parseInt(rs.getString("Status"));
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //FECHA DE GUARDADO EN BASE DE DATOS
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date currentDate = new Date();
            order.setActual_date(currentDate);

            //SI YA ESTA REGISTRADO EN LA BD
            if (actual_status != 0){
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