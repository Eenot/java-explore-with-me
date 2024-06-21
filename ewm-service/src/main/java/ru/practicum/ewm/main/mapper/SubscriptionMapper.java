package ru.practicum.ewm.main.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.main.model.SubscriptionEntity;
import ru.practicum.ewm.main.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {

    public static SubscriptionEntity toSubscription(SubscriptionDto subscriptionDto, List<User> users) {
        return SubscriptionEntity.builder()
                .isSubscribed(subscriptionDto.getIsSub())
                .timestamp(subscriptionDto.getTimestamp())
                .initiator(users.get(0))
                .subscriber(users.get(1))
                .build();
    }

    public static SubscriptionDto toSubscriptionDto(SubscriptionEntity subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .subId(subscription.getSubscriber().getId())
                .initId(subscription.getInitiator().getId())
                .isSub(subscription.isSubscribed())
                .timestamp(subscription.getTimestamp())
                .build();
    }

    public static SubscriptionEntity toSubscriptionUpdate(SubscriptionDto subscriptionDto, Long subId, SubscriptionEntity subFromDb) {
        return SubscriptionEntity.builder()
                .id(subId)
                .isSubscribed(subscriptionDto.getIsSub())
                .timestamp(subscriptionDto.getTimestamp())
                .subscriber(subFromDb.getSubscriber())
                .initiator(subFromDb.getInitiator())
                .build();
    }

}
