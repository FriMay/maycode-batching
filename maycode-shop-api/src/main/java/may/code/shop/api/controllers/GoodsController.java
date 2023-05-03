package may.code.shop.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.shop.api.dto.GoodDto;
import may.code.shop.api.controllers.exceptions.NotFoundException;
import may.code.shop.api.factories.GoodDtoFactory;
import may.code.shop.store.repositories.GoodRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

        return goodRepository
                .findAll()
                .stream()
                .map(goodDtoFactory::makeDto)
                .toList();
    }

    @GetMapping(GET_GOOD)
    public GoodDto getGood(@PathVariable("good_id") Long goodId) {

        return goodRepository
                .findById(goodId)
                .map(goodDtoFactory::makeDto)
                .orElseThrow(() -> {
                    throw new NotFoundException("Good not found");
                });
    }
}
