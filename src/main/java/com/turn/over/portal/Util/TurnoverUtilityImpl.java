package com.turn.over.portal.Util;

import com.turn.over.portal.Constant.TurnOverConstant;
import com.turn.over.portal.Controller.TurnoverController;
import com.turn.over.portal.Model.Turnover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class TurnoverUtilityImpl implements TurnoverUtility{

    private static final Logger log = LoggerFactory.getLogger(TurnoverUtilityImpl.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${turn.over.file.path}")
    private String turnOverFilePath;

    @Override
    public Map<String, List<Turnover>> readTempCSVFile() {
        Map<String, List<Turnover>> map = null;
        synchronized (this){
            String line;
            map = new HashMap<>();
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile(turnOverFilePath + "\\" + TurnOverConstant.TURN_OVER_TEMP)))) {
                while ((line = bufferedReader.readLine()) != null) {
                    Turnover turnover = populateTurnoverTempData(line);
                    if(turnover != null){
                        if(map.get(turnover.getExchange()) == null){
                            List<Turnover> list = new ArrayList<>();
                            list.add(turnover);
                            map.put(turnover.getExchange(), list);
                        }else{
                            List<Turnover> list = map.get(turnover.getExchange());
                            list.add(turnover);
                            map.put(turnover.getExchange(), list);
                        }
                    }
                }
            }catch (Exception e){
                log.error("Error getting while reading data from temp csv file :",e);
            }
        }
        return map;
    }

    private Turnover populateTurnoverTempData(String line) {
        List<String> fields = null;
        Turnover turnover = null;
        try{
            fields = Arrays.asList(line.split(";+"));
            turnover = new Turnover();
            turnover.setIsin(fields.get(0));
            turnover.setTradeDate(LocalDate.parse(fields.get(1), dateTimeFormatter));
            turnover.setExchange(fields.get(2));
            turnover.setVolume(Double.parseDouble(fields.get(3)));
            turnover.setPieces(Double.parseDouble(fields.get(4)));
            turnover.setTrades(Double.parseDouble(fields.get(5)));
            if (fields.size() == 7) turnover.setBuy(fields.get(6));
        }catch (Exception e){
            log.error("Error getting while populate turn over temp data in object :",e);
        }
        return turnover;
    }
}
