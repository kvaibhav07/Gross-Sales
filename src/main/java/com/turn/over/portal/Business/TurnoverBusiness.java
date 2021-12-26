package com.turn.over.portal.Business;

import com.turn.over.portal.Model.Turnover;

import java.util.List;
import java.util.Map;

public interface TurnoverBusiness {

    public Map<String, List<Turnover>> readTempCSVFile();

    public void prepareTurnoverTempMap();

    public List<Turnover> getTurnoverDataByExchange(String exchangeKey);

    public Double getTurnoverDataByExchangeAndVolume(String exchangeKey, String result);

    public Double getVolumeSumOrAverageWithDateRange(List<String> exchangeList, String fromDate, String toDate, String result);
}
