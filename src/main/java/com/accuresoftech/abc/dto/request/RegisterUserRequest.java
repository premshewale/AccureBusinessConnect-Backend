package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.RoleKey;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterUserRequest {
	@NotBlank
	private String name;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	private String jobTitle;

	private RoleKey roleKey;

	private Long departmentId;
}
