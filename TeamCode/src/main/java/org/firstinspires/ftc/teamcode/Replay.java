package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@Autonomous(name="Autonomous Replay v1.0.1", preselectTeleOp = "AAAAAAAAA")
public class Replay extends OpMode {
    private API.Motor fl = API.Motor.M0;
    private API.Motor fr = API.Motor.M1;
    private API.Motor bl = API.Motor.M2;
    private API.Motor br = API.Motor.M3;

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

        intakeMotor.setDirection(API.Direction.REVERSE);
        recordingFile = AppUtil.getInstance().getSettingsFile(recordingFileName);
        try {
            recordingJSON = new JSONObject(ReadWriteFile.readFile(recordingFile));
        } catch (JSONException e) {
            somethingWentWrong(e);
        }

        API.clear();
        API.print("Press play to start");
    }

    @Override
    public void start() {
        API.clear();
        API.imu.reset();
        this.resetStartTime();
    }

    @Override
    public void loop() {
        if (nextIndex == -1) return;

        try {
            JSONObject currentListElement = recordingJSON.getJSONArray("recordedData").getJSONObject(nextIndex);
            JSONObject currentData = currentListElement.getJSONObject("data");

            if (this.getRuntime() * 1000 < currentListElement.getDouble("time")) return;

            // Movement
            fl.setPower(currentData.getDouble("fl"));
            fr.setPower(currentData.getDouble("fr"));
            bl.setPower(currentData.getDouble("bl"));
            br.setPower(currentData.getDouble("br"));

            // Auxiliary
            intakeMotor.start(currentData.getDouble("intake"));
            liftMotor.start(currentData.getDouble("lift"));
            carouselMotor.start(currentData.getDouble("carousel"));

            nextIndex++;

            if(nextIndex >= recordingJSON.getJSONArray("recordedData").length()){
                this.requestOpModeStop();
                nextIndex = -1;
            }
        } catch (JSONException e) {
            somethingWentWrong(e);
        }
    }

    public void somethingWentWrong(Exception e) {
        API.print(
                "Why are we here? Just to suffer?" + System.lineSeparator() +
                e.getMessage()
        );
        this.requestOpModeStop();
        nextIndex = -1;
    }
}
