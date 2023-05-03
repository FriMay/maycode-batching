package may.code.hystrix.services;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import may.code.hystrix.services.remote.PriceServiceApi;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceServiceCollapser extends HystrixCollapser<List<Long>, Long, Long> {

    Long goodId;

    PriceServiceApi priceServiceApi;

    HystrixCommand.Setter commandSetter;

    public PriceServiceCollapser(
            Long goodId,
            PriceServiceApi priceServiceApi,
            HystrixCollapser.Setter collapserSetter,
            HystrixCommand.Setter commandSetter) {

        super(collapserSetter);

        this.goodId = goodId;
        this.priceServiceApi = priceServiceApi;
        this.commandSetter = commandSetter;
    }

    @Override
    public Long getRequestArgument() {
        return goodId;
    }

    @Override
    protected HystrixCommand<List<Long>> createCommand(
            Collection<CollapsedRequest<Long, Long>> collapsedRequests) {

        return new HystrixCommand<>(commandSetter) {

            @Override
            protected List<Long> run() {

                return priceServiceApi.getPriceInRublesByGoodIds(
                        collapsedRequests.stream()
                                .map(CollapsedRequest::getArgument)
                                .toList()
                );
            }
        };
    }

    @Override
    protected void mapResponseToRequests(List<Long> batchResponse,
                                         Collection<CollapsedRequest<Long, Long>> collapsedRequests) {

        if (batchResponse.size() != collapsedRequests.size()) {
            throw new RuntimeException("Responses count not matches with initial requests size.");
        }

        Iterator<CollapsedRequest<Long, Long>> collapsedRequestIterator = collapsedRequests.iterator();

        for (Long priceInRubles: batchResponse) {

            collapsedRequestIterator
                    .next()
                    .setResponse(priceInRubles);
        }
    }
}
