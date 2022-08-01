package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import com.game.util.FilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/rest")
public class MyRestController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers(@RequestParam Map<String, String> params) {
        List<Player> players = FilterUtil.filter(playerService.getAllPlayers(), params);

        PlayerOrder order = PlayerOrder.ID;
        if (params.containsKey("order"))
            order = PlayerOrder.valueOf(params.get("order"));

        switch (order) {
            case ID:
                players.sort(Comparator.comparing(Player::getId));
                break;
            case NAME:
                players.sort(Comparator.comparing(Player::getName));
                break;
            case LEVEL:
                players.sort(Comparator.comparing(Player::getLevel));
                break;
            case EXPERIENCE:
                players.sort(Comparator.comparing(Player::getExperience));
                break;
            case BIRTHDAY:
                players.sort(Comparator.comparing(Player::getBirthday));
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
        List<Player> players = FilterUtil.filter(playerService.getAllPlayers(), params);
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
