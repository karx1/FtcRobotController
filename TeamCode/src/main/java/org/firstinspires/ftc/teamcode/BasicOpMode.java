package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class BasicOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        API.init(this);
        API.Motor motor = API.Motor.M0;

        waitForStart();

        while(opModeIsActive()) {
            motor.start(1);
        }
    }
}
