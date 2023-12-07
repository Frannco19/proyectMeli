package com.msmeli.configuration;

import com.msmeli.dto.AttributesDTO;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.model.Item;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(dtoItemToItem());
        modelMapper.addConverter(createAttributesConverter("SOME_OTHER_ID"));
        return modelMapper;
    }

    private PropertyMap<ItemFeignDTO, Item> dtoItemToItem() {
        return new PropertyMap<ItemFeignDTO, Item>() {
            @Override
            protected void configure() {
                // Mapear el campo catalog_product_id de DTO a catalog_product_id de la entidad
                map().setCatalog_product_id(source.getCatalog_product_id());

                // Transformar el campo price de int a Double
                map().setPrice(Double.valueOf(source.getPrice()));

                // Mapear el campo date_created de DTO a created_date_item de la entidad
                map().setCreated_date_item(source.getDate_created());

                // Mapear el campo thumbnail de DTO a image_url de la entidad
                map().setImage_url(source.getThumbnail());

                map().setSellerId(source.getSeller_id());

                map().setStatus_condition(source.getCondition());

                // Otros mapeos personalizados según sea necesario
                using(createAttributesConverter("GTIN")).map(source.getAttributes(), destination.getGtin());

                using(createAttributesConverter("BRAND")).map(source.getAttributes(), destination.getMarca());
            }


        };
    }

    private Converter<List<AttributesDTO>, String> createAttributesConverter(String targetId) {
        return new Converter<List<AttributesDTO>, String>() {
            @Override
            public String convert(MappingContext<List<AttributesDTO>, String> context) {
                List<AttributesDTO> attributes = context.getSource();
                if (attributes != null) {
                    for (AttributesDTO attribute : attributes) {
                        if (targetId.equals(attribute.getId())) {
                            return attribute.getValue_name();
                        }
                    }
                }
                return null;
            }
        };
    }
}
