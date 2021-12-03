package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="Gamepad 1.0 IMU 1.0")
public class Gamepad extends OpMode {
    private double speed = 1;
    private double prevSpeed = 1;
    private double imuOut = 0;
    private API.Motor intakeMotor = API.Motor.M4;
    private API.Motor liftMotor = API.Motor.M5;
    private API.Motor carouselMotor = API.Motor.M6;

    @Override
    public void init() {
        API.init(this);
        API.print("Status", "Initializing, please wait");
        API.pause(1);

        MovementAPI.init(API.Motor.M0, API.Motor.M1, API.Motor.M2, API.Motor.M3);

        API.clear();
        API.print("Press play to start");
    }

    @Override
    public void start() {
        API.clear();
        API.imu.reset();
        intakeMotor.setDirection(API.Direction.REVERSE);
    }

    @Override
    public void loop() {
        long ms = System.currentTimeMillis()+15;
        imuOut = API.imu.adjustAngle(API.imu.getHeading());

        double turn = gamepad1.right_stick_x;
        if (gamepad1.y && gamepad1.right_stick_x == 0) turn = imuOut/180;
        else API.imu.reset();

        MovementAPI.move(-gamepad1.left_stick_y, gamepad1.left_stick_x, turn,  speed, false);

        API.print(
            "Speed: " + speed + System.lineSeparator() +
            "Rotation (degrees, IMU): " + imuOut + System.lineSeparator() +
            "IMU Active: " + !gamepad1.y
        );
        
        if (gamepad1.right_bumper) speed = Math.min(speed+0.01, 1);
        else if (gamepad1.left_bumper) speed = Math.max(speed-0.01, 0.2);

        intakeMotor.controlWithTwoButtons(gamepad2.a, gamepad2.b);
        liftMotor.controlWithTwoButtons(gamepad2.dpad_up, gamepad2.dpad_down, 0.25);
        carouselMotor.controlWithTwoButtons(gamepad2.x, gamepad2.y);

        ms-=System.currentTimeMillis();
        if (ms>5) try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {}
    }

//    private void move(double power, double turn, double strafe, double speed, boolean verbose) {
//        double ffl = (-power + turn + strafe);
//        double fbl = (-power + turn - strafe);
//        /*if (gamepad1.y) {
//            ffl *= 0.65;
//            fbl *= 0.65;
//        }*/
//        double ffr = (-power - turn - strafe);
//        double fbr = (-power - turn + strafe);
//
//        double largest = 1.0;
//        largest = Math.max(largest, Math.abs(ffl));
//        largest = Math.max(largest, Math.abs(fbl));
//        largest = Math.max(largest, Math.abs(ffr));
//        largest = Math.max(largest, Math.abs(fbr));
//
//        ffl/=largest;
//        fbl/=largest;
//        ffr/=largest;
//        fbr/=largest;
//
//        ffl*=speed;
//        fbl*=speed;
//        ffr*=speed;
//        fbr*=speed;
//
//        fl.start(ffl);
//        bl.start(fbl);
//        fr.start(ffr);
//        br.start(fbr);
//
//        if (verbose) API.print("Speed: " + speed + System.lineSeparator() +
//                "Rotation (degrees, IMU): " + imuOut + System.lineSeparator() +
//                "Front Left: " + ffl + System.lineSeparator() +
//                "Back Left: " + fbl + System.lineSeparator() +
//                "Front Right: " + ffr + System.lineSeparator() +
//                "Back Right: " + fbr + System.lineSeparator() +
//                "IMU Active: " + !gamepad1.y);
//    }
}
