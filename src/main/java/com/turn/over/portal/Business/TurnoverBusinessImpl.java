package com.turn.over.portal.Business;

import com.turn.over.portal.Constant.TurnoverEnum;
import com.turn.over.portal.Model.Turnover;
import com.turn.over.portal.Util.TurnoverUtility;
import com.turn.over.portal.Util.TurnoverUtilityImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TurnoverBusinessImpl implements TurnoverBusiness{

    private static final Logger log = LoggerFactory.getLogger(TurnoverBusinessImpl.class);

    Map<String, List<Turnover>> turnoverTempMap = new ConcurrentHashMap<>();

    @Autowired
    private TurnoverUtility turnoverUtility;

    @Override
    public Map<String, List<Turnover>> readTempCSVFile() {
      return turnoverUtility.readTempCSVFile();
    }

    @Override
    public void prepareTurnoverTempMap() {
        turnoverTempMap = readTempCSVFile();
    }

    @Override
    public List<Turnover> getTurnoverDataByExchange(String exchangeKey) {
        if(turnoverTempMap.size() == 0)
            prepareTurnoverTempMap();
        return turnoverTempMap.get(exchangeKey);
    }

    @Override
    public Double getTurnoverDataByExchangeAndVolume(String exchangeKey, String result) {
        if(turnoverTempMap.size() == 0)
            prepareTurnoverTempMap();
        if(result.equalsIgnoreCase(TurnoverEnum.SUM.name()))
            return turnoverTempMap.get(exchangeKey).stream().mapToDouble(Turnover::getVolume).sum();
        else if(result.equalsIgnoreCase(TurnoverEnum.AVERAGE.name()))
            return turnoverTempMap.get(exchangeKey).stream().mapToDouble(Turnover::getVolume).average().getAsDouble();
        return null;
    }

    @Override
    public Double getVolumeSumOrAverageWithDateRange(List<String> exchangeList, String fromDate, String toDate, String result) {
        try {
            LocalDate fromLocalDate = LocalDate.parse(fromDate);
            LocalDate toLocalDate = LocalDate.parse(toDate);
            long numOfDaysBetween = ChronoUnit.DAYS.between(fromLocalDate, toLocalDate) + 1;
            List<LocalDate> dateList = IntStream.iterate(0, i -> i + 1)
                    .limit(numOfDaysBetween)
                    .mapToObj(fromLocalDate::plusDays)
                    .collect(Collectors.toList());
            List<Turnover> turnoverList = new ArrayList<>();
            for (String exchange : exchangeList) {
                if(StringUtils.isNotBlank(exchange)){
                    turnoverList = turnoverTempMap.get(exchange);
                }
            }
            List<Turnover> filteredTurnoverList = turnoverList.stream().filter(turnover -> dateList.contains(turnover.getTradeDate())).collect(Collectors.toList());
            if(result.equalsIgnoreCase(TurnoverEnum.SUM.name()))
                return filteredTurnoverList.stream().mapToDouble(Turnover::getVolume).sum();
            else if(result.equalsIgnoreCase(TurnoverEnum.AVERAGE.name()))
                return filteredTurnoverList.stream().mapToDouble(Turnover::getVolume).average().getAsDouble();
        }catch (Exception e){
            log.error("Error getting while get volume sum or average with date range :",e);
        }
        return null;
    }
}
