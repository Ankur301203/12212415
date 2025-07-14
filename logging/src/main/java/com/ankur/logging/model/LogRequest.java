package com.ankur.logging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogRequest {
    private String stack;
    private String level;

    @JsonProperty("package")
    private String packageName;

    private String message;
}

