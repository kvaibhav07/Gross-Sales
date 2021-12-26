package com.turn.over.portal.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turnover {
    private String isin;
    private String exchange;
    private double volume;
    private double pieces;
    private double trades;
    private String buy;
    private LocalDate tradeDate;
}
