package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous(name="IMU Code Reborn")
public class IMUOpMode extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;
    BNO055IMU imu;
    long loop = Long.MAX_VALUE;
    int ms = 100;
    @Override
    public void init() {
        API.init(this);
        frontLeft = hardwareMap.get(DcMotor.class, "m0");
        frontRight = hardwareMap.get(DcMotor.class, "m1");
        backLeft = hardwareMap.get(DcMotor.class, "m2");
        backRight = hardwareMap.get(DcMotor.class, "m3");
        imu = API.imu.imu;
        //imu = hardwareMap.get(BNO055IMU.class, "imu");

    }

    @Override
    public void start() {
        imu.startAccelerationIntegration(new Position(DistanceUnit.METER, 1, 2, 0, 0), new Velocity(DistanceUnit.METER, 0, 0, 0, 0), ms);
        loop = System.currentTimeMillis()+ms;
    }

    @Override
    public void loop() {
        while (System.currentTimeMillis()<loop);
        Position currentPosition = imu.getPosition().toUnit(DistanceUnit.METER);
        Velocity currentVelocity = imu.getVelocity().toUnit(DistanceUnit.METER);
        Acceleration currentAcceleration = imu.getAcceleration().toUnit(DistanceUnit.METER);
        API.print(
                currentPosition.y + " " + currentPosition.x + "\n" +
                currentVelocity.yVeloc + " " + currentVelocity.xVeloc + "\n" +
                currentAcceleration.yAccel + " " + currentAcceleration.xAccel
        );
        /*double x2 = x+currentPosition.y;
        double y2 = y-currentPosition.x;
        API.print("X, Y", x2 +", " + y2);
        double flbr = y2+x2;
        double frbl = y2-x2;
        double max = 2.5*Math.max(Math.abs(flbr),Math.abs(frbl));
        flbr/=max;
        frbl/=max;
        frontLeft.setPower(flbr);
        frontRight.setPower(frbl);
        backLeft.setPower(-frbl);
        backRight.setPower(-flbr);*/
        loop+=ms;
    }
}
