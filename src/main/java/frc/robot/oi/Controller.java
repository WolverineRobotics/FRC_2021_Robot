package frc.robot.oi;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.constants.JoystickMap.AxisMap;
import frc.robot.constants.JoystickMap.ButtonMap;
import frc.robot.constants.JoystickMap.POVMap;

public abstract class Controller {

    protected Joystick joystick;

    public Controller(int port) {
        joystick = new Joystick(port);
    }

    public boolean getButton(ButtonMap button) {
        return joystick.getRawButton(button.getPortNum());
    }

    public boolean getButtonPressed(ButtonMap button) {
        return joystick.getRawButtonPressed(button.getPortNum());
    }

    public double getAxis(AxisMap axis) {
        return joystick.getRawAxis(axis.getPort());
    }

    public POVMap getPOV() {
        int POVInt = joystick.getPOV();
        return POVMap.getPOV(POVInt);
    }

    protected Joystick getJoystick() {
        return joystick;
    }

    public JoystickButton getButtonObject(ButtonMap button){
        return (new JoystickButton(joystick, button.getPortNum()));
    }

}