package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Auton 2.0 maybe working?????", preselectTeleOp = "AAAAAAAAA")
public class AutonTwo extends LinearOpMode {
    private final API.Motor liftMotor = API.Motor.M5;
    @Override
    public void runOpMode() {
        API.init(this);
        API.print("Status", "Initializing, please wait");
        MovementAPI.init(API.Motor.M0, API.Motor.M1, API.Motor.M2, API.Motor.M3);
        liftMotor.setDirection(API.Direction.REVERSE, false);

        API.clear();
        API.print("Press play to start");

        waitForStart();

        MovementAPI.move(-180, 0.4, true);
        API.pause(0.8);
        MovementAPI.stop();
        liftMotor.start(0.25);
        API.pause(5);
        liftMotor.stop();
        liftMotor.start(-0.25);
        API.pause(2.5);
        liftMotor.stop();
        MovementAPI.move(-180, -0.4, true);
        API.pause(0.8);
        MovementAPI.stop();
    }
}
