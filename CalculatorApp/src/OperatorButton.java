import java.awt.event.*;
import javax.swing.*;


/**
 * OperatorButton are buttons such as: +, -, /, *, and MOD.
 * These buttons have unique functionality as their purpose is to
 * <p>
 * 1) call the main class to do math
 * <p>
 * 2) tell the main class what operation to do with the math nodes.
 */
@SuppressWarnings("serial")
public class OperatorButton extends JButton implements ActionListener {
	private Operator operator;
	public OperatorButton(Operator operator) {
		super(Main.getCorrespondingOperator(operator));
		setFont(Main.DEFAULT_FONT);
		this.operator = operator;
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Gets the current operation.
		Operator mainOp = Main.getCurrentOperator();
		
		// If the current math operation is NO_OP (meaning no +, -, etc)
		// and the current node is a dummy, then we can set the current
		// operator to this class' operator.
		// This is to prevent user from unexpectedly adding after hitting 
		// the plus button when they instead just wanted to change the
		// operation.
		if (mainOp != Operator.NO_OP && Main.isCurrentNodeDumb()) {
			Main.setCurrentOperator(operator);
			Main.updateView();
			return;
		}
		// Otherwise, the user used a modifier or inserted own values.
		// Therefore, it means they want to do some math.
		Main.doMath(operator);
		// If we didn't error, set the dummy node = to the current math node.
		if (Main.isAccepingInput()) {
			Main.setDummyNode(Main.getCurrentMathNode());
		}
	}

}
