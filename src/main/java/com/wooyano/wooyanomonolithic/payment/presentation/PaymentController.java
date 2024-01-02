package com.wooyano.wooyanomonolithic.payment.presentation;


import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.payment.application.PaymentService;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
 /*   @PostMapping("/toss")
    public ResponseEntity requestTossPayment( @RequestBody PaymentDto paymentReqDto) {
        PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentReqDto.toEntity(), principal.getUsername()).toPaymentResDto();
        paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
        paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
        return ResponseEntity.ok().body(new SingleResponse<>(paymentResDto));
    }
*/
    // 결제 요청 데이터 임시 저장
    @PostMapping("/request")
    public BaseResponse<?> requeste(@RequestBody PaymentRequest paymentRequest) {

        paymentService.savePayment(paymentRequest);

        //return ResponseEntity.ok(paymentResponse);
        return new BaseResponse<>();
    }




    @Operation(summary = "결제 승인",
          description = "토스 api로 통신")
    @GetMapping("/success")
    public BaseResponse<?> reservationApproveService(  @RequestParam(name = "serviceId") Long serviceId,
                                                       @RequestParam(name = "workerId") Long workerId,
                                                       @RequestParam(name = "userEmail") String userEmail,
                                                       @RequestParam(name = "reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDate,
                                                       @RequestParam(name = "request") String request,
                                                       @RequestParam(name = "address") String address,
                                                       @RequestParam(name = "clientEmail") String clientEmail,
                                                       @RequestParam(name = "orderId") String orderId,
                                                       @RequestParam(name = "paymentKey") String paymentKey,
                                                       @RequestParam(name = "amount") int amount,
                                                       @RequestParam(name = "serviceStart") @DateTimeFormat(pattern = "HH:mm") LocalTime serviceStart,
                                                       @RequestParam(name = "reservationGoodsId") List<Long> reservationGoodsId) {

        PaymentResponse paymentResponse = paymentService.approvePayment(paymentKey, orderId, amount,
                serviceId, workerId, userEmail, reservationDate, request, address, clientEmail, serviceStart,reservationGoodsId);

        //return ResponseEntity.ok(paymentResponse);
        return new BaseResponse<>(paymentResponse);
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
