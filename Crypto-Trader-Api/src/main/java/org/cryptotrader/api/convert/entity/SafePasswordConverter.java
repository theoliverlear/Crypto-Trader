package org.cryptotrader.api.convert.entity;
//=================================-Imports-==================================
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cryptotrader.entity.user.SafePassword;

@Converter(autoApply = true)
public class SafePasswordConverter implements AttributeConverter<SafePassword, String> {
    //============================-Variables-=================================
    private ObjectMapper objectMapper;
    //===========================-Constructors-===============================
    public SafePasswordConverter() {
        this.objectMapper = new ObjectMapper();
    }
    //============================-Overrides-=================================

    //---------------------Convert-To-Database-Column-------------------------
    @Override
    public String convertToDatabaseColumn(SafePassword safePassword) {
        return safePassword.getEncodedPassword();
    }
    //--------------------Convert-From-Database-Column------------------------
    @Override
    public SafePassword convertToEntityAttribute(String encodedPassword) {
        SafePassword safePassword = new SafePassword();
        safePassword.setEncodedPassword(encodedPassword);
        return safePassword;
    }
}
