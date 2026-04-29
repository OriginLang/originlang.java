package com.originlang.base.json;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.TokenStreamFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

	// @Bean
	// public ObjectMapper objectMapper() {
	// return new ObjectMapper().registerModule(longToString)
	// // .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
	// // .setSerializationInclusion(JsonInclude.Include.NON_NULL)
	// .registerModule(timeModule)
	// // .registerModule(new ParameterNamesModule())
	// // .registerModule(new Jdk8Module())
	// // .registerModule(new JtsModule())
	// ;
	// }

	private static final SimpleModule longToString = new SimpleModule().addSerializer(Long.class,
			ToStringSerializer.instance);

	private static final SimpleModule dateTimeModule = new SimpleModule()
		.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
		.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

	private static final SimpleModule dateModule = new SimpleModule()
		.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
		.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

	private static final SimpleModule timeModule = new SimpleModule()
		.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")))
		.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

	private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Bean
	public ObjectMapper objectMapper() {
		return JsonMapper.builder()
			.addModules(longToString, dateTimeModule, dateModule, timeModule)
			.defaultDateFormat(defaultDateFormat)
			// .defaultTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")))
			.build();
	}

	@Bean
	JsonMapperBuilderCustomizer jacksonCustomizer() {
		return builder -> builder.addModules(longToString, dateTimeModule, dateModule, timeModule)
			.defaultDateFormat(defaultDateFormat)
			// .defaultTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")))
			// .enable(SerializationFeature.INDENT_OUTPUT)
			// 忽略未知字段
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

}
