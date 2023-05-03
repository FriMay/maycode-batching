package may.code.hystrix.api.factories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.hystrix.api.dto.GoodDto;
import may.code.hystrix.store.entities.GoodEntity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GoodDtoFactory {

    PriceServiceCollapserPublisher priceServiceCollapserPublisher;

    public GoodDto makeDto(GoodEntity entity) throws ExecutionException, InterruptedException {

        return makeDto(
                entity,
                priceServiceCollapserPublisher.subscribe(entity).toBlocking().toFuture().get()
        );
    }

    public List<GoodDto> makeDtoList(List<GoodEntity> entities) {

        List<GoodDto> goodDtoList = new ArrayList<>();

        List<Observable<Long>> observables = new ArrayList<>();
        for (GoodEntity entity: entities) {

            GoodDto goodDto = makeBaseDto(entity);
            goodDtoList.add(goodDto);

            Observable<Long> observable = priceServiceCollapserPublisher.subscribe(entity);

            observables.add(observable);

            // TODO: Если для товара не была возвращена цена - нужно что-то с этим делать
            observable.subscribe(goodDto::setPriceInRubles, e -> {});
        }

        observables.forEach(observable -> {
            try {
                observable.toBlocking().toFuture().get();
            } catch (Exception ignored) { }
        });

        return goodDtoList
                .stream()
                .filter(goodDto -> Objects.nonNull(goodDto.getPriceInRubles()))
                .toList();
    }

    private GoodDto makeDto(GoodEntity entity, Long priceInRubles) {

        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .priceInRubles(priceInRubles)
                .build();
    }

    private GoodDto makeBaseDto(GoodEntity entity) {

        return GoodDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
