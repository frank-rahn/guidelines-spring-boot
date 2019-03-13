package de.rahn.guidelines.springboot.core.util;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_DATE;

@Component
@ConfigurationPropertiesBinding
public class LocalDateConverter implements Converter<String, LocalDate> {
	
	@Override
	public LocalDate convert(String source) {
		if (source == null) {
			return null;
		}
		
		return LocalDate.parse(source, ISO_DATE);
	}
	
}
