package Entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected Vector2 Pos = new Vector2();
    protected int AttackSpeed;
    protected float MoveSpeed;
    protected int Health;
    protected Rectangle Hitbox;
    protected int Damage;

    protected boolean IsMovingRight, IsMovingLeft, IsMovingUp, IsMovingDown;

    protected boolean IsAttacking;
    public Entity(){
    }
    public Entity(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage){
        Pos.set(x,y);
        Hitbox = new Rectangle(x,y, Width, Height);
        this.AttackSpeed = AttackSpeed;
        this.MoveSpeed = MoveSpeed;
        this.Health = Health;
        this.Damage = Damage;
    }
    public void SetPos(float x, float y){
        Pos.set(x,y);
        Hitbox.set(x,y, Hitbox.width, Hitbox.height);
    }

    public Rectangle getHitbox() {
        return Hitbox;
    }

    public void Update(){
    }
    public void Attack(){
    }
    public void Die(){
    }
    public int GetXPosTile1(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.x + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetXPosTile2(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.x + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetXPosTile3(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.x + Hitbox.width + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetXPosTile4(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.x + Hitbox.width + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetYPosTile1(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.y + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetYPosTile2(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.y + Hitbox.height + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetYPosTile3(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.y + Hitbox.height + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }

    public int GetYPosTile4(float Correcter) {
        int counter = 0;
        float position;
        position = Pos.y + Correcter;
        while (position - 32 > 0) {
            counter++;
            position -= 32;
        }
        return counter;
    }
    public boolean CanMoveHere(int[][] map,int[][] mapCorrector, Vector2 input) {
        if (IsMovingRight) {
            if(Pos.x+input.x > 3168){
                return false;
            }
            if ( (
                    (map[GetXPosTile1(input.x)][GetYPosTile1(0)] != 0) ||
                    (map[GetXPosTile2(input.x)][GetYPosTile2(0)] != 0) ||
                    (map[GetXPosTile3(input.x)][GetYPosTile3(0)] != 0) ||
                    (map[GetXPosTile4(input.x)][GetYPosTile4(0)] != 0)) &&
                    ((mapCorrector[GetXPosTile3(input.x)][GetYPosTile3(0)] == 0) &&
                            (mapCorrector[GetXPosTile4(input.x)][GetYPosTile4(0)] == 0)))
            {
                return false;
            }
        }
        if (IsMovingLeft) {
            if(Pos.x-input.x <0){
                return false;
            }
            if ( (
                    (map[GetXPosTile1(input.x)][GetYPosTile1(0)] != 0) ||
                            (map[GetXPosTile2(input.x)][GetYPosTile2(0)] != 0) ||
                            (map[GetXPosTile3(input.x)][GetYPosTile3(0)] != 0) ||
                            (map[GetXPosTile4(input.x)][GetYPosTile4(0)] != 0)) &&
                    ((mapCorrector[GetXPosTile1(input.x)][GetYPosTile1(0)] == 0) &&
                            (mapCorrector[GetXPosTile2(input.x)][GetYPosTile2(0)] == 0))){
                return false;
            }
        }
        if (IsMovingUp) {
            if(Pos.y+input.y > 3168){
                return false;
            }
            if ( (
                    (map[GetXPosTile1(0)][GetYPosTile1(input.y)] != 0) ||
                            (map[GetXPosTile2(0)][GetYPosTile2(input.y)] != 0) ||
                            (map[GetXPosTile3(0)][GetYPosTile3(input.y)] != 0) ||
                            (map[GetXPosTile4(0)][GetYPosTile4(input.y)] != 0)) &&
                    ((mapCorrector[GetXPosTile2(0)][GetYPosTile2(input.y)] == 0) &&
                            (mapCorrector[GetXPosTile3(0)][GetYPosTile3(input.y)] == 0)))
            {
                return false;
            }
        }

        if (IsMovingDown) {

            if(Pos.y+input.y <0){
                return false;
            }
            if ( (
                    (map[GetXPosTile1(0)][GetYPosTile1(input.y)] != 0) ||
                            (map[GetXPosTile2(0)][GetYPosTile2(input.y)] != 0) ||
                            (map[GetXPosTile3(0)][GetYPosTile3(input.y)] != 0) ||
                            (map[GetXPosTile4(0)][GetYPosTile4(input.y)] != 0)) &&
                    ((mapCorrector[GetXPosTile1(0)][GetYPosTile1(input.y)] == 0) &&
                            (mapCorrector[GetXPosTile4(0)][GetYPosTile4(input.y)] == 0)))
            {
                return false;
            }
        }




        return true;
    }
    protected void Move(Vector2 input){
        Pos.add(input);
        Hitbox = new Rectangle(Pos.x,Pos.y, Hitbox.width, Hitbox.height);
    }

}
