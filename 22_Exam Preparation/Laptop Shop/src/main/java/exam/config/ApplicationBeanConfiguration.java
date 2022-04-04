package exam.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.util.ValidationUtil;
import exam.util.ValidationUtilImpl;
import exam.util.XmlParser;
import exam.util.XmlParserImpl;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, LocalDate> localDateConverter = new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                return mappingContext.getSource() == null
                        ? LocalDate.now()
                        : LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        };

        modelMapper.addConverter(localDateConverter);

        return modelMapper;
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public XmlParser xmlParser() {
        return new XmlParserImpl();
    }

}
