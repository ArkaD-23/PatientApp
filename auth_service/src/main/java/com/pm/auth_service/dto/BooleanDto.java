package com.pm.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BooleanDto {

    @JsonProperty("status")
    private Boolean status;
}
