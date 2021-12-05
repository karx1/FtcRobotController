package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@TeleOp(name="Gamepad Recorder v1.0.0")
public class GamepadRecord extends OpMode {
    private double speed = 1;
    private API.Motor intakeMotor = API.Motor.M4;
    private API.Motor liftMotor = API.Motor.M5;
    private API.Motor carouselMotor = API.Motor.M6;

    private String outputFileName = "recording1.json";
    private File outputFile;
    private JSONObject outputJSON;
    private boolean prevGuide = false;
    private int currentIndex = 0;

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
        outputFile = AppUtil.getInstance().getSettingsFile(outputFileName);
        try {
            outputJSON = new JSONObject("{ \"recordedData\": [] }");
        } catch (JSONException e) {
            API.print("Why are we here? Just to suffer?");
        }
    }

    @Override
    public void loop() {
        long ms = System.currentTimeMillis()+15;

        MovementAPI.move(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,  speed, false);

        API.print(
            "Speed: " + speed + System.lineSeparator() +
            "Time: " + this.time
        );
        
        if (gamepad1.right_bumper) speed = Math.min(speed+0.01, 1);
        else if (gamepad1.left_bumper) speed = Math.max(speed-0.01, 0.2);

        intakeMotor.controlWithTwoButtons(gamepad2.a, gamepad2.b);
        liftMotor.controlWithTwoButtons(gamepad2.dpad_up, gamepad2.dpad_down, 0.25);
        carouselMotor.controlWithTwoButtons(gamepad2.x, gamepad2.y);

        try {
            JSONObject currentData = new JSONObject()
                    .put("movement",
                            new JSONObject()
                                    .put("x", gamepad1.left_stick_x)
                                    .put("y", -gamepad1.left_stick_y)
                                    .put("turn", gamepad1.right_stick_x)
                                    .put("speed", speed)
                    )
                    .put("intake", intakeMotor.getPower())
                    .put("lift", liftMotor.getPower())
                    .put("carousel", carouselMotor.getPower())
                    .put("time", this.getRuntime() * 1000);

            if (!currentData.toString().equals(outputJSON.getJSONArray("recordedData").get(currentIndex - 1).toString()))
                outputJSON.getJSONArray("recordedData").put(currentIndex++, currentData);
        } catch (JSONException e) {
            API.print("Why are we here? Just to suffer?");
        }

        ms-=System.currentTimeMillis();
        if (ms>5) try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            API.print("Why are we here? Just to suffer?");
        }

        if (gamepad1.guide && !prevGuide) {
            ReadWriteFile.writeFile(outputFile, outputJSON.toString());
            API.print("Wrote file to '" + outputFile.getName() + "'!");
        }

        prevGuide = gamepad1.guide;
    }
}
