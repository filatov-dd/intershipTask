package com.game.util;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FilterUtil {

    private FilterUtil() {}

    public static List<Player> filter(List<Player> players, Map<String, String> params) {
        for (String key : params.keySet()) {
            switch (key) {
                case("name"):
                    players = PlayerUtil.filterByName(players, params.get("name"));
                    break;
                case("title"):
                    players = PlayerUtil.filterByTitle(players, params.get("title"));
                    break;
                case("race"):
                    players = PlayerUtil.filterByRace(players, Race.valueOf(params.get("race")));
                    break;
                case("profession"):
                    players = PlayerUtil.filterByProfession(players, Profession.valueOf(params.get("profession")));
                    break;
                case("after"):
                case("before"):
                    String after = params.getOrDefault("after", null);
                    String before = params.getOrDefault("before", null);
                    Date start = after == null ? null : new Date(Long.parseLong(after));
                    Date end = before == null ? null : new Date(Long.parseLong(before));
                    players = PlayerUtil.filterByDate(players, start, end);
                    break;
                case("banned"):
                    players = PlayerUtil.filterByBanned(players, Boolean.parseBoolean(params.get("banned")));
                    break;
                case("minExperience"):
                case("maxExperience"):
                    String minExperience = params.getOrDefault("minExperience", null);
                    String maxExperience = params.getOrDefault("maxExperience", null);
                    Integer startExp = minExperience == null ? null : Integer.parseInt(minExperience);
                    Integer endExp = maxExperience == null ? null : Integer.parseInt(maxExperience);
                    players = PlayerUtil.filterByExperience(players, startExp, endExp);
                    break;
                case("minLevel"):
                case("maxLevel"):
                    String minLevel = params.getOrDefault("minLevel", null);
                    String maxLevel = params.getOrDefault("maxLevel", null);
                    Integer startLevel = minLevel == null ? null : Integer.parseInt(minLevel);
                    Integer endLevel = maxLevel == null ? null : Integer.parseInt(maxLevel);
                    players = PlayerUtil.filterByLevel(players, startLevel, endLevel);
                    break;

            }
        }
        return players;
    }
}
