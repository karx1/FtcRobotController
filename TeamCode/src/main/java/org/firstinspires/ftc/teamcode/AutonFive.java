package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Auton 5.0", preselectTeleOp = "AAAAAAAAA")
public class AutonFive extends LinearOpMode {
    private final API.Motor liftMotor = API.Motor.M5;
    private API.Motor carouselMotor = API.Motor.M6;
    @Override
    public void runOpMode() {
        API.init(this);
        API.print("Status", "Initializing, please wait");
        MovementAPI.init(API.Motor.M0, API.Motor.M1, API.Motor.M2, API.Motor.M3);
        liftMotor.setDirection(API.Direction.REVERSE, false);

        API.clear();
        API.print("Press play to start");

        waitForStart();

        MovementAPI.move(0, 0.35, true);
        API.pause(4);
        MovementAPI.stop();
    }
}
