package com.wooyano.wooyanomonolithic.payment.presentation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.payment.application.PaymentService;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.ChangeReservationRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

  /*  private final PaymentListScheduler paymentListScheduler;
    private final BatchScheduler batchScheduler;*/
/*
  @PostMapping("/toss")
  public ResponseEntity requestTossPayment( @RequestBody @Valid PaymentDto paymentReqDto) {
      PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentReqDto.toEntity(), principal.getUsername()).toPaymentResDto();
      paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
      paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
      return ResponseEntity.ok().body(new SingleResponse<>(paymentResDto));
  }
*/



    @Operation(summary = "결제 승인",
          description = "토스 api로 통신")
    @GetMapping("/success")
    public BaseResponse<?> reservationApproveService( @RequestParam String paymentKey,
                                                      @RequestParam String orderId,
                                                      @RequestParam Long amount) {

        paymentService.approvePayment(paymentKey,orderId,amount);

        return new BaseResponse<>();
  }






    // 결제완료,취소된 데이터 저장
    @PostMapping("/save")
    public ResponseEntity<String> savePayment(@RequestBody PaymentRequest paymentRequest) {
        paymentService.savePayment(paymentRequest);
        return ResponseEntity.ok("결제 데이터 저장");
    }


    // 결제완료,취소 건들의 리스트 조회
    @GetMapping("/list")
    public List<PaymentResultResponse> getCompletedAndCancelledTransactions() {
        return paymentService.getPaymentsList();
    }

/*

    //결제 내역
    @GetMapping("/test")
    public String test() throws JsonProcessingException {
        paymentListScheduler.sendMonthlyPaymentEvent();
       return "Test";
    }

    //batch test
    @GetMapping("/batch")
    public void batchTest()  {
        //paymentService.batchTest();

        batchScheduler.runJob();

    }
*/


}
