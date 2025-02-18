package com.ratmir.spring.hogwardsartifactsonline.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Result {
    private boolean flag;
    private Integer code;
    private String message;
    private Object data;
}
