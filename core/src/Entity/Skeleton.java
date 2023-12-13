package Entity;

public class Skeleton extends Enemy{
    public Skeleton() {
        super();
    }

    public Skeleton(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
    }
}
