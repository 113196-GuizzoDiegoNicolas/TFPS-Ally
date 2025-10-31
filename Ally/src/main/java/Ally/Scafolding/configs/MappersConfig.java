package Ally.Scafolding.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class MappersConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 🔥 Conversor de String a LocalDate (para evitar error 500)
        mapper.addConverter(ctx -> {
            String source = ctx.getSource();
            return (source == null || source.isEmpty())
                    ? null
                    : LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
        }, String.class, LocalDate.class);

        return mapper;
    }

    @Bean("mergerMapper")
    public ModelMapper mergerMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull());

        // 🔥 También agregamos el mismo converter para las actualizaciones
        mapper.addConverter(ctx -> {
            String source = ctx.getSource();
            return (source == null || source.isEmpty())
                    ? null
                    : LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
        }, String.class, LocalDate.class);

        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
