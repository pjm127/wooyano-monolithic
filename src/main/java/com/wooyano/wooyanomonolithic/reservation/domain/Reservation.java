package com.wooyano.wooyanomonolithic.reservation.domain;

import com.wooyano.wooyanomonolithic.global.common.domain.BaseEntity;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationStateConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ReservationGoods reservationGoods;
    @Column(nullable = false, length = 50, name = "user_email")
    private String userEmail;
    @Column(nullable = false, name = "service_id")
    private Long serviceId;
    @Column(name = "worker_id")
    private Long workerId;
    @Column(nullable = false, name = "reservation_date")
    private LocalDate reservationDate;
    @Column(nullable = false, name = "service_start")
    private LocalTime serviceStart;
    @Column(nullable = false, name = "service_end")
    private LocalTime serviceEnd;
    @Column(nullable = false)
    @Convert(converter = ReservationStateConverter.class)
    private ReservationState reservationState;
    @Column(nullable = false, name = "payment_amount")
    private Integer paymentAmount;
    @Column(length = 50, name = "cancel_desc")
    private String cancelDesc;
    @Column(length = 100, name = "request")
    private String request;

    @Column(nullable = false, length = 30, name = "address")
    private String address;

    @Column(nullable = false, length = 30, name = "order_id")
    private String orderId;


    public void approveStatus(ReservationState status) {
        this.reservationState = status;
    }

    public static Reservation createReservation(ReservationGoods reservationGoods, String userEmail, Long serviceId, Long workerId,
                                                LocalDate reservationDate, LocalTime serviceStart, LocalTime serviceEnd,
                                                 Integer paymentAmount, String cancelDesc,
                                                String request, String address,String orderId) {
        return Reservation.builder()
                .reservationGoods(reservationGoods)
                .userEmail(userEmail)
                .serviceId(serviceId)
                .workerId(workerId)
                .reservationDate(reservationDate)
                .serviceStart(serviceStart)
                .serviceEnd(serviceEnd)
                .reservationState(ReservationState.PAYMENT_WAITING)
                .paymentAmount(paymentAmount)
                .cancelDesc(cancelDesc)
                .request(request)
                .address(address)
                .orderId(orderId)
                .build();
    }

    @Builder
    private Reservation(ReservationGoods reservationGoods, String userEmail, Long serviceId, Long workerId,
                       LocalDate reservationDate, LocalTime serviceStart, LocalTime serviceEnd,
                       ReservationState reservationState, Integer paymentAmount, String cancelDesc, String request,
                       String address,String orderId) {
        this.reservationGoods = reservationGoods;
        this.userEmail = userEmail;
        this.serviceId = serviceId;
        this.workerId = workerId;
        this.reservationDate = reservationDate;
        this.serviceStart = serviceStart;
        this.serviceEnd = serviceEnd;
        this.reservationState = reservationState;
        this.paymentAmount = paymentAmount;
        this.cancelDesc = cancelDesc;
        this.request = request;
        this.address = address;
        this.orderId = orderId;
    }


}
