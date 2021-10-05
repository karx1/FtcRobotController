package org.firstinspires.ftc.teamcode;

public class MovementAPI {
    private static API.Motor fl;
    private static API.Motor fr;
    private static API.Motor bl;
    private static API.Motor br;

    public static void init(API.Motor _fl, API.Motor _fr, API.Motor _bl, API.Motor _br) {
        fl = _fl;
        fr = _fr;
        bl = _bl;
        br = _br;

        fl.setDirection(API.Direction.FORWARD);
        bl.setDirection(API.Direction.FORWARD);

        fr.setDirection(API.Direction.REVERSE);
        br.setDirection(API.Direction.REVERSE);
    }

    public static void move(double power, double turn, double strafe, double speed, boolean verbose) {
        double flPower = (power + turn + strafe) * speed;
        double frPower = (-power + turn - strafe) * speed;
        double blPower = (power + turn - strafe) * speed;
        double brPower = (-power + turn + strafe) * speed;

        double largest = Math.max(1, Math.abs(power) + Math.abs(turn) + Math.abs(strafe));

        flPower /= largest;
        frPower /= largest;
        blPower /= largest;
        brPower /= largest;

        fl.start(flPower);
        fr.start(frPower);
        bl.start(blPower);
        br.start(brPower);

        if (verbose) API.print(
                "Power: " + power + System.lineSeparator() +
                "Turn: " + turn + System.lineSeparator() +
                "Strafe: " + strafe + System.lineSeparator() +
                "Speed: " + speed + System.lineSeparator() +
                "Front Left: " + flPower + System.lineSeparator() +
                "Back Left: " + frPower + System.lineSeparator() +
                "Front Right: " + blPower + System.lineSeparator() +
                "Back Right: " + brPower + System.lineSeparator()
        );
    }

    public static void move(double direction, double speed, boolean verbose) {
        move(Math.cos(Math.toRadians(direction)), 0, Math.sin(Math.toRadians(-direction)), speed, verbose);
    }
}
