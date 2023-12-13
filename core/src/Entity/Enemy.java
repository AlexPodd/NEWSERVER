package Entity;

public class Enemy extends Entity{
    public Enemy() {
    }
    public Enemy(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
    }

}
