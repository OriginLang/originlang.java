package com.originlang.data.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 逻辑删除转换器, deleted = 0 为未删除, deleted = time 为已删除
 *
 */
@Converter(autoApply = true)
public class SoftDeleteConverter implements AttributeConverter<Boolean, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Boolean attribute) {
		if (attribute != null && attribute) {
			return 1;
		}
		return 0;
	}

	@Override
	public Boolean convertToEntityAttribute(Integer dbData) {
		return dbData != null && dbData == 1;
	}

}
