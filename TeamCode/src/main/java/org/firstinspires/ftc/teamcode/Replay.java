package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@Autonomous(name="Autonomous Replay v1.0.0", preselectTeleOp = "AAAAAAAAA")
public class Replay extends OpMode {
    private API.Motor intakeMotor = API.Motor.M4;
    private API.Motor liftMotor = API.Motor.M5;
    private API.Motor carouselMotor = API.Motor.M6;

    private String recordingFileName = "recording1.json";
    private File recordingFile;
    private JSONObject recordingJSON;
    private int nextIndex = 0;

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
        recordingFile = AppUtil.getInstance().getSettingsFile(recordingFileName);
        try {
            recordingJSON = new JSONObject(ReadWriteFile.readFile(recordingFile));
        } catch (JSONException e) {
            API.print("Why are we here? Just to suffer?");
        }
    }

    @Override
    public void loop() {
        if (nextIndex == -1) return;

        try {
            JSONObject currentData = recordingJSON.getJSONArray("recordingData").getJSONObject(nextIndex);

            if (this.getRuntime() * 1000 < currentData.getDouble("time")) return;

            JSONObject currentMovementData = currentData.getJSONObject("movement");
            MovementAPI.move(
                    currentMovementData.getDouble("y"),
                    currentMovementData.getDouble("x"),
                    currentMovementData.getDouble("turn"),
                    currentMovementData.getDouble("speed"),
                    true
            );

            intakeMotor.start(currentData.getDouble("intake"));
            liftMotor.start(currentData.getDouble("lift"));
            carouselMotor.start(currentData.getDouble("carousel"));

            nextIndex++;

            if(nextIndex >= recordingJSON.getJSONArray("recordingData").length()){
                this.requestOpModeStop();
                nextIndex = -1;
            }
        } catch (JSONException e) {
            API.print("Why are we here? Just to suffer?");
        }

//        try {
//            outputJSON.getJSONArray("recordedData").put(
//                    new JSONObject()
//                    .put("movement",
//                            new JSONObject()
//                            .put("x", gamepad1.left_stick_x)
//                            .put("y", -gamepad1.left_stick_y)
//                            .put("turn", gamepad1.right_stick_x)
//                            .put("speed", speed)
//                    )
//                    .put("intake", intakeMotor.getPower())
//                    .put("lift", liftMotor.getPower())
//                    .put("carousel", carouselMotor.getPower())
//                    .put("time", this.getRuntime() * 1000)
//            );
//        } catch (JSONException e) {
//            API.print("Why are we here? Just to suffer?");
//        }
    }
}
