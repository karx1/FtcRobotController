package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="AAAAAAAAA")
public class TotallyGamepaad extends OpMode {
    private double speed = 1;
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
    }

    @Override
    public void loop() {
        MovementAPI.move(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,  gamepad1.y ? 1 : gamepad1.a ? 0.35 : speed, false);
        if (gamepad1.right_bumper) speed = Math.min(speed+0.002, 1);
        else if (gamepad1.left_bumper) speed = Math.max(speed-0.002, 0.2);

        intakeMotor.controlWithTwoButtons(gamepad2.a, gamepad2.b);
        liftMotor.controlWithTwoButtons(gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.left_bumper ? 0.6 : 0.25);
        carouselMotor.controlWithTwoButtons(gamepad2.x, gamepad2.y);
    }
}
