package frc.robot.commands.camera;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CameraSubsystem;

public class SetSnapshotModeCommand extends CommandBase {

    private CameraSubsystem s_camera;

    public SetSnapshotModeCommand(CameraSubsystem s_camera) {

    }

    @Override
    public void initialize() {
        s_camera.setSnapshotMode(true);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return true; /* Must not be true ~ change later */
    }

    @Override
    public void end(boolean interrupted) {
        s_camera.setSnapshotMode(false);
    }

}