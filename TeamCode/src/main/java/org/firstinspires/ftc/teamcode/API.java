package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class API {
    private static OpMode opMode;

    public static void init(final OpMode mode) {
        opMode = mode;
        HardwareMap map = mode.hardwareMap;

        for (Motor m : Motor.values()) m.init(map);
    }

    public static void print(String s) {
        opMode.telemetry.addLine(s);
        opMode.telemetry.update();
    }

    public static void print(String s1, String s2) {
        opMode.telemetry.addData(s1, s2);
        opMode.telemetry.update();
    }

    public static enum Motor {
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

        public void start(double power) {
            try {
                rawMotor.setPower(power*direction.i);
                this.power = power;
            } catch (Exception e) {/* should never occur */}
        }

        public void stop() {
            start(0);
        }

        public void setPower(double power) {
            try {
                rawMotor.setPower(power*direction.i);
                this.power = power;
            } catch (Exception e) {/* should never occur */}
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
            start(this.power);
        }
    }

    public static enum Direction {
        FORWARD(1), REVERSE(-1);
        private final int i;
        Direction(int i) {
            this.i = i;
        }
    }
}
