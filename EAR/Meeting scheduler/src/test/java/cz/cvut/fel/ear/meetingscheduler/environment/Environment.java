package cz.cvut.fel.ear.meetingscheduler.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.meetingscheduler.config.AppConfig;
import java.nio.charset.StandardCharsets;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;

public class Environment {

    private static ObjectMapper objectMapper;

    /**
     * Gets a Jackson object mapper for mapping JSON to Java and vice versa.
     *
     * @return Object mapper
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new AppConfig().objectMapper();
        }
        return objectMapper;
    }

    public static HttpMessageConverter<?> createDefaultMessageConverter() {
        return new MappingJackson2HttpMessageConverter(getObjectMapper());
    }

    public static HttpMessageConverter<?> createStringEncodingMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    /**
     * Clears current security context.
     */
    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
