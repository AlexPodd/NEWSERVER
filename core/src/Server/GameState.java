package Server;

import Entity.Enemy;
import Entity.Player;
import LvlManager.LvlManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;

import java.sql.Timestamp;
import java.util.LinkedList;

public class GameState {
    private LinkedList<PlayerInput> ProcessedPlayerInput;
    private Timestamp timestamp;
    private Player player1;
    private Player player2;
    private LinkedList<Enemy> enemies;
    private int[][] Map;
    private int[][] MapCorrector;
    public GameState(Timestamp timestamp, Player player1, Player player2, LinkedList<Enemy> enemies, int[][] Map, int[][] MapCorrector){
        this.timestamp = timestamp;
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
        this.enemies = enemies;
        this.Map = Map;
        this.MapCorrector = MapCorrector;
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
            player1.UpdatePos(input.getInputDir(), Map, MapCorrector);
        }
        if(input.getNumber() == 2){
            player2.UpdatePos(input.getInputDir(), Map, MapCorrector);
        }
        ProcessedPlayerInput.add(input);
    }
    public void UpdateGame(float Time, LvlManager lvlManager){
        lvlManager.UpdateMap(player1,player2);
        for(Enemy enemy : enemies) {
            enemy.Update(player1.getPos(), player1.getPos(), Time);
            if (enemy.GetProj() != null) {
                if (Intersector.overlaps(enemy.GetProj(), player1.getHitbox())) {
                    player1.GetDamaged(Time, enemy.GetDamage());
                }
            }
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
