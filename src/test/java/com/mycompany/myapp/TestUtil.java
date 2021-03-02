package com.mycompany.myapp;

import static com.mycompany.myapp.config.Constants.DATE_TIME_FORMAT;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Set;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtil {

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
        .ofPattern(DATE_TIME_FORMAT)
        .withZone(ZoneId.of("UTC"));

    public static String formatDateTime(Temporal temporal) {
        return DATE_TIME_FORMATTER.format(temporal);
    }

    /**
     * Verifies the equals/hashcode contract on the domain object.
     */
    public static <T> void equalsVerifier(Class<T> clazz) throws Exception {
        T domainObject1 = clazz.getConstructor().newInstance();
        assertThat(domainObject1.toString()).isNotNull();
        assertThat(domainObject1).isEqualTo(domainObject1);
        assertThat(domainObject1.hashCode()).isEqualTo(domainObject1.hashCode());
        // Test with an instance of another class
        Object testOtherObject = new Object();
        assertThat(domainObject1).isNotEqualTo(testOtherObject);
        assertThat(domainObject1).isNotEqualTo(null);
        // Test with an instance of the same class
        T domainObject2 = clazz.getConstructor().newInstance();
        assertThat(domainObject1).isNotEqualTo(domainObject2);
        // HashCodes are equals because the objects are not persisted yet
        assertThat(domainObject1.hashCode()).isEqualTo(domainObject2.hashCode());
    }

    public static String getAdminToken() {
        return MockOidcServerTestResource.getAccessToken("admin", Set.of("ROLE_ADMIN", "ROLE_USER"));
    }

    public static ObjectMapper jsonbObjectMapper() {
        final var config = new JsonbConfig().withDateFormat(DATE_TIME_FORMAT, null);
        final Jsonb jsonb = JsonbBuilder.create(config);
        return new ObjectMapper() {

            @Override
            public Object deserialize(ObjectMapperDeserializationContext context) {
                return jsonb.fromJson(context.getDataToDeserialize().asString(), context.getType());
            }

            @Override
            public Object serialize(ObjectMapperSerializationContext context) {
                return jsonb.toJson(context.getObjectToSerialize());
            }
        };
    }

    private TestUtil() {}
}
