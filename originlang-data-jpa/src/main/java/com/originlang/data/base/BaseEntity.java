package com.originlang.data.base;

import com.originlang.data.converter.SoftDeleteConverter;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 848753134316472701L;

	public static final String CREATE_TIME = "createTime";

	public static final String UPDATETIME = "updateTime";

	public static final String CREATEBY = "createBy";

	public static final String UPDATEBY = "updateBy";

	@Column(nullable = false, comment = "create time", updatable = false)
	@CreatedDate
	private LocalDateTime createTime;

	@Column(nullable = false, comment = "update time")
	@LastModifiedDate
	private LocalDateTime updateTime;

	@Column(nullable = false, comment = "create user id", updatable = false)
	@ColumnDefault("0")
	@CreatedBy
	private Long createBy;

	@Column(nullable = false, comment = "update user id")
	@ColumnDefault("0")
	@LastModifiedBy
	private Long updateBy;

	@ColumnDefault("0")
	@Column(nullable = false, comment = "revision")
	@Version
	private Integer revision;

	/**
	 * default is 0
	 */
	@ColumnDefault("0")
	@Column(nullable = false)
	// @SoftDelete(strategy = SoftDeleteType.ACTIVE, converter =
	// SoftDeleteConverter.class)
	@SoftDelete(strategy = SoftDeleteType.TIMESTAMP)
	private Long deleteTime;

	@Column(nullable = false, comment = "deleted user id")
	@ColumnDefault("0")
	@LastModifiedBy
	private Long deleteBy;

}
