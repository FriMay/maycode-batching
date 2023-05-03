package may.code.shop.services.remote;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import may.code.shop.api.controllers.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PriceServiceApi {

    private static final Map<Long, Long> GOOD_ID_TO_PRICE_IN_RUBLES = new ConcurrentHashMap<>();

    @Value("${may.code.goods.price.fetch-size-limit:25}")
    @NonFinal Integer fetchSizeLimit;

    public Map<Long, Long> getGoodIdToPriceInRublesByGoodIds(List<Long> goodIds) {

        if (goodIds.size() > fetchSizeLimit) {
            throw new BadRequestException("Good ids list too large for request processing.");
        }

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) { }

        Map<Long, Long> goodIdToPriceInRubles = new HashMap<>();
        for (Long goodId: goodIds) {
            goodIdToPriceInRubles.put(goodId, calculatePriceInRubles(goodId));
        }

        return goodIdToPriceInRubles;
    }

    public Long getPriceInRublesByGoodId(Long goodId) {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) { }

        return calculatePriceInRubles(goodId);
    }

    private Long calculatePriceInRubles(Long goodId) {

        return GOOD_ID_TO_PRICE_IN_RUBLES.computeIfAbsent(
                goodId,
                it -> new Random().nextLong(10, 40)
        );
    }
}
