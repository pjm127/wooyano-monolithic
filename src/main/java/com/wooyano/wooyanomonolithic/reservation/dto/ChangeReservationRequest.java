package com.wooyano.wooyanomonolithic.reservation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChangeReservationRequest {

    private String orderId; //주문번호


}
