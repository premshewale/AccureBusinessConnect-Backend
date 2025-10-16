package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	// optional manager - manager must be SUB_ADMIN in business logic (enforced in
	// service)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "manager_id")
	private User manager;
}
