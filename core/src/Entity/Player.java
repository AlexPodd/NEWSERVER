package Entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity{
    private boolean OpenDoor;
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
    public Vector2 getPos() {
        return Pos;
    }
}
