package Entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Skeleton extends Enemy{
    public Projectile projectile;
    public Skeleton(Skeleton skeleton) {
        this.IsImmunity = skeleton.IsImmunity;
        this.TimeImmunity = skeleton.TimeImmunity;
        this.Damage = skeleton.Damage;
        this.AttackSpeed = skeleton.AttackSpeed;
        this.Health = skeleton.Health;
        this.IsAttacking = skeleton.IsAttacking;
        this.Hitbox = skeleton.Hitbox;
        this.Pos = skeleton.Pos;
        this.projectile = skeleton.projectile;
    }
    public Skeleton(float x, float y, int AttackSpeed, float MoveSpeed, int Health, int Width, int Height, int Damage) {
        super(x, y, AttackSpeed, MoveSpeed, Health, Width, Height, Damage);
        projectile = new Projectile();
    }

    @Override
    public void Update(Vector2 Player1Pos, Vector2 Player2Pos, float StateTime) {
        if(IsAttacking){
            projectile.UpdateProj();
        }
        super.Update(Player1Pos, Player2Pos, StateTime);
    }

    @Override
    public void Attack(Vector2 Player1Pos, Vector2 Player2Pos) {
        Vector2 StartPos = new Vector2(Pos);
        if(Pos.dot(Player1Pos)>Pos.dst(Player2Pos)){
            projectile = new Projectile(StartPos, Player2Pos);
        }
        else {
            projectile = new Projectile(StartPos, Player1Pos);
        }

    }

    @Override
    public Circle GetProj() {
       return projectile.projectileCircle;
    }
    class Projectile{
        private Vector2 velocity;
        private Circle projectileCircle;
        private Vector2 PosProj;
        public Projectile(){
        }
        public Projectile(Vector2 StartPos, Vector2 EndPos){
            PosProj = StartPos;
            projectileCircle = new Circle(StartPos.x+8, StartPos.y+8, 1);
            velocity = EndPos.cpy().sub(Pos).nor().scl(3F);
        }
        public void UpdateProj(){
            PosProj.add(velocity);
            projectileCircle.setPosition(PosProj.x+8, PosProj.y+8);
        }

    }
}
