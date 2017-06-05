import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.rint;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    private static Set <Checkpoint> checkpoints = new LinkedHashSet<>();
    private static boolean boosted = false;
    private static int farthestCpId = -1;


//    public Player() {
//        checkpoints = new Set<Checkpoint>();
//    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            int nextCheckpointX = in.nextInt(); // x position of the next check point
            int nextCheckpointY = in.nextInt(); // y position of the next check point
            int nextCheckpointDist = in.nextInt(); // distance to the next checkpoint
            int nextCheckpointAngle = in.nextInt(); // angle between your pod orientation and the direction of the next checkpoint
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();
            String thrust = "0";

            //checkpoints eventueel toevoegen
            addCheckpoint (nextCheckpointX, nextCheckpointY);
            // ALS het volgende checkpoint het eerste is DAN eenmalig bepalen welke checkpoints het verst uit elkaar liggen
            if(checkpoints.size() > 1
                    && getCheckpointId(nextCheckpointX, nextCheckpointY) == 1
                    && farthestCpId == -1) {
                farthestCpId = getIdFarthestCheckpoint();
            }
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            if (abs(nextCheckpointAngle) > 90 )
                {thrust = "15";
                }
            if (abs(nextCheckpointAngle) < 90 )
                {thrust = "70";
                }
            if (abs(nextCheckpointAngle) < 45 )
                {thrust = "100";
                }
            if (abs(nextCheckpointAngle) < 5 )
                {
                    if ( (farthestCpId == getCheckpointId(nextCheckpointX, nextCheckpointY)) && !boosted)
                    {
                        thrust = "BOOST";
                        boosted = true;
                    }
                    else
                    {
                        thrust = "100";
                    }
                }
            if ( nextCheckpointDist< 1000 && farthestCpId > 0)
                {thrust = "40";
                //aim for the checkpoint after the real next Checkpoint
                int currentNextId = getCheckpointId(nextCheckpointX, nextCheckpointY);
                nextCheckpointX = getCpAfterNext(currentNextId).getX();
                nextCheckpointY = getCpAfterNext(currentNextId).getY();
                }
                
            System.err.println("x " + nextCheckpointX +
                    ", y " +  nextCheckpointY  +
                    ", ID " +  getCheckpointId(nextCheckpointX, nextCheckpointY)  +
                    ", dist " + nextCheckpointDist +
                    ", nextCheckpointAngle " + nextCheckpointAngle +
                    ", thrust "  + thrust  + boosted);

            // You have to output the target position
            // followed by the power (0 <= thrust <= 100)
            // i.e.: "x y thrust"
            System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + thrust);
        }
    }
    private static int getCheckpointId(int x, int y){
        Iterator<Checkpoint> it = checkpoints.iterator();
        Checkpoint nextCheckpoint = new Checkpoint(x, y );
        while(it.hasNext()){
            Checkpoint cpInCheckpoints= it.next();
            if (cpInCheckpoints.equals(nextCheckpoint)){
                return cpInCheckpoints.getID();
            }
        }
        System.err.println("Checkpoint ID NOT found");
        return -2;
    }
    
    private static Checkpoint getCpAfterNext(int nextCpId){
        if (checkpoints.size() > 1){
            for (int id = 1; id < checkpoints.size() + 1; id++) {
                Iterator<Checkpoint> it = checkpoints.iterator();
                if (nextCpId == checkpoints.size()) {
                    return it.next(); // The first Checkpoint
                }
                else
                {
                    while(it.hasNext()) {
                        if (it.next().getID() == nextCpId){
                            return it.next();
                        }
                    }
                }
            }
        }
        return new Checkpoint(1, 1); // unnecessary catch
    }

    private static int getIdFarthestCheckpoint() {
        double maxDistance = 0;
        for (int id = 1; id < checkpoints.size() + 1; id++) {
            Iterator<Checkpoint> it = checkpoints.iterator();
            while (it.hasNext()) {
                Checkpoint cpA = it.next();
                if (cpA.getID() == id) {
                    Checkpoint cpB = getCpAfterNext(id);
                    double distance = Math.sqrt(Math.pow((cpA.getX()-cpB.getX()),2) + Math.pow((cpA.getY() - cpB.getY()),2));
                    System.err.println("Distance to Checkpoint " + cpB.getID() + ": " +  rint(distance));
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        farthestCpId = cpB.getID();
                    }
                }
            }
        }
        System.err.println("farthestCp: " + farthestCpId);
        return farthestCpId;
    }

    private static void addCheckpoint (int x, int y){
        //Unieke checkpoint toevoegen
        Iterator<Checkpoint> it = checkpoints.iterator();
        Checkpoint testCheckpoint;
        Checkpoint potentialCheckpoint;
        potentialCheckpoint = new Checkpoint(x, y, checkpoints.size() + 1);
        boolean found = false;
        while (!found && it.hasNext()){
            testCheckpoint = it.next();
            if (testCheckpoint.equals(potentialCheckpoint)){
                found = true;
            }
        }
        if (!found ){
            checkpoints.add(potentialCheckpoint); // with coordinates & ID. First ID
            System.err.println("size na toevoeging " + checkpoints.size());
        }
    }
}
class Checkpoint{
    private int x;
    private int y;
    private int ID;

    Checkpoint( int x, int y, int ID){
        this.x = x;
        this.y = y;
        this.ID = ID;
    }
    Checkpoint ( int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getID(){
        return ID;
    }

    //**
    // Gebaseerd op : https://stackoverflow.com/questions/13791601/linkedhashset-equals-method
    // en https://stackoverflow.com/questions/13791601/linkedhashset-equals-method
    //**
    public boolean equals(Checkpoint checkpoint2){
        return (this.x == checkpoint2.getX() && this.y == checkpoint2.getY());
    }

}

