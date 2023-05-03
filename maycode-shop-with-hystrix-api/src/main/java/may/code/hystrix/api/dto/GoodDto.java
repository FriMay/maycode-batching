package may.code.hystrix.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoodDto {
    
    Long id;
    
    String name;

    @JsonProperty("price_in_rubles")
    Long priceInRubles;
}
