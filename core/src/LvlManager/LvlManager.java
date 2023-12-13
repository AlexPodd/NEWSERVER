package LvlManager;

import Entity.Player;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;



public class LvlManager {
    public TiledMap map;
    public LvlManager(){
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("Lvl1.tmx");
    }
    public void UpdateMap(Player player1, Player player2) {
        boolean OPENDOOR = false;
        MapObjects objects = map.getLayers().get("Кнопки").getObjects();
        int objectCount = objects.getCount();
        for (int i = 0; i < objectCount; i++) {
            MapObject object = objects.get(i);
            Rectangle objectBounds = ((RectangleMapObject) object).getRectangle();

        }
    }
}