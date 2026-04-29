package com.originlang.data.hibernate;

import com.github.f4b6a3.tsid.TsidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serial;
import java.io.Serializable;
import java.util.Properties;

/**
 * id生成器
 *
 */
@SuppressWarnings("unused")
public final class IdGenerate implements Configurable, IdentifierGenerator {

	@Serial
	private static final long serialVersionUID = 9023860583568712570L;

	public static final String ID_PREFIX = "0001";

	/**
	 * id前缀
	 */
	private String idPrefix;

	public IdGenerate() {
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
		return TsidCreator.getTsid().toLong();
	}

	@Override
	public void configure(org.hibernate.generator.GeneratorCreationContext creationContext,
			java.util.Properties parameters) {
		this.idPrefix = parameters.getProperty(ID_PREFIX);
	}

}
