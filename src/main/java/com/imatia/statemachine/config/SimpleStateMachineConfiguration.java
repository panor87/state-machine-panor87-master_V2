package com.imatia.statemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import javax.swing.*;
import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class SimpleStateMachineConfiguration
        extends EnumStateMachineConfigurerAdapter<Estados, Eventos> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<Estados, Eventos> config) throws Exception {

        config
                .withConfiguration()
                .autoStartup(true) //la máquina de estados se iniciará automáticamente al correr la aplicación
                .listener(listener()); //Un listener (escuchador) que ocurrirá en cada cambio de estado.
    }

    @Override
    public void configure(StateMachineStateConfigurer<Estados, Eventos> states)
            throws Exception {

        states
                .withStates()
                .initial(Estados.RECOGIDO_EN_ALMACÉN)
                .end(Estados.ENTREGADO)
                .states(EnumSet.allOf(Estados.class));

    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<Estados, Eventos> transitions)
            throws Exception {

        transitions.withExternal()
                .source(Estados.RECOGIDO_EN_ALMACÉN).target(Estados.EN_REPARTO).event(Eventos.REPARTO).and()
                .withExternal()
                .source(Estados.EN_REPARTO).target(Estados.ENTREGADO).event(Eventos.ENTREGADO).and()
                .withExternal()
                .source(Estados.EN_REPARTO).target(Estados.INCIDENCIA_EN_ENTREGA).event(Eventos.INCIDENCIA).and()
                .withExternal()
                .source(Estados.INCIDENCIA_EN_ENTREGA).target(Estados.ENTREGADO).event(Eventos.ENTREGADO).and()
                .withExternal()
                .source(Estados.RECOGIDO_EN_ALMACÉN).target(Estados.INCIDENCIA_EN_ENTREGA).event(Eventos.INCIDENCIA);
    }

    @Bean
    public StateMachineListener<Estados, Eventos> listener() {
        return new StateMachineListenerAdapter<Estados, Eventos>() {
            @Override
            public void stateChanged(State<Estados, Eventos> from, State<Estados, Eventos> to) {
                System.out.println(to.getId());
            }
        };
    }

}
