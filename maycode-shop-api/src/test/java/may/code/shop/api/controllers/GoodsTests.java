package may.code.shop.api.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import may.code.shop.store.entities.GoodEntity;
import may.code.shop.store.repositories.GoodRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest
@AutoConfigureMockMvc
class GoodsTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    GoodRepository goodRepository;

//    @Test
    void should_Pass_When_GetAllGoodsByController() throws Exception {

        Long goodsCount = goodRepository.countAll();

        long getAllGoodsFromControllerStartTime = System.currentTimeMillis();

        mvc.perform(
                get(GoodsController.GET_ALL_GOODS)
        )
                .andExpect(jsonPath("$.size()").value(goodsCount));

        log.info("All goods fetched by %s millis (controller)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromControllerStartTime));
    }

    @Test
    void should_Pass_When_GetAllGoodsByControllerV1() throws Exception {

        Long goodsCount = goodRepository.countAll();

        long getAllGoodsFromControllerStartTime = System.currentTimeMillis();

        mvc.perform(
                get(GoodsControllerV1.GET_ALL_GOODS)
        )
                .andExpect(jsonPath("$.size()").value(goodsCount));

        log.info("All goods fetched by %s millis (controller v1)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromControllerStartTime));
    }

    @Test
    void should_Pass_When_GetAllGoodsByRepository() {

        long getAllGoodsFromRepositoryStartTime = System.currentTimeMillis();

        List<GoodEntity> goods = goodRepository.findAll();

        log.info("All goods fetched by %s millis (repository)."
                .formatted(System.currentTimeMillis() - getAllGoodsFromRepositoryStartTime));
    }
}

