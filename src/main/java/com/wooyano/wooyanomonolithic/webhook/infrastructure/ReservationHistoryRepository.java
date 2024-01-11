package com.wooyano.wooyanomonolithic.webhook.infrastructure;

import com.wooyano.wooyanomonolithic.webhook.domain.ReservationHistoryWebhook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistoryWebhook,Long> {
}
