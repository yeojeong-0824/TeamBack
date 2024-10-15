package com.yeojeong.application.config.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExceptionMessage {
    private List<String> error;
}
