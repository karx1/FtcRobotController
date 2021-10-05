package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Gamepad 1.0 IMU 1.0 (VERY NOT STABLE!!)")
public class Gamepad extends OpMode {
    private int step = 0;
    private API.Motor fl = API.Motor.M0;
    private API.Motor fr = API.Motor.M1;
    private API.Motor bl = API.Motor.M2;
    private API.Motor br = API.Motor.M3;
    private double speed = 1;
    private double imuOut = 0;

    private final double imuAdjustmentSlowdownZone = 30;

    @Override
    public void init() {
        API.init(this);
        API.print("Status", "Initializing, please wait");
        API.pause(1);
//        br.setDirection(API.Direction.REVERSE, false);

        fl.resetEncoder();
        fr.resetEncoder();
        bl.resetEncoder();
        br.resetEncoder();
        API.clear();
        API.print("Press play to start");
    }

    @Override
    public void start() {
        API.clear();
        API.imu.reset();
    }

    @Override
    public void loop() {
        if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0 && gamepad1.right_stick_y == 0) {
            API.print("gamepad null, stopping wheels");
            for (API.Motor m : API.Motor.values()) m.stop();
        } else {

            API.print(String.valueOf(step));
            if (gamepad2.left_bumper) step--;
            if (gamepad2.right_bumper) step++;
            if (step < 25) step = 25;
            if (step > 425) step = 425;
            long ms = System.currentTimeMillis() + 15;
            imuOut = API.imu.adjustAngle(API.imu.getHeading());

            double turn = gamepad1.left_stick_x;
            if (!gamepad1.y && gamepad1.left_stick_x == 0) turn = Math.min(imuOut/imuAdjustmentSlowdownZone, 1);
            else API.imu.reset();

            move(gamepad1.left_stick_y, turn, gamepad1.right_stick_x, speed, true);

            if (gamepad1.right_bumper) speed = Math.min(speed + 0.01, 1);
            else if (gamepad1.left_bumper) speed = Math.max(speed - 0.01, 0.2);
            else if (gamepad1.x) speed = 0.35;

            ms -= System.currentTimeMillis();
            if (ms > 5) try {
                Thread.sleep(ms);
            } catch (InterruptedException ignored) {
            }
        }
    }
    private void move(double power, double turn, double strafe, double speed, boolean verbose) {
        double ffl = (-power + turn + strafe);
        double fbl = (-power + turn - strafe);
        /*if (gamepad1.y) {
            ffl *= 0.65;
            fbl *= 0.65;
        }*/
        double ffr = (-power - turn - strafe);
        double fbr = (-power - turn + strafe);

        double largest = 1.0;
        largest = Math.max(largest, Math.abs(ffl));
        largest = Math.max(largest, Math.abs(fbl));
        largest = Math.max(largest, Math.abs(ffr));
        largest = Math.max(largest, Math.abs(fbr));

        ffl/=largest;
        fbl/=largest;
        ffr/=largest;
        fbr/=largest;

        ffl*=speed;
        fbl*=speed;
        ffr*=speed;
        fbr*=speed;

        fl.start(ffl);
        bl.start(fbl);
        fr.start(ffr);
        br.start(fbr);

        if (verbose) API.print("Power: " + power + System.lineSeparator() +
                "Turn: " + turn + System.lineSeparator() +
                "Strafe: " + strafe + System.lineSeparator() +
                "Speed: " + speed + System.lineSeparator() +
                "Rotation (degrees, IMU): " + imuOut + System.lineSeparator() +
                "Front Left: " + ffl + System.lineSeparator() +
                "Back Left: " + fbl + System.lineSeparator() +
                "Front Right: " + ffr + System.lineSeparator() +
                "Back Right: " + fbr + System.lineSeparator() +
                "IMU Active: " + !gamepad1.y);
    }
}
