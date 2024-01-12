import javax.swing.*;
import java.awt.event.*;
/**
 * StateButton are buttons: . and the +/- button.
 * These buttons have unique functionality as their purpose is to
 * <p>
 * 1) Solely change the value (+/-) or presentation (.) of the current
 * working node.
 */
@SuppressWarnings("serial")
public class StateButton extends JButton implements ActionListener {
    private OperationFunc func;
    public StateButton(String name) {
        super(name);
		setFont(Main.DEFAULT_FONT);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        // If we are not accepting input (we have errored), we are done...
        if (!Main.isAccepingInput()) return;
        // Otherwise, do the operation.
        func.doOperation();
    }

    public void registerFunction(OperationFunc func) {
        this.func = func;
    }
}
