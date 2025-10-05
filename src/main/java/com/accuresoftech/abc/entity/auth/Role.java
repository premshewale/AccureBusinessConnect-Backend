package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.enums.RoleKey;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import com.accuresoftech.abc.entity.BaseEntity;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_key", nullable = false, unique = true)
	private RoleKey key;

	@Column(name = "name", nullable = false)
	private String name;
}
