package may.code.hystrix.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.hystrix.api.controllers.exceptions.InternalServerErrorException;
import may.code.hystrix.api.dto.GoodDto;
import may.code.hystrix.api.factories.GoodDtoFactory;
import may.code.hystrix.store.repositories.GoodRepository;
import may.code.hystrix.api.controllers.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class GoodsController {

    GoodRepository goodRepository;

    GoodDtoFactory goodDtoFactory;

    public static final String GET_ALL_GOODS = "/api/goods";
    public static final String GET_GOOD = "/api/goods/{good_id}";

    @GetMapping(GET_ALL_GOODS)
    public List<GoodDto> getAllGoods() {
        return goodDtoFactory.makeDtoList(goodRepository.findAll());
    }

    @GetMapping(GET_GOOD)
    public GoodDto getGood(@PathVariable("good_id") Long goodId) {

        return goodRepository
                .findById(goodId)
                .map(entity -> {
                    try {
                        return goodDtoFactory.makeDto(entity);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new InternalServerErrorException(e.getMessage());
                    }
                })
                .orElseThrow(() -> {
                    throw new NotFoundException("Good not found");
                });
    }
}
