package com.originlang.sys.account;

import com.originlang.data.base.BaseIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
// @SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({ AuditingEntityListener.class })
@DynamicInsert
@DynamicUpdate
@Entity
@Table(
// uniqueConstraints = {
// @UniqueConstraint(name = "i_uni_username", columnNames = {UserAccount_.USERNAME})
// }
)
public class Account extends BaseIdEntity {

	@Serial
	private static final long serialVersionUID = -5522239235493103213L;

	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false, unique = true)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String openId;

	/**
	 * 0-close, 1-email 2-TOTP
	 */
	// private Integer mfaType;

	// todo TOTP (Time-Based One-Time Password) 2FA
	// @OneToOne(targetEntity = Account.class)
	// @JoinColumn(name = "userId", foreignKey =
	// @ForeignKey(ConstraintMode.NO_CONSTRAINT), unique = true,
	// updatable = false)
	// private Long userId;

	private LocalDateTime lastLoginTime;

}
