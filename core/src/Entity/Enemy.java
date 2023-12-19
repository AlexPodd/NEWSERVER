package Entity;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity{
    protected boolean IsImmunity;
    protected float TimeImmunity;

    protected float PrevAttack;
    public Enemy() {

    }
    public Enemy(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
    }

    public void Update(Vector2 Player1Pos, Vector2 Player2Pos, float StateTime){
        if (IsAttacking) {
            if (StateTime - PrevAttack > AttackSpeed) {
                IsAttacking = false;
            }
        }
        if (PlayerIsHere(Player1Pos, Player2Pos)&& !IsAttacking){
            PrevAttack = StateTime;
            Attack(Player1Pos, Player2Pos);
            IsAttacking = true;
        }
        else {
            RandomMoving();
        }
    }
    public int GetDamage(){
        return Damage;
    }
    public void Attack(Vector2 Player1Pos, Vector2 Player2Pos) {
    }
    public void GetDamaged(float Damage){
        Health -=Damage;
        if(Health<0){
            Die();
        }
    }
    protected boolean PlayerIsHere(Vector2 posPlayer1, Vector2 posPlayer2){
        float Distance1 = posPlayer1.dst(Pos);
        float Distance2 = posPlayer2.dst(Pos);
        if(Distance1>100&&Distance2>100){
            return false;
        }
        else {
            return true;
        }
    }

    public void Die() {
    }

    @Override
    public boolean CanMoveHere(int[][] map,int[][] mapCorrector, Vector2 input) {
        return super.CanMoveHere(map,mapCorrector, input);
    }
    public Circle GetProj(){
        return null;
    }
    protected void RandomMoving(){


    }
}
