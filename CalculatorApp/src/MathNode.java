import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * MathNode is a dataclass for holding the numerical value and its
 * string representation. It also uses the NumberFormatter to
 * use the appropriate amount of decimal digits when necessary.
 */
public class MathNode {
	public static NumberFormat nf = NumberFormat.getInstance();
	private double num;
	private String presentation;
	public static final MathNode ZERO_MATH_NODE = new MathNode(0, "0");

	public MathNode(Double num) {
		this.num = num;
		this.presentation = nf.format(num);
	}
	
	public MathNode(double num, String presentation) {
		this.num = num;
		this.presentation = presentation;
		if (canAddDigit()) 
			finalizePresentation();
	}
	
	// Copy constructor
	public MathNode(MathNode otherNode) {
		num = otherNode.num;
		presentation = otherNode.presentation;
		if (canAddDigit()) 
			finalizePresentation();
	}
	
	public Double getNum() {
		return num;
	}
	
	public void setNum(double num) {
		this.num = num;
	}
	
	public String getPresentation() {
		return presentation;
	}
	
	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}
	/**
	 * Returns whether or not the presentation can be 
	 * parsed to a double.
	 * @return true if it can, otherwise false.
	 */
	public boolean canAddDigit() {
		try {
			Double.parseDouble(presentation);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Adds the digit to the current presentation along with its numerical value.
	 * @param digit
	 */
	public void addDigit(Byte digit) {
		presentation += digit;
		num = Double.parseDouble(presentation);
	}
	
	public String toString() {
		return presentation;
	}
	
	public static MathNode finalizeNode(MathNode node1, MathNode node2, Double newVal, Operator op) {
		node1.setPresentation(nf.format(newVal));
		node1.setNum(newVal);
		node1.finalizePresentation();
		return node1;
	}
	
	/**
	 * Finalizes the presentation by using the number formatter if it is parsable.
	 */
	public void finalizePresentation() {
		// If it can be parsed to a double... then finalize the presentation using
		// the number formatter.
		if (canAddDigit())
			presentation = nf.format(Double.parseDouble(presentation));
	}
	
	/**
	 * Returns the num value formatter using the number formatter
	 * as compared to num.toString()
	 * @return the formatted num
	 */
	public String numToString() {
		return nf.format(num);
	}


	/**
	 * Sets up the NumberFormatter.
	 */
	public static void initialize() {
		nf.setMaximumFractionDigits(16);
		nf.setRoundingMode(RoundingMode.FLOOR);
	}
	
	
}
