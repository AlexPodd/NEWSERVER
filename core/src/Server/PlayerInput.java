package Server;

import com.badlogic.gdx.math.Vector2;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerInput implements Comparable<PlayerInput>{

    private final Timestamp timestamp;
    private final int number;
    private final Vector2 InputDir;
    private final int NumZap;


    public int getNumber() {
        return number;
    }

    public PlayerInput(String Message, int number){
        String[] InputWords = Message.split(" ");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(InputWords[0]+" "+InputWords[1]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        float X = 0;
        float Y = 0;
        if(InputWords[2].charAt(1) == '-'){

            X =  Float.valueOf(String.valueOf(InputWords[2].charAt(1))+ InputWords[2].charAt(2) + InputWords[2].charAt(3) + InputWords[2].charAt(4));
            Y =  Float.valueOf(String.valueOf(InputWords[2].charAt(6)) + InputWords[2].charAt(7) + InputWords[2].charAt(8));
        }
        if(InputWords[2].charAt(5) == '-'){

            X =  Float.valueOf(String.valueOf(InputWords[2].charAt(1))+ InputWords[2].charAt(2) + InputWords[2].charAt(3));
            Y =  Float.valueOf(String.valueOf(InputWords[2].charAt(5))+ InputWords[2].charAt(6) + InputWords[2].charAt(7) + InputWords[2].charAt(8));
        }
        if(InputWords[2].charAt(5) == '-'&&InputWords[2].charAt(1) == '-'){

            X =  Float.valueOf(String.valueOf(InputWords[2].charAt(1)) + InputWords[2].charAt(2) + InputWords[2].charAt(3) + String.valueOf(InputWords[2].charAt(4)));
            Y =  Float.valueOf(String.valueOf(InputWords[2].charAt(6))+ InputWords[2].charAt(7) + InputWords[2].charAt(8) + InputWords[2].charAt(9));
        }
        if((InputWords[2].charAt(5) != '-'&&InputWords[2].charAt(1) != '-')) {

            X = Float.valueOf(String.valueOf(InputWords[2].charAt(1)) + InputWords[2].charAt(2) + InputWords[2].charAt(3));
            Y = Float.valueOf(String.valueOf(InputWords[2].charAt(5)) + InputWords[2].charAt(6) + InputWords[2].charAt(7));
        }
        this.timestamp = new Timestamp(parsedDate.getTime());
        this.InputDir = new Vector2(X,Y);
        this.number = number;
        this.NumZap = Integer.parseInt(InputWords[3]);
    }

    public int getNumZap() {
        return NumZap;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Vector2 getInputDir() {
        return InputDir;
    }
    @Override
    public int compareTo(PlayerInput other) {
        return Long.compare(this.timestamp.getTime(), other.getTimestamp().getTime());
    }
}