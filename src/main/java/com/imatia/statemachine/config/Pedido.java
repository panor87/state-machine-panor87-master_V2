package com.imatia.statemachine.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Date;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private long orderId;
    private long trackingStatusId;
    private OffsetDateTime changeStatusDate;

    private Date actual_date;

    public Pedido() {
    }

    public Pedido(long orderId, long trackingStatusId, OffsetDateTime changeStatusDate) {
        this.orderId = orderId;
        this.trackingStatusId = trackingStatusId;
        this.changeStatusDate = changeStatusDate;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTrackingStatusId() {
        return trackingStatusId;
    }

    public void setTrackingStatusId(long trackingStatusId) {
        this.trackingStatusId = trackingStatusId;
    }

    public OffsetDateTime getChangeStatusDate() {
        return changeStatusDate;
    }

    public void setChangeStatusDate(OffsetDateTime changeStatusDate) {
        this.changeStatusDate = changeStatusDate;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public Date getActual_date() {return actual_date;}

    public void setActual_date(Date actual_date) {this.actual_date = actual_date;}



}
