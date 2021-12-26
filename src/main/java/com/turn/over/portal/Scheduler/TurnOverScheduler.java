package com.turn.over.portal.Scheduler;

import com.turn.over.portal.Business.TurnoverBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@ConditionalOnProperty(name = "turn.over.schedule.status", havingValue = "ON", matchIfMissing = true)
public class TurnOverScheduler {

	private static final Logger log = LoggerFactory.getLogger(TurnOverScheduler.class);
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
	
	@Autowired
	TurnoverBusiness turnoverBusiness;

	@Scheduled(fixedRate = 1000 * 60, initialDelay = 1)
	public void scheduledTurnoverTemp() {
		executor.submit(() -> {
			log.info("Execute turn over temp call for update map :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
			turnoverBusiness.prepareTurnoverTempMap();
		});
	}
}
