package com.wooyano.wooyanomonolithic.reservation.domain;

import com.wooyano.wooyanomonolithic.global.common.domain.BaseEntity;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationStateConverter;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationItem> reservationItems = new ArrayList<>();
    @Column(nullable = false, length = 50, name = "user_email")
    private String userEmail;
    @Column(nullable = false, name = "service_id")
    private Long serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;
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
    private int totalPrice;
    @Column(length = 50, name = "cancel_desc")
    private String cancelDesc;
    @Column(length = 100, name = "request")
    private String request;

    @Column(nullable = false, length = 30, name = "address")
    private String address;

    @Column(nullable = false, length = 30, name = "order_id")
    private String orderId;

    private LocalDateTime paymentApprovedAt;


    public void approveStatus(ReservationState status) {
        this.reservationState = status;
    }

    public static Reservation createReservation(List<ReservationGoods> reservationGoods, String userEmail, Long serviceId,
                                                Worker worker, LocalDate reservationDate, LocalTime serviceStart,LocalTime serviceEnd,
                                                int totalPrice, String cancelDesc,
                                                String request, String address,String orderId,LocalDateTime paymentApprovedAt) {
        return Reservation.builder()
                .reservationGoods(reservationGoods)
                .userEmail(userEmail)
                .serviceId(serviceId)
                .worker(worker)
                .reservationDate(reservationDate)
                .serviceStart(serviceStart)
                .serviceEnd(serviceEnd)
                .reservationState(ReservationState.WAIT)
                .totalPrice(totalPrice)
                .cancelDesc(cancelDesc)
                .request(request)
                .address(address)
                .orderId(orderId)
                .paymentApprovedAt(paymentApprovedAt)
                .build();
    }

    @Builder
    private Reservation(List<ReservationGoods> reservationGoods, String userEmail, Long serviceId, Worker worker,
                       LocalDate reservationDate, LocalTime serviceStart,LocalTime serviceEnd,
                       ReservationState reservationState, int totalPrice, String cancelDesc, String request,
                       String address,String orderId,LocalDateTime paymentApprovedAt) {
        this.reservationItems = reservationGoods.stream().map(item-> new ReservationItem(this,item)).collect(Collectors.toList());
        this.userEmail = userEmail;
        this.serviceId = serviceId;
        this.worker = worker;
        this.reservationDate = reservationDate;
        this.serviceStart = serviceStart;
        this.serviceEnd = serviceEnd;
        this.reservationState = reservationState;
        this.totalPrice = totalPrice;
        this.cancelDesc = cancelDesc;
        this.request = request;
        this.address = address;
        this.orderId = orderId;
        this.paymentApprovedAt = paymentApprovedAt;
    }


}
