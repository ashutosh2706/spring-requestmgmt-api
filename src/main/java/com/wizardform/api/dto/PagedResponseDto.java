package com.wizardform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseDto<T> {
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private List<T> items;
}
