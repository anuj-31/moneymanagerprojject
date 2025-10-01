package com.project.moneymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterDTO {
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private  String keyword;
    private  String sortField;//date,amount, name
    private String sortOrder;//asc, desc

}
