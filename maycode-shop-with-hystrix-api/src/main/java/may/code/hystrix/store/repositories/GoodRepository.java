package may.code.hystrix.store.repositories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import may.code.hystrix.store.entities.GoodEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GoodRepository {

    private static final List<GoodEntity> GOOD_ENTITIES = new ArrayList<>();

    public Long countAll() {
        return (long) GOOD_ENTITIES.size();
    }

    public List<GoodEntity> findAll() {
        return GOOD_ENTITIES;
    }

    public Optional<GoodEntity> findById(Long id) {

        return GOOD_ENTITIES
                .stream()
                .filter(goodDto -> Objects.equals(goodDto.getId(), id))
                .findFirst();
    }

    public void add(GoodEntity entity) {
        GOOD_ENTITIES.add(entity);
    }
}
