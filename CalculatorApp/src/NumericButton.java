import javax.swing.*;
import java.awt.event.*;
/**
 * NumericButton are button numbers 0-9.
 * These buttons have unique functionality as their purpose is to
 * <p>
 * 1) append the value to the current working node if 
 * it is not a dummy or if it can accept input.
 */
@SuppressWarnings("serial")
public class NumericButton extends JButton implements ActionListener {
	private static byte num = 0;
	private Byte buttonValue = 0;
	// Notice, it is a private constructor. Must be called through
	// generateNumericButtons() to generate buttons.
	private NumericButton() {
		super();
		setFont(Main.DEFAULT_FONT);
		buttonValue = num++;
		setText(buttonValue.toString());
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Main.addToWorkingValue(buttonValue);	
	}
	
	// Use this function to generate the numeric buttons.
	public static NumericButton[] generateNumericButtons() {
		NumericButton[] buttons = new NumericButton[10];
		for (byte i = 0; i < 10; i++) {
			buttons[i] = new NumericButton();
		}
		return buttons;
	}
	
	
}
