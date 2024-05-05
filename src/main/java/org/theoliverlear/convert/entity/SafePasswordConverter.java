package org.theoliverlear.convert.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.theoliverlear.entity.SafePassword;

@Converter(autoApply = true)
public class SafePasswordConverter implements AttributeConverter<SafePassword, String> {
    private ObjectMapper objectMapper;
    public SafePasswordConverter() {
        this.objectMapper = new ObjectMapper();
    }
    @Override
    public String convertToDatabaseColumn(SafePassword safePassword) {
        return safePassword.getEncodedPassword();
    }
    @Override
    public SafePassword convertToEntityAttribute(String encodedPassword) {
        SafePassword safePassword = new SafePassword();
        safePassword.setEncodedPassword(encodedPassword);
        return safePassword;
    }
}
