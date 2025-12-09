package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.UserStatus;
import lombok.Data;

@Data
public class UpdateUserRequest {
	private String name;
	private String jobTitle;
	private String phoneExtension;
	private UserStatus status;
	private Long departmentId;
}
