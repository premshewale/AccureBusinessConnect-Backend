package com.accuresoftech.abc.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageResponse<T> {
	private List<T> content;
	private long totalElements;
	private int totalPages;

}