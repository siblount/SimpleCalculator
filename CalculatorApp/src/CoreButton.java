import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class CoreButton extends JButton implements ActionListener {
	private OperationFunc func;
	public CoreButton(String name) {
		super(name);
		setFont(Main.DEFAULT_FONT);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		func.doOperation();
	}
	
	public void registerFunction(OperationFunc func) {
		this.func = func;
	}
}
