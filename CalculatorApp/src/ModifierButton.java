import javax.swing.*;
import java.awt.event.*;

interface MathFunc {
	/**
	 * This function will be used to get the value of
	 * the modifier. For example, where val = 10, and our 
	 * modifier is log... calling calculate(10) is the same 
	 * as log(10) which will return 1.
	 * @param val - The current value of the current node.
	 * @return - The modifier's math value.
	 */
	double calculate(double val);
	/**
	 * This function will be used to get the string representation
	 * of the current node's math value.
	 * For example, where val = 10, and our 
	 * modifier is log... calling getStringRepresentation(10) will
	 * return 'log(10)'.
	 * @param val - The value of the node before modified by calculate().
	 * @return - The modifier's string representation.
	 */
	String getStringRepresentation(String val);
}

/**
 * ModiferButton are buttons such as: sqrt, cube, tan.
 * These buttons have unique functionality as their purpose is to
 * <p>
 * 1) modify the current working math value and
 * <p>
 * 2) calculate using the MathFunc's calculate() and updates its string
 * representation.
 * <p>
 * 3) call the main function to do math once the button is clicked.
 */
public class ModifierButton extends JButton implements ActionListener {
	
	private MathFunc mathFunc;
	public ModifierButton(String name) {
		super(name);
		setFont(Main.DEFAULT_FONT);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Current Node will NEVER be null.
		MathNode currentNode = Main.getCurrentMathNode();

		// Save previous state of the current node.
		Double num = currentNode.getNum();
		String numFormatted = currentNode.numToString();
		try {
			// Calculate function can emit errors.
			currentNode.setNum(mathFunc.calculate(num));
			// If it emitted an error, we are done.
			if (!Main.isAccepingInput()) return;
			// Also, if the result is NaN, emit an error.
			if (Double.isNaN(currentNode.getNum())) {
				Main.emitError();
				return;
			}
			// Otherwise, update it's representation using the number formatted value 
			// that will be parsed using mathFunc.getStringRepresentation().
			currentNode.setPresentation(mathFunc.getStringRepresentation(numFormatted));

			// Then do math.
			Main.doMath(Operator.NO_OP);
			// If everything went smoothly, update it's presentation again.
			// We want the current node to have it's numeric value.
			if (Main.isAccepingInput())
				currentNode.setPresentation(currentNode.numToString());
			Main.updateView();
				
		} catch (Exception ex) {
			// Unexpected error.
			System.err.println(ex);
		}
	}
	
	public void registerMathFunction(MathFunc func) {
		mathFunc = func;
	}
}
