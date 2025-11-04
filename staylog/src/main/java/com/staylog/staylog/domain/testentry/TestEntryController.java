package com.staylog.staylog.domain.testentry;

import com.staylog.staylog.global.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestEntryController {

    private final TestEntryService testEntryService;

    @PostMapping("/create-entry")
    public ResponseEntity<SuccessResponse<TestEntryResponse>> createTestEntry(
            @RequestBody TestEntryRequest request) {
        TestEntryResponse response = testEntryService.createTestEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(SuccessResponse.of("CREATED", "Test entry created successfully", response));
    }
}
