package com.accuresoftech.abc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
	private Long id;
	private String name;
	private String email;
	private String jobTitle;
	private String phoneExtension;
	private String status;
	private String roleName;
	private String departmentName;
}
