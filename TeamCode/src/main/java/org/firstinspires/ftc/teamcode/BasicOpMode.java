package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class BasicOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        API.init(this);
        API.Motor m0 = API.Motor.M0;
        API.Motor m1 = API.Motor.M1;
        API.Motor m2 = API.Motor.M2;
        API.Motor m3 = API.Motor.M3;

        waitForStart();

        while(opModeIsActive()) {
            m0.setDirection(API.Direction.REVERSE, false);
            m2.setDirection(API.Direction.REVERSE, false);
            m0.start(1);
            m2.start(1);

            m1.start(1);
            m3.start(1);
        }
    }
}
