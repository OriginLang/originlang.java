package com.originlang.data.base;

import com.originlang.data.hibernate.TsIdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.TenantId;

import java.io.Serial;

/**
 * id , userId
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseTenantIdEntity extends BaseEntity {

	@Id
	@NaturalId // 自然id
	// @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
	@Column(updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TsIdGenerator
	private Long id;

	@Serial
	private static final long serialVersionUID = 7498783920423533869L;

	@TenantId
	@Column(nullable = false)
	private Long tenantId;

}
