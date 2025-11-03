package com.staylog.staylog.domain.testentry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class TestEntryService {

    // Simulate a database sequence for ttId
    private static final AtomicLong sequence = new AtomicLong(0);

    @Transactional
    public TestEntryResponse createTestEntry(TestEntryRequest request) {
        // In a real application, this would involve:
        // 1. Mapping request to an entity
        // 2. Saving the entity to the database
        // 3. Returning the generated ID

        // For this test, we'll simulate ID generation
        Long newTtId = sequence.incrementAndGet();

        // You could also log the request or store it in a simple in-memory map for debugging
        System.out.println("Simulating creation of TestEntry: " + request.getTestType() + " with ID: " + newTtId);

        return new TestEntryResponse(newTtId);
    }
}
