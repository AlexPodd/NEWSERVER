package Server;

import Entity.Enemy;
import Entity.Player;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.sql.Timestamp;
import java.util.LinkedList;

public class GameState {
    private LinkedList<PlayerInput> ProcessedPlayerInput;
    private Timestamp timestamp;
    private Player player1;
    private Player player2;
    private LinkedList<Enemy> enemies;
    private TiledMap Map;
    public GameState(Timestamp timestamp, Player player1, Player player2, LinkedList<Enemy> enemies, TiledMap Map){
        this.timestamp = timestamp;
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
        this.enemies = enemies;
        this.Map = Map;
        ProcessedPlayerInput = new LinkedList<>();
    }
    public GameState(GameState gameState, Timestamp timestamp){
        this.enemies = gameState.enemies;
        this.player1 = new Player(gameState.player1);
        this.player2 = new Player(gameState.player2);
        this.ProcessedPlayerInput = gameState.ProcessedPlayerInput;
        this.timestamp = timestamp;
        this.Map = gameState.Map;
    }
    public void UpdatePlayerPos(PlayerInput input){
        if(input.getNumber() == 1){
            player1.Update(input.getInputDir(), Map);
        }
        if(input.getNumber() == 2){
            player2.Update(input.getInputDir(), Map);
        }
        ProcessedPlayerInput.add(input);
    }
    public void UpdateGame(float Time){
        for(Enemy enemy : enemies){
            enemy.Update();
        }
        timestamp = new Timestamp(System.currentTimeMillis());
    }
    public GameState createCopy(Timestamp timestamp, GameState gameState){
        GameState gameState1 = new GameState(gameState, timestamp);
        ProcessedPlayerInput = new LinkedList<>();
        return gameState1;

    }
    public String getGameState(){
        String pl1Info = player1.getPos()+""+player1.GetHP();
        String pl2Info = player2.getPos()+""+player2.GetHP();
        return timestamp+" "+pl1Info+" "+pl2Info;
    }
}
