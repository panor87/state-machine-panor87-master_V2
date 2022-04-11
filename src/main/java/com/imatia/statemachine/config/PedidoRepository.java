package com.imatia.statemachine.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(value = "SELECT tracking_status_id AS Status FROM pedido WHERE order_id = ?1 ORDER BY actual_date DESC LIMIT 1", nativeQuery = true)
    Integer findTrackingStatusId(int id);
}