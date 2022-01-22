package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
//    public List<Player> getPlayersList();
//
//    public Integer getPlayersCount(); //Количество игроков на данный момент (?) удалить комментарий
//
//    public void createPlayer(Player player);
//
//    public Player getPlayer(int id);
//
//    public void updatePlayer(int id, Player player);
//
//    public void deletePlayer(int id);
}
