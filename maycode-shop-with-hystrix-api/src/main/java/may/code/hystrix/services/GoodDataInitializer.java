package may.code.hystrix.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.hystrix.store.repositories.GoodRepository;
import may.code.hystrix.store.entities.GoodEntity;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
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
