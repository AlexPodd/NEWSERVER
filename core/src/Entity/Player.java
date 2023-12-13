package Entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity{
    public boolean OpenDoor;
    private boolean IsImmunity;
    private float TimeImmunity;
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

    }
    public int GetHP(){
        return Health;
    }
    public Player(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
    }

    @Override
    public void SetPos(float x, float y) {
        super.SetPos(x, y);
    }

    @Override
    public boolean CanMoveHere(TiledMap map, Vector2 input) {
        if(OpenDoor){
            if(Door(map, input)){
                return true;
            }
        }
        return super.CanMoveHere(map, input);
    }

    public void Update(Vector2 input, TiledMap map) {
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
        if(CanMoveHere(map, input)){
            Move(input);
        }
        IsMovingDown = false;
        IsMovingUp = false;
        IsMovingRight = false;
        IsMovingLeft = false;
    }

    private boolean Door(TiledMap map, Vector2 input){
        TiledMapTileLayer tileLayer2 = (TiledMapTileLayer) map.getLayers().get("ОткрытаяДверь");
        if (IsMovingUp){
            if((tileLayer2.getCell(GetXPosTile1(0), GetYPosTile1(input.y)) != null)||
                    (tileLayer2.getCell(GetXPosTile2(0), GetYPosTile2(input.y)) != null)||
                    (tileLayer2.getCell(GetXPosTile3(0), GetYPosTile3(input.y)) != null)||
                    (tileLayer2.getCell(GetXPosTile4(0), GetYPosTile4(input.y)) != null)
            )
            {
                return true;
            }
        }
        if (IsMovingDown){
            if((tileLayer2.getCell(GetXPosTile1(0), GetYPosTile1(input.y)) != null)||
                    (tileLayer2.getCell(GetXPosTile2(0), GetYPosTile2(input.y)) != null)||
                    (tileLayer2.getCell(GetXPosTile3(0), GetYPosTile3(input.y)) != null)||
                    (tileLayer2.getCell(GetXPosTile4(0), GetYPosTile4(input.y)) != null)
            )
            {
                return true;
            }
        }
        if (IsMovingRight){
            if((tileLayer2.getCell(GetXPosTile1(input.x), GetYPosTile1(0)) != null)||
                    (tileLayer2.getCell(GetXPosTile2(input.x), GetYPosTile2(0)) != null)||
                    (tileLayer2.getCell(GetXPosTile3(input.x), GetYPosTile3(0)) != null)||
                    (tileLayer2.getCell(GetXPosTile4(input.x), GetYPosTile4(0)) != null)
            )
            {
                return true;
            }
        }
        if (IsMovingLeft){
            if((tileLayer2.getCell(GetXPosTile1(input.x), GetYPosTile1(0)) != null)||
                    (tileLayer2.getCell(GetXPosTile2(input.x), GetYPosTile2(0)) != null)||
                    (tileLayer2.getCell(GetXPosTile3(input.x), GetYPosTile3(0)) != null)||
                    (tileLayer2.getCell(GetXPosTile4(input.x), GetYPosTile4(0)) != null)
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
