package com.turn.over.portal.Util;

import com.turn.over.portal.Model.Turnover;

import java.util.List;
import java.util.Map;

public interface TurnoverUtility {

    public Map<String, List<Turnover>> readTempCSVFile();
}
