package Entity;

public class Player extends Entity{
    public Player() {
        super();
    }

    public Player(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
    }

    @Override
    public void SetPos(float x, float y) {
        super.SetPos(x, y);
    }

    public void Update() {

    }
}
