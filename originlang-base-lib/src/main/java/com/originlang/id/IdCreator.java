package com.originlang.id;

import com.github.f4b6a3.tsid.TsidCreator;

public final class IdCreator {

	public static long tsid() {
		return TsidCreator.getTsid().toLong();
	}

}
