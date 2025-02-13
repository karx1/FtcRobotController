package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name="Autonomous 0.00000000000000000000000002 WORKING????", preselectTeleOp="AAAAAAAAA")
public class AutonOne extends LinearOpMode {
    @Override
    public void runOpMode() {
        API.init(this);
        API.print("Status", "Initializing, please wait");
        MovementAPI.init(API.Motor.M0, API.Motor.M1, API.Motor.M2, API.Motor.M3);

        API.clear();
        API.print("Press play to start");

        waitForStart();

        MovementAPI.move(0, 1, true);
        API.pause(1);
        MovementAPI.stop();
    }
}
