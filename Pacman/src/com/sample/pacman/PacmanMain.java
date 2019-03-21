package com.sample.pacman;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PacmanMain {
    public static Map<Direction, Point> steps = new HashMap<Direction, Point>();
    private static Map<String, Method> commands = new HashMap<String, Method>();

    static {
        steps.put(Direction.NORTH, new Point(0, 1));
        steps.put(Direction.EAST, new Point(1, 0));
        steps.put(Direction.WEST, new Point(-1, 0));
        steps.put(Direction.SOUTH, new Point(0, -1));

        try {
            commands.put("MOVE", PacmanMain.class.getDeclaredMethod(
                    "move"));
            commands.put("LEFT", PacmanMain.class.getDeclaredMethod(
            "left"));
            commands.put("RIGHT", PacmanMain.class.getDeclaredMethod(
            "right"));
            commands.put("REPORT", PacmanMain.class.getDeclaredMethod(
            "report"));
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    Robot robot = new Robot();

    
    public static void main(String[] args) {
        PacmanMain robotSimulator = new PacmanMain();
        System.out.println("Please enter the commands");
        Scanner in = new Scanner(System.in);
        String command = null;
        while (true) {
            command = in.nextLine();
            if ("q".equalsIgnoreCase(command)) {
                return;
            }
            
            if (!commands.containsKey(command)) {
                if (!command.startsWith("PLACE")) {
                    System.out.println("The command is not valid, " + command);
                    continue;
                }
            }
            
            if (command != null && command.trim().toLowerCase().startsWith("place")) {
                String[] splitCommands = command.split(" ");
                if (splitCommands != null && splitCommands.length == 2) {
                    String[] params = splitCommands[1].split(",");
                    if (params != null && params.length == 3) {
                        String xStr = params[0];
                        String yStr = params[1];
                        String face = params[2];
                        
                        int x, y;
                        Direction d;
                        try {
                            x = Integer.valueOf(xStr);
                            y = Integer.valueOf(yStr);
                            d = Direction.faces.get(face);
                        } catch (NumberFormatException nfe) {
                            System.out.println("The PLACE command params are not valid, " + command);
                            continue;
                        }
                        
                        robotSimulator.placeXYF(x, y, d);
                    } else {
                        System.out.println("The PLACE command params are not valid, " + command);
                        continue;
                    }
                } else {
                    System.out.println("The PLACE command params are not valid, " + command);
                    continue;
                }
            } else {
                try {
                    commands.get(command).invoke(robotSimulator);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void placeXYF(int x, int y, Direction d) {
        robot.placeMe(new Point(x, y), d);
    }

    public void move() {
        robot.move();
    }

    public void left() {
        robot.turnLeft();
    }

    public void right() {
        robot.turnRight();
    }
    
    public void report() {
        System.out.println(robot.report());
    }

}

class TableTop {
    public static final int MAX_WIDTH = 4;
    public static final int MAX_HEIGHT = 4;

}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point addPoint(Point p) {
        int newPointX = this.x + p.x;
        int newPointY = this.y + p.y;

        return new Point(newPointX, newPointY);
    }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }
}

class Robot {
    Point point;
    Direction d;

    public void placeMe(Point p, Direction d) {
        this.point = p;
        this.d = d;

        if (isOutOfRange()) {
            throw new RuntimeException("Can't place the robot to this point: "
                    + this.point);
        }
    }

    public void turnLeft() {
        switch (d) {
        case NORTH:
            d = Direction.WEST;
            break;
        case WEST:
            d = Direction.SOUTH;
            break;
        case SOUTH:
            d = Direction.EAST;
            break;
        case EAST:
            d = Direction.NORTH;
            break;
        default:
            break;
        }
    }

    public void turnRight() {
        switch (d) {
        case NORTH:
            d = Direction.EAST;
            break;
        case WEST:
            d = Direction.NORTH;
            break;
        case SOUTH:
            d = Direction.WEST;
            break;
        case EAST:
            d = Direction.SOUTH;
            break;
        default:
            break;
        }
    }

    public void move() {
        Point moveP = PacmanMain.steps.get(this.d);
        this.point = this.point.addPoint(moveP);

        if (isOutOfRange()) {
            throw new RuntimeException("Can't move the robot to this point: "
                    + this.point);
        }
    }

    public boolean isOutOfRange() {
        if (this.point.x > TableTop.MAX_WIDTH || this.point.x < 0
                || this.point.y > TableTop.MAX_HEIGHT || this.point.y < 0) {
            return true;
        }

        return false;
    }

    public String report() {
        return "[" + this.point.x + ", " + this.point.y + ", " + this.d + "]";
    }
}

enum Direction {
    NORTH, SOUTH, EAST, WEST;
    
    public static Map<String, Direction> faces = new HashMap<String, Direction>();
    static {
        faces.put("NORTH", NORTH);
        faces.put("north", NORTH);
        faces.put("N", NORTH);
        faces.put("n", NORTH);
        faces.put("SOUTH", SOUTH);
        faces.put("south", SOUTH);
        faces.put("S", SOUTH);
        faces.put("s", SOUTH);
        faces.put("EAST", EAST);
        faces.put("east", EAST);
        faces.put("E", EAST);
        faces.put("e", EAST);
        faces.put("WEST", WEST);
        faces.put("west", WEST);
        faces.put("W", WEST);
        faces.put("w", WEST);
    }
}