package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rest")
public class MyRestController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers(@RequestParam Map<String, String> params) {
        List<Player> players = playerService.getAllPlayers();
        for (String key : params.keySet()) {
            switch (key) {
                case("name"):
                    CharSequence filterNameSequence = params.get("name");
                    List<Player> filteredByNamePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getName().contains(filterNameSequence))
                            filteredByNamePlayers.add(player);
                    }
                    players = filteredByNamePlayers;
                    break;
                case("title"):
                    CharSequence filterTitleSequence = params.get("title");
                    List<Player> filteredByTitlePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getTitle().contains(filterTitleSequence))
                            filteredByTitlePlayers.add(player);
                    }
                    players = filteredByTitlePlayers;
                    break;
                case("race"):
                    Race raceFilter = Race.valueOf(params.get("race"));
                    List<Player> filteredByRacePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getRace().equals(raceFilter))
                            filteredByRacePlayers.add(player);
                    }
                    players = filteredByRacePlayers;
                    break;
                case("profession"):
                    Profession professionFilter = Profession.valueOf(params.get("profession"));
                    List<Player> filteredByProfessionPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getProfession().equals(professionFilter))
                            filteredByProfessionPlayers.add(player);
                    }
                    players = filteredByProfessionPlayers;
                    break;
                case("after"):
                    Date afterDateFilter = new Date(Long.parseLong(params.get("after")));
                    List<Player> filteredByAfterDatePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getBirthday().after(afterDateFilter))
                            filteredByAfterDatePlayers.add(player);
                    }
                    players = filteredByAfterDatePlayers;
                    break;
                case("before"):
                    Date beforeDateFilter = new Date(Long.parseLong(params.get("before")));
                    List<Player> filteredByBeforeDatePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getBirthday().before(beforeDateFilter))
                            filteredByBeforeDatePlayers.add(player);
                    }
                    players = filteredByBeforeDatePlayers;
                    break;
                case("banned"):
                    List<Player> filteredByBanPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getBanned() == Boolean.valueOf(params.get("banned")))
                            filteredByBanPlayers.add(player);
                    }
                    players = filteredByBanPlayers;
                    break;
                case("minExperience"):
                    Integer minExpFilter = Integer.valueOf(params.get("minExperience"));
                    List<Player> filteredByMinExpPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getExperience() >= minExpFilter)
                            filteredByMinExpPlayers.add(player);
                    }
                    players = filteredByMinExpPlayers;
                    break;
                case("maxExperience"):
                    Integer maxExpFilter = Integer.valueOf(params.get("maxExperience"));
                    List<Player> filteredByMaxExpPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getExperience() <= maxExpFilter)
                            filteredByMaxExpPlayers.add(player);
                    }
                    players = filteredByMaxExpPlayers;
                    break;
                case("minLevel"):
                    Integer minLevelFilter = Integer.valueOf(params.get("minLevel"));
                    List<Player> filteredByMinLevelPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getLevel() >= minLevelFilter)
                            filteredByMinLevelPlayers.add(player);
                    }
                    players = filteredByMinLevelPlayers;
                    break;
                case("maxLevel"):
                    Integer maxLevelFilter = Integer.valueOf(params.get("maxLevel"));
                    List<Player> filteredByMaxLevelPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getLevel() <= maxLevelFilter)
                            filteredByMaxLevelPlayers.add(player);
                    }
                    players = filteredByMaxLevelPlayers;
                    break;
            }
        }
        PlayerOrder order = PlayerOrder.ID;
        if (params.containsKey("order"))
            order = PlayerOrder.valueOf(params.get("order"));

        switch (order) {
            case ID:
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });
                break;
            case NAME:
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case LEVEL:
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getLevel().compareTo(o2.getLevel());
                    }
                });
                break;
            case EXPERIENCE:
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getExperience().compareTo(o2.getExperience());
                    }
                });
                break;
            case BIRTHDAY:
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getBirthday().compareTo(o2.getBirthday());
                    }
                });
                break;
            default:
                break;
        }

        int pageNumber = 0;
        if (params.containsKey("pageNumber"))
            pageNumber = Integer.parseInt(params.get("pageNumber"));
        int pageSize = 3;
        if (params.containsKey("pageSize"))
            pageSize = Integer.parseInt(params.get("pageSize"));

        if (players.size() / pageSize == pageNumber)
            players = players.subList(pageNumber * pageSize, players.size());
        else
            players = players.subList(pageNumber * pageSize, (pageNumber * pageSize) + pageSize);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public ResponseEntity<Integer> countPlayers(@RequestParam Map<String, String> params) {
        List<Player> players = playerService.getAllPlayers();
        for (String key : params.keySet()) {
            switch (key) {
                case("name"):
                    CharSequence filterNameSequence = params.get("name");
                    List<Player> filteredByNamePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getName().contains(filterNameSequence))
                            filteredByNamePlayers.add(player);
                    }
                    players = filteredByNamePlayers;
                    break;
                case("title"):
                    CharSequence filterTitleSequence = params.get("title");
                    List<Player> filteredByTitlePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getTitle().contains(filterTitleSequence))
                            filteredByTitlePlayers.add(player);
                    }
                    players = filteredByTitlePlayers;
                    break;
                case("race"):
                    Race raceFilter = Race.valueOf(params.get("race"));
                    List<Player> filteredByRacePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getRace().equals(raceFilter))
                            filteredByRacePlayers.add(player);
                    }
                    players = filteredByRacePlayers;
                    break;
                case("profession"):
                    Profession professionFilter = Profession.valueOf(params.get("profession"));
                    List<Player> filteredByProfessionPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getProfession().equals(professionFilter))
                            filteredByProfessionPlayers.add(player);
                    }
                    players = filteredByProfessionPlayers;
                    break;
                case("after"):
                    Date afterDateFilter = new Date(Long.parseLong(params.get("after")));
                    List<Player> filteredByAfterDatePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getBirthday().after(afterDateFilter))
                            filteredByAfterDatePlayers.add(player);
                    }
                    players = filteredByAfterDatePlayers;
                    break;
                case("before"):
                    Date beforeDateFilter = new Date(Long.parseLong(params.get("before")));
                    List<Player> filteredByBeforeDatePlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getBirthday().before(beforeDateFilter))
                            filteredByBeforeDatePlayers.add(player);
                    }
                    players = filteredByBeforeDatePlayers;
                    break;
                case("banned"):
                    List<Player> filteredByBanPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getBanned() == Boolean.valueOf(params.get("banned")))
                            filteredByBanPlayers.add(player);
                    }
                    players = filteredByBanPlayers;
                    break;
                case("minExperience"):
                    Integer minExpFilter = Integer.valueOf(params.get("minExperience"));
                    List<Player> filteredByMinExpPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getExperience() >= minExpFilter)
                            filteredByMinExpPlayers.add(player);
                    }
                    players = filteredByMinExpPlayers;
                    break;
                case("maxExperience"):
                    Integer maxExpFilter = Integer.valueOf(params.get("maxExperience"));
                    List<Player> filteredByMaxExpPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getExperience() <= maxExpFilter)
                            filteredByMaxExpPlayers.add(player);
                    }
                    players = filteredByMaxExpPlayers;
                    break;
                case("minLevel"):
                    Integer minLevelFilter = Integer.valueOf(params.get("minLevel"));
                    List<Player> filteredByMinLevelPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getLevel() >= minLevelFilter)
                            filteredByMinLevelPlayers.add(player);
                    }
                    players = filteredByMinLevelPlayers;
                    break;
                case("maxLevel"):
                    Integer maxLevelFilter = Integer.valueOf(params.get("maxLevel"));
                    List<Player> filteredByMaxLevelPlayers = new ArrayList<>();
                    for (Player player : players) {
                        if (player.getLevel() <= maxLevelFilter)
                            filteredByMaxLevelPlayers.add(player);
                    }
                    players = filteredByMaxLevelPlayers;
                    break;
            }
        }
        Integer result = players.size();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        ResponseEntity<Player> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (player == null)
            return badRequest;
        if (!player.playerIsCorrect())
            return badRequest;
        player.setExperience(player.getExperience());
        Player createdPlayer = playerService.createPlayer(player);
        return new ResponseEntity<>(createdPlayer, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player playerForUpdate, @PathVariable Long id) {
        if (id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.getPlayer(id);
        if (player == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (playerForUpdate.getName() != null)
            player.setName(playerForUpdate.getName());
        if (playerForUpdate.getTitle() != null)
            player.setTitle(playerForUpdate.getTitle());
        if (playerForUpdate.getRace() != null)
            player.setRace(playerForUpdate.getRace());
        if (playerForUpdate.getProfession() != null)
            player.setProfession(playerForUpdate.getProfession());
        if (playerForUpdate.getBirthday() != null)
            player.setBirthday(playerForUpdate.getBirthday());
        if (playerForUpdate.getBanned() != null)
            player.setBanned(playerForUpdate.getBanned());
        if (playerForUpdate.getExperience() != null)
            player.setExperience(playerForUpdate.getExperience());

        if (!player.playerIsCorrect())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        if (id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.getPlayer(id);
        if (player == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }


    @DeleteMapping("/players/{id}")
    public ResponseEntity<String> deletePlayerById(@PathVariable Long id) {
        System.out.println("deletePlayerById: starts");
        System.out.println(id);
        if (id == 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            playerService.deletePlayerById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println("deletePlayerById: ends");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
