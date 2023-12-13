package Entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    protected Vector2 Pos = new Vector2();
    protected int AttackSpeed;
    protected float MoveSpeed;
    protected int Health;
    protected Rectangle Hitbox;
    protected int Damage;


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
    public void Update(){
    }
    public void Attack(){
    }
    public void Die(){
    }
}
