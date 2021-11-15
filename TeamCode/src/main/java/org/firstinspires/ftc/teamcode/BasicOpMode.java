package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class BasicOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        API.init(this);
        API.Motor m6 = API.Motor.M6;
        m6.setDirection(API.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {
            m6.start(1);
        }
    }
}
