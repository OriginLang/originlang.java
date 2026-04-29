package com.originlang.data.base;

import com.originlang.data.hibernate.TsIdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseIdEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = 9210280031048523848L;

	public static final String ID = "id";

	@Id
	@NaturalId // 自然id
	// @GeneratedValue(strategy = GenerationType.IDENTITY) //自增
	@Column(updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TsIdGenerator
	private Long id;

}
