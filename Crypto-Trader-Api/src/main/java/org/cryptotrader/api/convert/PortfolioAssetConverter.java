package org.cryptotrader.api.convert;
//=================================-Imports-==================================
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cryptotrader.entity.portfolio.PortfolioAsset;

@Converter(autoApply = true)
public class PortfolioAssetConverter implements AttributeConverter<PortfolioAsset, String> {
    //============================-Variables-=================================
    ObjectMapper objectMapper;
    //===========================-Constructors-===============================
    public PortfolioAssetConverter() {
        this.objectMapper = new ObjectMapper();
    }
    //============================-Overrides-=================================

    //---------------------Convert-To-Database-Column-------------------------
    @Override
    public String convertToDatabaseColumn(PortfolioAsset portfolioAsset) {
        try {
            return this.objectMapper.writeValueAsString(portfolioAsset);
        } catch (JsonProcessingException ex) {
            final String EXCEPTION_MESSAGE = "Error converting portfolio asset to JSON.";
            throw new RuntimeException(EXCEPTION_MESSAGE, ex);
        }
    }
    //--------------------Convert-From-Database-Column------------------------
    @Override
    public PortfolioAsset convertToEntityAttribute(String portfolioAssetJson) {
        try {
            return this.objectMapper.readValue(portfolioAssetJson, PortfolioAsset.class);
        } catch (JsonProcessingException ex) {
            final String EXCEPTION_MESSAGE = "Error converting JSON to portfolio asset.";
            throw new RuntimeException(EXCEPTION_MESSAGE, ex);
        }
    }
}
