package org.firstinspires.ftc.teamcode;

public class MovementAPI {
    private static API.Motor fl;
    private static API.Motor fr;
    private static API.Motor bl;
    private static API.Motor br;

    public static API.Motor getFL() { return fl; }
    public static API.Motor getFR() { return fr; }
    public static API.Motor getBL() { return bl; }
    public static API.Motor getBR() { return br; }

    /**
     * Initializes the API
     *
     * @param _fl the front-left wheel
     * @param _fr the front-right wheel
     * @param _bl the back-left wheel
     * @param _br the back-right wheel
     */
    public static void init(API.Motor _fl, API.Motor _fr, API.Motor _bl, API.Motor _br) {
        fl = _fl;
        fr = _fr;
        bl = _bl;
        br = _br;

        fl.setDirection(API.Direction.REVERSE);
        bl.setDirection(API.Direction.REVERSE);
        fr.setDirection(API.Direction.FORWARD);
        br.setDirection(API.Direction.FORWARD);
    }

    /**
     * Moves the robot given the speed to move forward/back and left/right
     * @param powerY the speed to move forward/back, -1 to 1, positive being forward
     * @param powerX the speed to move left/right, -1 to 1,  positive being to the right
     * @param turn the speed to turn at, -1 to 1, positive being clockwise
     * @param speed the speed to move at
     * @param verbose whether or not to log extra data to telemetry
     */
    public static void move(double powerY, double powerX, double turn, double speed, boolean verbose) {
        double flPower = (powerY + turn + powerX) * speed;
        double frPower = (powerY - turn - powerX) * speed;
        double blPower = (powerY + turn - powerX) * speed;
        double brPower = (powerY - turn + powerX) * speed;

        double scale = Math.max(1, (Math.abs(powerY) + Math.abs(turn) + Math.abs(powerX)) * Math.abs(speed)); // shortcut for max(abs([fl,fr,bl,br]))
        flPower /= scale;
        frPower /= scale;
        blPower /= scale;
        brPower /= scale;

        fl.start(flPower);
        fr.start(frPower);
        bl.start(blPower);
        br.start(brPower);

        if (verbose) API.print(
            "Front Left: " + flPower + System.lineSeparator() +
            "Back Left: " + blPower + System.lineSeparator() +
            "Front Right: " + frPower + System.lineSeparator() +
            "Back Right: " + brPower + System.lineSeparator()
        );
    }

    /**
     * @see MovementAPI#move(double, double, double, double, boolean)
     */
    public static void move(double power, double turn, double strafe, double speed) {
        move(power, strafe, turn, speed, false);
    }

    /**
     * Moves the robot given a direction and a speed
     *
     * @param direction the direction to move in, in degrees, with positive being to the left
     * @param speed the speed to move at
     * @param verbose whether or not to log extra data to telemetry
     */
    public static void move(double direction, double speed, boolean verbose) {
        move(Math.cos(Math.toRadians(direction)), Math.sin(Math.toRadians(-direction)), 0, speed, verbose);
    }

    /**
     * @see MovementAPI#move(double, double)
     */
    public static void move(double direction, double speed) {
        move(direction, speed, false);
    }

    /**
     * Stops the robot
     */
    public static void stop() {
        fl.stop();
        fr.stop();
        bl.stop();
        br.stop();
    }
}
