package may.code.shop.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.shop.api.dto.GoodDto;
import may.code.shop.store.entities.GoodEntity;
import may.code.shop.store.repositories.GoodRepository;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class GoodDataInitializer {

    GoodRepository goodRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void initData() {

        long currentTimeMillis = System.currentTimeMillis();

        for (int i = 0; i < 2000; ++i) {

            goodRepository.add(
                    GoodEntity.builder()
                            .id(currentTimeMillis + i)
                            .name(UUID.randomUUID().toString())
                            .build()
            );
        }
    }
}
