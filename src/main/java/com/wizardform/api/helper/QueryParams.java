package com.wizardform.api.helper;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryParams {

    private String searchTerm = "";
    private String sortField = "";
    @Pattern(regexp = "asc|desc", message = "Sort direction must be either 'ascending' or 'descending'")
    private String sortDirection = "asc";     // default sorting direction is ascending (asc)
    @Min(value = 1, message = "Page number should be >= 1")
    @Max(value = Integer.MAX_VALUE, message = "Page number should be under max integer limit")
    private int pageNumber = 1;
    @Min(value = 20, message = "Page size should be >= 20")
    @Max(value = 200, message = "Page size should be <= 200")
    private int pageSize = 15;   // default page size is 15 records per page
}
