package com.originlang.minio;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("unused")
public interface TagEnum {

	// 状态类标签
	enum State implements TagEnum {

		// 废弃的
		ABANDON,
		// 未生效的
		INEFFECTIVE;

	}

	static Map<String, String> toMap(TagEnum tagEnum) {
		String key = tagEnum.getClass().getSimpleName().toUpperCase(Locale.ROOT);
		String label = tagEnum.toString();
		return Map.of(key, label);
	}

	static Map<String, String> toMap(List<TagEnum> tagEnums) {
		Map<String, String> map = new HashMap<>();
		tagEnums.forEach(e -> {
			String key = e.getClass().getSimpleName().toUpperCase(Locale.ROOT);
			String label = e.toString();
			map.put(key, label);
		});
		return map;
	}

}
