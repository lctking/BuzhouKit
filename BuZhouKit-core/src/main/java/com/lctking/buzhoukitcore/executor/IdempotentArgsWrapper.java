package com.lctking.buzhoukitcore.executor;

import com.lctking.buzhoukitcore.annotation.Idempotent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;
import lombok.Data;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IdempotentArgsWrapper {
    private ProceedingJoinPoint joinPoint;

    private Idempotent idempotent;

    private String keyForLock;
}
