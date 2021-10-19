package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class API {
    private static OpMode opMode;
    public static HubIMU imu;

    public static void init(final OpMode mode) {
        opMode = mode;
        HardwareMap map = mode.hardwareMap;
        imu = new HubIMU("imu", map);

        for (Motor m : Motor.values()) m.init(map);
        imu = new HubIMU("imu", map);
    }

    public static void pause(double seconds) {
        double time = opMode.getRuntime() + seconds;
        while (opMode.getRuntime()<time);
    }

    public static void print(String s) {
        opMode.telemetry.addLine(s);
        opMode.telemetry.update();
    }

    public static void print(String caption, String value) {
        opMode.telemetry.addData(caption, value);
        opMode.telemetry.update();
    }

    public static void clear() {
        opMode.telemetry.clear();
    }

    public enum Motor {
        M0("m0"), M1("m1"), M2("m2"), M3("m3");

        private final String name;
        private DcMotor rawMotor;
        private Direction direction = Direction.FORWARD;
        private double power = 0;

        Motor(String name) {
            this.name = name;
        }

        void init(HardwareMap map) {
            rawMotor = map.get(DcMotor.class, name);
            rawMotor.setPower(0);
        }

        /**
         * Starts the motor.
         */
        public void start() {
            rawMotor.setPower(power * direction.i);
        }

        /**
         * Starts the motor with the specified power.
         *
         * @param power the power to use for the motor
         */
        public void start(double power) {
            setPower(power);
            start();
        }

        /**
         * Stops the motor and sets the power to 0.
         */
        public void stop() {
            start(0);
        }

        /**
         * Sets the power without starting the motor.
         *
         * @param power the power to use for the motor
         */
        public void setPower(double power) {
            this.power = power;
        }

        /**
         * Sets the direction the motor should move in. Does not start the motor.
         *
         * @param direction the direction to use for the motor
         */
        public void setDirection(Direction direction) {
            setDirection(direction, false);
        }

        public void setDirection(Direction direction, boolean immediate) {
            this.direction = direction;
            if (immediate) start(this.power);
        }

        public void resetEncoder() {
            rawMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        public void resetEncoder(boolean enable) {
            rawMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            enableEncoder(enable); // in the code this was copy-pasted from enableEncoder was an empty func
        }
    }

    public enum Direction {
        FORWARD(1), REVERSE(-1);
        private final int i;
        Direction(int i) {
            this.i = i;
        }
    }

    public static class HubIMU {
        private final BNO055IMU imu;
        private final String name;
        private double zeroPos;

        public HubIMU(String name, HardwareMap hardwareMap) {
            this.name = name;
            imu = hardwareMap.get(BNO055IMU.class, name);
            setParameters();
        }

        private void setParameters() {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.useExternalCrystal = true;
            parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
            parameters.pitchMode = BNO055IMU.PitchMode.WINDOWS;
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            imu.initialize(parameters);
        }
        private double[] getAngles() {
            Quaternion quatAngles = imu.getQuaternionOrientation();
            double w = quatAngles.w;
            double x = quatAngles.x;
            double y = quatAngles.y;
            double z = quatAngles.z;

            double roll = Math.atan2( 2*(w*x + y*z) , 1 - 2*(x*x + y*y) ) * 180.0 / Math.PI;
            double pitch = Math.asin( 2*(w*y - x*z) ) * 180.0 / Math.PI;
            double yaw = Math.atan2( 2*(w*z + x*y), 1 - 2*(y*y + z*z) ) * 180.0 / Math.PI;

            return new double[]{yaw, pitch, roll};
        }

        /**
         * Adjusts an angle so that -180 <= angle < 180.
         *
         * @param angle the angle to adjust
         * @return the adjusted angle
         */
        public double adjustAngle(double angle) {
            return ((angle + 180) % 360) - 180;
        }

        public double getHeading() {
            return getYaw() - zeroPos;
        }
        public double getYaw () {
            return getAngles()[0];
        }
        public void reset(){
            zeroPos = getYaw();
        }
        public double getPitch() {
            return getAngles()[1];
        }
        public double getRoll() {
            return getAngles()[2];
        }
    }
}
