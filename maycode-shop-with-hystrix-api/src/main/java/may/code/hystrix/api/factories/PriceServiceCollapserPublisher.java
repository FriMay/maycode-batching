package may.code.hystrix.api.factories;

import com.netflix.hystrix.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.hystrix.services.PriceServiceCollapser;
import may.code.hystrix.services.remote.PriceServiceApi;
import may.code.hystrix.store.entities.GoodEntity;
import org.springframework.stereotype.Component;
import rx.Observable;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PriceServiceCollapserPublisher {

    PriceServiceApi priceServiceApi;

    public Observable<Long> subscribe(GoodEntity entity) {

        return new PriceServiceCollapser(
                entity.getId(),
                priceServiceApi,
                HystrixCollapser.Setter
                        .withCollapserKey(() -> "getAllPriceInRublesForGoodsCollapserKey")
                        .andScope(HystrixCollapser.Scope.GLOBAL)
                        .andCollapserPropertiesDefaults(
                                HystrixCollapserProperties
                                        .defaultSetter()
                                        .withMaxRequestsInBatch(25)
                        ),
                HystrixCommand.Setter
                        .withGroupKey(() -> "getAllPriceInRublesForGoodsGroupKey")
                        .andCommandKey(() -> "getAllPriceInRublesForGoodsCommandKey")
                        .andThreadPoolKey(() -> "PriceService")
                        .andThreadPoolPropertiesDefaults(
                                HystrixThreadPoolProperties.defaultSetter()
                                        .withCoreSize(3)
                                        .withMaximumSize(3)
                                        .withKeepAliveTimeMinutes(0)
                                        .withMaxQueueSize(20000)
                                        .withQueueSizeRejectionThreshold(20000)
                        )
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.defaultSetter()
                                        .withExecutionTimeoutEnabled(true)
                                        .withExecutionTimeoutInMilliseconds(500)
                        )
        ).observe();
    }
}
