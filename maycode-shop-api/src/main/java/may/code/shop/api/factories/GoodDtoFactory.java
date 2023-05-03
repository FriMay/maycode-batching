package may.code.shop.api.factories;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.shop.api.dto.GoodDto;
import may.code.shop.services.PriceService;
import may.code.shop.store.entities.GoodEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GoodDtoFactory {

    PriceService priceService;

    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

    public GoodDto makeDto(GoodEntity entity) {

        return makeDto(entity, priceService.getPriceInRubles(entity));
    }

    public List<GoodDto> makeDtoList(List<GoodEntity> entities) {

        Map<Long, Long> goodIdToPriceInRublesMap = new HashMap<>();

        Lists
                .partition(entities, 25)
                .forEach(entitiesBatch ->
                        goodIdToPriceInRublesMap.putAll(
                                priceService.getGoodIdToPriceInRublesMap(entitiesBatch)
                        )
                );

        return entities
                .stream()
                .map(entity -> makeDto(entity, goodIdToPriceInRublesMap.get(entity.getId())))
                .toList();
    }

    private GoodDto makeDto(GoodEntity entity, Long priceInRubles) {

        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priceInRubles(priceInRubles)
                .build();
    }
}
