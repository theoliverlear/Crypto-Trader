package org.theoliverlear.convert;
//=================================-Imports-==================================
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.theoliverlear.entity.portfolio.PortfolioAsset;

import java.util.ArrayList;

@Converter(autoApply = true)
public class PortfolioAssetArrayListConverter implements AttributeConverter<ArrayList<PortfolioAsset>, String> {
    //============================-Variables-=================================
    ObjectMapper objectMapper;
    //===========================-Constructors-===============================
    public PortfolioAssetArrayListConverter() {
        this.objectMapper = new ObjectMapper();
    }
    //============================-Overrides-=================================

    //---------------------Convert-To-Database-Column-------------------------
    @Override
    public String convertToDatabaseColumn(ArrayList<PortfolioAsset> portfolioAssets) {
        try {
            return this.objectMapper.writeValueAsString(portfolioAssets);
        } catch (JsonProcessingException ex) {
            final String EXCEPTION_MESSAGE = "Error converting portfolio assets to JSON.";
            throw new RuntimeException(EXCEPTION_MESSAGE, ex);
        }
    }
    //--------------------Convert-From-Database-Column------------------------
    @Override
    public ArrayList<PortfolioAsset> convertToEntityAttribute(String portfolioAssetsJson) {
        try {
            if (portfolioAssetsJson == null) {
                return new ArrayList<>();
            }
            return this.objectMapper.readValue(portfolioAssetsJson, new TypeReference<>() {});
        } catch (JsonProcessingException ex) {
            final String EXCEPTION_MESSAGE = "Error converting JSON to portfolio assets.";
            throw new RuntimeException(EXCEPTION_MESSAGE, ex);
        }
    }
}
