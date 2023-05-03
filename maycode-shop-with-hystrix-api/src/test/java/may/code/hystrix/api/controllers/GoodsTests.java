package may.code.hystrix.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import may.code.hystrix.api.controllers.GoodsController;
import may.code.hystrix.api.dto.GoodDto;
import may.code.hystrix.store.entities.GoodEntity;
import may.code.hystrix.store.repositories.GoodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class GoodsTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    GoodRepository goodRepository;

    @Test
    void should_Pass_When_GetAllGoodsByController() throws Exception {

        long getAllGoodsFromControllerStartTime = System.currentTimeMillis();

        byte[] contentAsByteArray = mvc.perform(
                get(GoodsController.GET_ALL_GOODS)
                )
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        log.info("All goods fetched by %s millis (controller)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromControllerStartTime));

        List<GoodDto> goodDtoList = mapper.readValue(contentAsByteArray, new TypeReference<>() {});

        List<GoodEntity> goodEntities = goodRepository.findAll();

        long goodDtoItemsWithoutPriceCount = goodDtoList.stream()
                .map(GoodDto::getPriceInRubles)
                .filter(Objects::isNull)
                .count();

        Assertions.assertEquals(0, goodDtoItemsWithoutPriceCount);
        Assertions.assertNotEquals(0, goodDtoList.size());

//        Assertions.assertEquals(goodEntities.size(), goodDtoItemsWithoutPriceCount);
//        Assertions.assertEquals(goodEntities.size(), goodDtoList.size());

//        Iterator<GoodDto> goodDtoIterator = goodDtoList.iterator();
//        Iterator<GoodEntity> goodEntityIterator = goodEntities.iterator();
//
//        do {
//
//            boolean hasNextGoodDto = goodDtoIterator.hasNext();
//            boolean hasNextGoodEntity = goodEntityIterator.hasNext();
//
//            Assertions.assertEquals(hasNextGoodEntity, hasNextGoodDto);
//
//            if (!hasNextGoodEntity) {
//                break;
//            }
//
//            GoodDto nextGoodDto = goodDtoIterator.next();
//            GoodEntity goodEntity = goodEntityIterator.next();
//
//            Assertions.assertEquals(nextGoodDto.getId(), goodEntity.getId());
//
//        } while (true);
    }

    @Test
    void should_Pass_When_GetAllGoodsByRepository() {

        long getAllGoodsFromRepositoryStartTime = System.currentTimeMillis();

        List<GoodEntity> goods = goodRepository.findAll();

        log.info("All goods fetched by %s millis (repository)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromRepositoryStartTime));
    }
}

