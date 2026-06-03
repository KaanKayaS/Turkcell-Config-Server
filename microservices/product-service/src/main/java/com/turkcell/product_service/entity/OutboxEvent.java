package com.turkcell.product_service.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="outbox")
public class OutboxEvent {
    @Id
    private UUID id;
    private String aggregateType; // Product
    private String aggregateId; // ProductId , Aggregate -> İlgili nesne
    private String eventType; // TestEvent
    @Column(columnDefinition = "TEXT")
    private String payload; // JSON
    private String errorMessage; // Hata varsa, ne hatası var?
    private int retryCount; // Kaç kere denedim?

    private Instant createdAt; // Şu tarihte sıraya aldım
    private Instant processedAt; // Şu tarihte kafkaya gönderdim?

    @Enumerated(EnumType.STRING)
    private OutboxStatus status; // PENDING, SENT, FAILED
}


// OutboxStatus => 1,2,3
// OutboxStatus => PENDING,SENT,FAILED

// Validator -> 50 tane farklı import