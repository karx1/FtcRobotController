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
        API.Servo s3 = API.Servo.S3;

        waitForStart();

        while(opModeIsActive()) {
            m6.start(1);
            s3.start(1);
        }
    }
}
