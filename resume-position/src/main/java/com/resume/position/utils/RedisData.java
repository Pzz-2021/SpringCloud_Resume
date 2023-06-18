package com.resume.position.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisData implements Serializable{
    private Object data;
    private LocalDateTime expireTime;
}
