package com.cetys.josea.challenge;
import com.cetys.josea.challenge.services.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void saveValue_ShouldSaveValueToRedis() {
        String key = "product1";
        String value = "product details";

        redisService.saveValue(key, value);

        verify(valueOperations, times(1)).set(key, value);
    }

    @Test
    void getValue_ShouldReturnValueFromRedis() {
        String key = "product1";
        String expectedValue = "product details";

        when(valueOperations.get(key)).thenReturn(expectedValue);

        String actualValue = redisService.getValue(key);

        assertEquals(expectedValue, actualValue);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void deleteValue_ShouldDeleteValueFromRedis() {
        String key = "product1";

        redisService.deleteValue(key);

        verify(redisTemplate, times(1)).delete(key);
    }
}

