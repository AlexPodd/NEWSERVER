package Entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity{
    public boolean OpenDoor;
    private boolean IsImmunity;
    private float TimeImmunity;
    private int[][] DoorMap;
    public Player(Player player) {
        this.IsImmunity = player.IsImmunity;
        this.OpenDoor = player.OpenDoor;
        this.AttackSpeed = player.AttackSpeed;
        this.TimeImmunity = player.TimeImmunity;
        this.Hitbox = player.Hitbox;
        this.Damage = player.Damage;
        this.Health = player.Health;
        this.Pos = player.Pos;
        this.MoveSpeed = player.MoveSpeed;
        this.DoorMap = player.DoorMap;

    }
    public int GetHP(){
        return Health;
    }
    public Player(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage, int[][] doorMap) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
        this.DoorMap = doorMap;
    }

    @Override
    public void SetPos(float x, float y) {
        super.SetPos(x, y);
    }
    public Rectangle getHitbox(){
        return Hitbox;
    }

    @Override
    public boolean CanMoveHere(int[][] map,int[][] mapCorrector, Vector2 input) {
        if(OpenDoor){
            if(Door(input)){
                return true;
            }
       }
        return super.CanMoveHere(map,mapCorrector, input);
    }
    public void GetDamaged(float StateTime, int Damage){
        if(IsImmunity){
            if(StateTime-TimeImmunity>1000){
                IsImmunity = false;
            }
        }
        if(!IsImmunity){
            Health-=Damage;
            IsImmunity = true;
            TimeImmunity = StateTime;
        }
        if(Health < 1){
            Die();
        }
    }
    public void Die(){
        SetPos(0,0);
        Health = 6;
    }
    public void UpdatePos(Vector2 input, int[][] map, int[][] mapCorrector) {
        if (input.x == MoveSpeed) {
            IsMovingRight = true;
        }
        if (input.x == -MoveSpeed) {
            IsMovingLeft = true;
        }
        if (input.y == MoveSpeed) {
            IsMovingUp = true;
        }
        if (input.y == -MoveSpeed) {
            IsMovingDown = true;
        }
        if(CanMoveHere(map,mapCorrector, input)){
            Move(input);
        }
        IsMovingDown = false;
        IsMovingUp = false;
        IsMovingRight = false;
        IsMovingLeft = false;
    }

    private boolean Door(Vector2 input){
        if (IsMovingUp){
            if((
                    (DoorMap[GetXPosTile1(0)][GetYPosTile1(input.y)] != 0) ||
                            (DoorMap[GetXPosTile2(0)][GetYPosTile2(input.y)] != 0) ||
                            (DoorMap[GetXPosTile3(0)][GetYPosTile3(input.y)] != 0) ||
                            (DoorMap[GetXPosTile4(0)][GetYPosTile4(input.y)] != 0))
            )
            {
                return true;
            }
        }
        if (IsMovingDown){
            if((
                    (DoorMap[GetXPosTile1(0)][GetYPosTile1(input.y)] != 0) ||
                            (DoorMap[GetXPosTile2(0)][GetYPosTile2(input.y)] != 0) ||
                            (DoorMap[GetXPosTile3(0)][GetYPosTile3(input.y)] != 0) ||
                            (DoorMap[GetXPosTile4(0)][GetYPosTile4(input.y)] != 0))
            )
            {
                return true;
            }
        }
        if (IsMovingRight){
            if((
                    (DoorMap[GetXPosTile1(input.x)][GetYPosTile1(0)] != 0) ||
                            (DoorMap[GetXPosTile2(input.x)][GetYPosTile2(0)] != 0) ||
                            (DoorMap[GetXPosTile3(input.x)][GetYPosTile3(0)] != 0) ||
                            (DoorMap[GetXPosTile4(input.x)][GetYPosTile4(0)] != 0))
            )
            {
                return true;
            }
        }
        if (IsMovingLeft){
            if((
                    (DoorMap[GetXPosTile1(input.x)][GetYPosTile1(0)] != 0) ||
                            (DoorMap[GetXPosTile2(input.x)][GetYPosTile2(0)] != 0) ||
                            (DoorMap[GetXPosTile3(input.x)][GetYPosTile3(0)] != 0) ||
                            (DoorMap[GetXPosTile4(input.x)][GetYPosTile4(0)] != 0))
            )
            {
                return true;
            }
        }
        return false;
    }
    public Vector2 getPos() {
        return Pos;
    }
}
