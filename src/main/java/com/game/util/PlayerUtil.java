package com.game.util;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerUtil {

    private PlayerUtil() {}

    public static List<Player> filterByPredicate(List<Player> players, Predicate<Player> filter) {
        return players.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static List<Player> filterByName(List<Player> players, String name) {
        return filterByPredicate(players, player -> player.getName().contains(name));
    }

    public static List<Player> filterByTitle(List<Player> players, String title) {
        return filterByPredicate(players, player -> player.getTitle().contains(title));
    }

    public static List<Player> filterByRace(List<Player> players, Race race) {
        return filterByPredicate(players, player -> player.getRace() == race);
    }

    public static List<Player> filterByProfession(List<Player> players, Profession profession) {
        return filterByPredicate(players, player -> player.getProfession() == profession);
    }

    public static List<Player> filterByBanned(List<Player> players, boolean banned) {
        return filterByPredicate(players, player -> player.getBanned() == banned);
    }

    public static List<Player> filterByDate(List<Player> players, Date start, Date end) {
        return filterByPredicate(players, player -> Util.isBetween(player.getBirthday(), start, end));
    }

    public static List<Player> filterByExperience(List<Player> players, Integer start, Integer end) {
        return filterByPredicate(players, player -> Util.isBetween(player.getExperience(), start, end));
    }

    public static List<Player> filterByLevel(List<Player> players, Integer start, Integer end) {
        return filterByPredicate(players, player -> Util.isBetween(player.getLevel(), start, end));
    }
}
