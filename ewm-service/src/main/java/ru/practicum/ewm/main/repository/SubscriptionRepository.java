package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.model.SubscriptionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("SELECT s FROM Subscription s WHERE s.subscriber.id = :subId AND initiator.id = :initId " +
            "AND s.isSubscribed = :isSub")
    Optional<SubscriptionEntity> findSubscriptionByIds(long initId, long subId, boolean isSub);

    @Query("SELECT s FROM Subscription s WHERE s.subscriber.id = :subId AND s.isSubscribed = true")
    Optional<List<SubscriptionEntity>> findSubscriptionsBySubscriberId(long subId);
}
