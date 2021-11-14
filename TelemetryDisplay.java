package org.firstinspires.ftc.teamcode.OfficalGitHub;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetryDisplay {
    private Telemetry _telemetry;

    public TelemetryDisplay(Telemetry telemetry){
        _telemetry = telemetry;
    }

    public void addData(String message, Object value) {
        /*_telemetry.addData("Target Left: ", newLeftTarget);
        _telemetry.addData("Target right: ", newRightTarget);
        _telemetry.addData("Current Pos Left:", motorLeftBACK.getCurrentPosition());
        _telemetry.addData("Current Pos Right:", motorRightBACK.getCurrentPosition());
        _telemetry.addData("right power: ", motorRightBACK.getPower());
        _telemetry.addData("left power: ", motorLeftBACK.getPower());
        _telemetry.update();

         */
        _telemetry.addData(message, value);
    }

    public void update() {
        _telemetry.update();
    }
}
