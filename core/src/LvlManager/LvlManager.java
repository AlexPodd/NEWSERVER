package LvlManager;

import Entity.Player;
import com.badlogic.gdx.math.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LvlManager {
    private final int[][] map;
    private final int[][] OpenDoor;
    private final int[][] mapCorrector;

    public int[][] getMap() {
        return map;
    }

    public int[][] getOpenDoor() {
        return OpenDoor;
    }

    private final Rectangle[] buttons;

    public int[][] getMapCorrector() {
        return mapCorrector;
    }

    public LvlManager(){
        File file = new File("assets/map.txt");
        File file1 = new File("assets/OpenDoor.txt");
        File file2 = new File("assets/MapCorrector.txt");
        try {
            map = MapCreator(file);
            OpenDoor = MapCreator(file1);
            mapCorrector = MapCreator(file2);
            Rectangle button1 = new Rectangle(160, 2146, 30, 30);
            Rectangle button2 = new Rectangle(258, 2500, 30, 30);
            buttons = new Rectangle[2];
            buttons[0] = button1;
            buttons[1] = button2;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void UpdateMap(Player player1, Player player2){
        boolean OPENDOOR = false;
        for (Rectangle button : buttons) {
            if (button.contains(player1.getPos().x, player1.getPos().y) || button.contains(player2.getPos().x, player2.getPos().y)
            ) {
                OPENDOOR = true;
                player1.OpenDoor = true;
                player2.OpenDoor = true;

            }
        }
        if(!OPENDOOR){
            player1.OpenDoor = false;
            player2.OpenDoor = false;

        }
    }
    private int[][] MapCreator(File file) throws FileNotFoundException {
        int Size = 100;
        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();

        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine().trim());
        }
        String text = stringBuilder.toString();
        String[] Tiles = text.split(",");
        scanner.close();
        int[][] Map = new int[Size][Size];
        int Index = 0;
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                Map[i][j] = Integer.parseInt(Tiles[Index].trim());
                Index++;
            }
        }
        int[][] ReverseMap = new int[Size][Size];

        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                ReverseMap[j][Size - i - 1] = Map[i][j];
            }
        }
        return ReverseMap;
    }
}
