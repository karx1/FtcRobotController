package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class API {
    private static OpMode opMode;
    public static HubIMU imu;

    /**
     * Initializes the API
     *
     * @param mode the opmode to initialize with
     */
    public static void init(final OpMode mode) {
        opMode = mode;
        HardwareMap map = mode.hardwareMap;
        imu = new HubIMU("imu", map);

        for (Motor m : Motor.values()) m.init(map);
        for (Servo s : Servo.values()) s.init(map);
        imu = new HubIMU("imu", map);
    }

    /**
     * Pauses for a given amount of seconds, with sub-millisecond accuracy
     *
     * @param seconds the amount of seconds to pause for
     */
    public static void pause(double seconds) {
        double time = opMode.getRuntime() + seconds;
        while (opMode.getRuntime()<time);
    }

    /**
     * Prints a line to telemetry
     *
     * @param s the text to print
     *
     * @see API#print(String, String)
     */
    public static void print(String s) {
        opMode.telemetry.addLine(s);
        opMode.telemetry.update();
    }

    /**
     * Prints a value to telemetry, formatted as "caption: value"
     *
     * @param caption the caption to print
     * @param value the value to print
     *
     * @see API#print(String)
     */
    public static void print(String caption, String value) {
        opMode.telemetry.addData(caption, value);
        opMode.telemetry.update();
    }

    /**
     * Clears the telemetry
     */
    public static void clear() {
        opMode.telemetry.clear();
    }

    public enum Motor {
        M0("m0"), M1("m1"), M2("m2"), M3("m3"), M4("m4"), M5("m5"), M6("m6"), M7("m7");

        private final String name;
        private DcMotor rawMotor;
        private Direction direction = Direction.FORWARD;
        private double power = 0;

        Motor(String name) {
            this.name = name;
        }

        private void init(HardwareMap map) {
            rawMotor = map.get(DcMotor.class, name);
            rawMotor.setPower(0);
        }

        /**
         * Starts the motor
         */
        public void start() {
            rawMotor.setPower(power * direction.i);
        }

        /**
         * Starts the motor with the specified power
         *
         * @param power the power to use for the motor
         */
        public void start(double power) {
            setPower(power);
            start();
        }

        /**
         * Stops the motor and sets the power to 0
         */
        public void stop() {
            rawMotor.setPower(0);
        }

        /**
         * Sets the power without starting the motor
         *
         * @param power the power to use for the motor
         */
        public void setPower(double power) {
            this.power = power;
        }

        /**
         * Sets the direction the motor should move in, without starting the motor
         *
         * @param direction the direction to use for the motor
         */
        public void setDirection(Direction direction) {
            setDirection(direction, false);
        }

        public void setDirection(Direction direction, boolean immediate) {
            this.direction = direction;
            if (immediate) start();
        }

        /**
         * Resets the encoder
         */
        public void resetEncoder() {
            rawMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        /**
         * Set the zero-power behaviour of the motor.
         *
         * BRAKE means to immediately apply brakes when power is 0, and FLOAT means to come to a rolling stop.
         * @param behaviour the behaviour to use
         */
        public void setBehaviour(MotorBehaviour behaviour) {
            if (behaviour.s.equals("brake")) {
                rawMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else {
                rawMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
        }

        /**
         * Moves the motor forward or backward at the provided speed based on the button inputs
         *
         * @param positiveControl the button used to move the motor forward
         * @param negativeControl the button used to move the motor backward
         */
        public void controlWithTwoButtons(boolean positiveControl, boolean negativeControl, double speed) {
            if (positiveControl && !negativeControl) this.start(speed);
            else if (negativeControl && !positiveControl) this.start(-speed);
            else this.stop();
        }

        /**
         * Moves the motor forward or backward at maximum speed based on the button inputs
         *
         * @param positiveControl the button used to move the motor forward
         * @param negativeControl the button used to move the motor backward
         */
        public void controlWithTwoButtons(boolean positiveControl, boolean negativeControl) {
            this.controlWithTwoButtons(positiveControl, negativeControl, 1);
        }

        /**
         * Moves the motor forward at the provided speed if the button is pressed.
         *
         * @param positiveControl the button used to move forward
         */
        public void controlWithOneButton(boolean positiveControl, double speed) {
            this.controlWithTwoButtons(positiveControl, false, speed);
        }

        /**
         * Moves the motor forward at maximum speed if the button is pressed.
         *
         * @param positiveControl the button used to move forward
         */
        public void controlWithOneButton(boolean positiveControl) {
            this.controlWithOneButton(positiveControl, 1);
        }
    }

    public enum MotorBehaviour {
        BRAKE("brake"), FLOAT("float");
        private final String s;
        MotorBehaviour(String s) {
            this.s = s;
        }
    }

    public enum Direction {
        FORWARD(1), REVERSE(-1);
        private final int i;
        Direction(int i) {
            this.i = i;
        }
    }

    public enum Servo {
        S0("s0"), S1("s1"), S2("s2"), S3("s3"), S4("s4"), S5("s5"), S6("s6"), S7("s7"), S8("s8"), S9("s9"), S10("s10"), S11("s11");
        private final String name;
        private com.qualcomm.robotcore.hardware.Servo servo;
        private Direction direction = Direction.FORWARD;
        private double power = 0;
        Servo(String name) {
            this.name = name;
        }

        void init(HardwareMap map) {
            servo = map.get(com.qualcomm.robotcore.hardware.Servo.class, name);
        }

        /**
         * Sets the intended position of the servo, in degrees.
         * Note that intended means it will not necessarily get to this position, but that it will constantly attempt to get there.
         *
         * @param degrees The intended position.
         */
        public void setPosition(double degrees) {
            servo.setPosition(degrees);
        }

        /**
         * Gets the current position of the servo. Might not match up with setPosition.
         *
         * @return The position in degrees.
         */
        public double getPosition() {
            return servo.getPosition();
        }

        /**
         * Starts the servo
         *
         * @param power The power to use, from 0 to 1
         */
        public void start(double power) {
            this.power = power;
            power*=direction.i;
            servo.setPosition(power/2+0.5);
        }

        /**
         * Stops the servo by setting the power to 0.
         */
        public void stop() {
            start(0);
        }

        /**
         * Sets the direction of the servo.
         *
         * @param direction The direction.
         * @param immediate Whether to start the servo upon setting direction or not.
         */
        public void setDirection(Direction direction, boolean immediate) {
            this.direction = direction;
            if (immediate) {
                start(power);
            }
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
         * Adjusts an angle so that -180 &#60;= angle &#62; 180.
         *
         * @param angle the angle to adjust
         * @return the adjusted angle
         */
        public double adjustAngle(double angle) {
            return ((angle + 180) % 360) - 180;
        }

        /**
         * Gets the yaw change since the last time the IMU was reset
         *
         * @return the yaw change
         */
        public double getHeading() {
            return getYaw() - zeroPos;
        }

        /**
         * Gets the yaw
         *
         * @return the yaw
         */
        public double getYaw() {
            return getAngles()[0];
        }

        /**
         * Resets the IMU
         */
        public void reset(){
            zeroPos = getYaw();
        }

        /**
         * Gets the pitch
         *
         * @return the pitch
         */
        public double getPitch() {
            return getAngles()[1];
        }

        /**
         * Gets the roll
         *
         * @return the roll
         */
        public double getRoll() {
            return getAngles()[2];
        }
    }
}
