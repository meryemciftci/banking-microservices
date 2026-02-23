package com.banking.transfer.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "FRAUD-DETECTION-SERVICE")
public interface FraudServiceClient {

    @PostMapping("/api/v1/fraud/check")
    ResponseEntity<Map<String, Boolean>> checkFraud(@RequestBody FraudCheckRequest request);
}