package com.turn.over.portal.Controller;

import com.google.gson.GsonBuilder;
import com.turn.over.portal.Business.TurnoverBusiness;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/turnover")
public class TurnoverController {

    private static final Logger log = LoggerFactory.getLogger(TurnoverController.class);

    @Autowired
    private TurnoverBusiness turnoverBusiness;

    @GetMapping("/alltempdata")
    public String getAllTurnoverTemp(@RequestParam(required = false, defaultValue = "0") long from, @RequestParam(required = false, defaultValue = "100") long limit) {
        log.info("calling turn over temp csv file data methods.");
        return new GsonBuilder().serializeNulls().create()
                .toJsonTree(turnoverBusiness.readTempCSVFile().values().stream().skip(from).limit(limit).collect(Collectors.toList())).toString();
    }

    @GetMapping("/allturnoverdatabyexchange")
    public String getTurnoverByExchange(@RequestParam(required = false, defaultValue = "") String exchange, @RequestParam(required = false, defaultValue = "0") long from, @RequestParam(required = false, defaultValue = "100") long limit) {
        if(StringUtils.isNotBlank(exchange)){
            log.info("Getting turn over data by exchange keyword : "+exchange);
            return new GsonBuilder().serializeNulls().create()
                    .toJsonTree(turnoverBusiness.getTurnoverDataByExchange(exchange).stream().skip(from).limit(limit).collect(Collectors.toList())).toString();
        }else
            return "Exchange keyword is not correct or null. please verify it.";
    }

    @GetMapping("/allturnovervolumebyexchange")
    public String getTurnoverVolumeByExchange(@RequestParam(required = false, defaultValue = "") String exchange, @RequestParam(required = false, defaultValue = "sum") String result) {
        if(StringUtils.isNotBlank(exchange) && StringUtils.isNotBlank(result)){
            log.info("Getting turn over data volume calculate value : "+result+" by exchange keyword : "+exchange);
            return new GsonBuilder().serializeNulls().create()
                    .toJsonTree(turnoverBusiness.getTurnoverDataByExchangeAndVolume(exchange, result)).toString();
        }else
            return "Exchange keyword is not correct or null. please verify it.";
    }

    @GetMapping("/allturnovervolumewithdaterange")
    public String getTurnoverVolumeSumOrAverageWithDateRange(@RequestParam(required = false, defaultValue = "") List<String> exchangeList,
                                                             @RequestParam(required = false, defaultValue = "sum") String result,
                                                             @RequestParam(required = false, defaultValue = "", name = "") String fromDate,
                                                             @RequestParam(required = false, defaultValue = "", name = "") String toDate) {
        if(exchangeList.size() > 0 && StringUtils.isNotBlank(result)
                && StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
            log.info("Getting turn over data volume with data range calculate value : "+result+" by list exchange keyword : "+exchangeList
                    +" and from date : "+fromDate+" and to date : "+toDate);
            return new GsonBuilder().serializeNulls().create()
                    .toJsonTree(turnoverBusiness.getVolumeSumOrAverageWithDateRange(exchangeList, fromDate, toDate, result)).toString();
        }else
            return "Data is not correct or null. please verify it.";
    }
}
