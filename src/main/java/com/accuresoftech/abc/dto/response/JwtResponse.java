package com.accuresoftech.abc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for login/refresh endpoints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("user")
    private UserSummary user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String name;
        private String email;
        private String roleName;
        private Long roleId;
        private String departmentName;
    }
}
