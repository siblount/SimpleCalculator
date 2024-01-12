import java.util.*;
import javax.swing.*;
import java.awt.*;


public class Main {
	private static JFrame mainFrame = new JFrame("Calculator");
	private static JPanel topButtonPanel = new JPanel(new GridBagLayout()), bottomButtonPanel = new JPanel(new GridBagLayout());
	private static JTextField historyField = new JTextField(15), currentHistoryField = new JTextField(15);
	private static GridBagConstraints constraints = new GridBagConstraints();
	private static MathNode lastMathNode = null, currentMathNode = MathNode.ZERO_MATH_NODE;
	private static MathNode dummyNode = MathNode.ZERO_MATH_NODE;
	private static final Color BACKGROUND_GREEN_COLOR = new Color(0, 153, 51), BACKGROUND_RED_COLOR = new Color(153,0,0);
	private static boolean canAcceptInput = true;
	private static Operator mathOp = Operator.NO_OP;

	private static HashMap<Operator, String> operatorToStringMap = new HashMap<Operator, String>(5);
	public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	/**
	 * A utility function to easily add a component to the main frame.
	 * @param cmp - The component to add.
	 * @param x - The gridbag location, x.
	 * @param y - The gridbag location, y.
	 * @param gridmod - The gridbag gridwidth or gridheight; it is gridwidth if `setWidth` is true; otherwise it is gridheight.
	 * @param setWidth - Determines whether gridmod value will be used for gridwidth or gridheight.
	 */
	private static void addToFrame(Component cmp, int x, int y, int gridmod, boolean setWidth) {
		constraints.gridx = x;
		constraints.gridy = y;
		if (setWidth) {
			constraints.gridwidth = gridmod;
		} else {
			constraints.gridheight = gridmod;
		}
		mainFrame.add(cmp, constraints);
		constraints.gridheight = constraints.gridwidth = 1;
	}
	/**
	 * A utility function to easily add a component to a JPanel.
	 * @param panel - The panel to add a component to.
	 * @param cmp - The component to add.
	 * @param x - The gridbag location, x.
	 * @param y - The gridbag location, y.
	 * @param gridmod - The gridbag gridwidth or gridheight; it is gridwidth if `setWidth` is true; otherwise it is gridheight.
	 * @param setWidth - Determines whether gridmod value will be used for gridwidth or gridheight.
	 */
	private static void addToPanel(JPanel panel, Component cmp, int x, int y, int gridmod, boolean setWidth ) {
		constraints.gridx = x;
		constraints.gridy = y;
		if (setWidth) {
			constraints.gridwidth = gridmod;
		} else {
			constraints.gridheight = gridmod;
		}
		panel.add(cmp, constraints);
		constraints.gridheight = constraints.gridwidth = 1;
		
	}
	
	/**
	 * Function that builds the calculator header, which is,
	 * the results panel.
	 */
	private static void buildCalcHeader() {
		final Font smallerFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
		final Font biggerFont = new Font(Font.SANS_SERIF, Font.BOLD, 30);
		final JPanel historyPanel = new JPanel();
		historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
		
		historyField.setEditable(false);
		currentHistoryField.setEditable(false);
		
		historyField.setFont(smallerFont);
		currentHistoryField.setFont(biggerFont);
		historyField.setBackground(BACKGROUND_GREEN_COLOR);
		currentHistoryField.setBackground(BACKGROUND_GREEN_COLOR);
		
		historyField.setAlignmentX(Component.CENTER_ALIGNMENT);
		currentHistoryField.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		historyPanel.add(historyField);
		historyPanel.add(currentHistoryField);
		updateView();
		addToFrame(historyPanel, 0, 0, 1, false);
	}
	
	/**
	 * Function that builds core buttons and adds it
	 * to the panel.
	 */
	private static void buildCoreButtons() {
		constraints.insets = new Insets(0, 5, 5, 5);
		CoreButton clearButton = new CoreButton("C");
		clearButton.registerFunction(new OperationFunc() {
			public void doOperation() {
				Main.clear();
			}
		});
		
		CoreButton equalButton = new CoreButton("=");
		equalButton.registerFunction(new OperationFunc() {
			public void doOperation() {
				Main.finalizeAnswer();
			}
		});
		CoreButton exitButton = new CoreButton("EXIT");
		exitButton.registerFunction(new OperationFunc() {
			public void doOperation() {
				Main.closeApp();
			}
		});
		addToPanel(topButtonPanel, clearButton, 6, 0, 1, false);
		addToPanel(topButtonPanel, equalButton, 3, 4, 3, true);
		addToPanel(bottomButtonPanel, exitButton, 3, 2, 3, true);
	}
	
	/**
	 * Function that builds operator buttons and adds it
	 * to the panel.
	 */
	private static void buildOperatorButtons() {
		constraints.insets = new Insets(0, 5, 5, 5);
		OperatorButton plusButton = new OperatorButton(Operator.ADDITION);
		OperatorButton minusButton = new OperatorButton(Operator.SUBTRACTION);
		OperatorButton multiButton = new OperatorButton(Operator.MULTIPLICATION);
		OperatorButton divisionButton = new OperatorButton(Operator.DIVISION);
		OperatorButton modButton = new OperatorButton(Operator.MOD);
		addToPanel(topButtonPanel, plusButton, 4, 3, 1, false);
		addToPanel(topButtonPanel, divisionButton, 5, 2, 1, false);
		addToPanel(topButtonPanel, multiButton, 4, 2, 1, false);
		addToPanel(topButtonPanel, minusButton, 5, 3, 1, false);
		addToPanel(topButtonPanel, modButton, 6, 4, 1, false);
	}
	
	/**
	 * Function that builds state buttons and adds it
	 * to the panel.
	 */
	private static void buildStateButtons() {
		StateButton plusMinusButton = new StateButton("\u00B1");
		plusMinusButton.registerFunction(new OperationFunc() {
			public void doOperation() {
				currentMathNode = new MathNode(currentMathNode.getNum() * -1);
				updateView();
			}
		});

		StateButton dotButton = new StateButton(".");
		dotButton.registerFunction(new OperationFunc() {
			public void doOperation() {
				// Get the current representation.
				currentMathNode = new MathNode(currentMathNode);
				if (currentMathNode.getPresentation().indexOf('.') == -1) {
					currentMathNode.setPresentation(currentMathNode.getPresentation() + '.');
				}
				updateView();
			}
		});
		addToPanel(topButtonPanel, plusMinusButton, 2, 5, 1, false);
		addToPanel(topButtonPanel, dotButton, 1, 5, 1, false);

	}
	
	/**
	 * This function adds operator buttons, core buttons, 
	 * numberic buttons, and state buttons such as 
	 * =, 1/n, 3, and '.'. These buttons are located at
	 * the middle panel; the one below the results panel.
	 */
	private static void buildTopButtonPanel() {
		constraints.insets = new Insets(0, 5, 5, 5);
		buildOperatorButtons();
		buildStateButtons();
		// 0 - 9 
		
		NumericButton[] numButtons = NumericButton.generateNumericButtons();
		for (byte i = 0, j = 5, z = 0; i < numButtons.length; i++) {
			if (i == 0) {
				addToPanel(topButtonPanel, numButtons[i], 0, j, 1, false);
				j--;
			} else {
				addToPanel(topButtonPanel, numButtons[i], z, j, 1, false);
				if (z == 2) {
					z = 0;
					j--;
				} else z++;
			}
		}

		ModifierButton squaredButton = new ModifierButton("x\u00B2");
		squaredButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.pow(val, 2);
			}

			public String getStringRepresentation(String val) {
				return "sqr( " + val + " )";
			}
		});

		ModifierButton cubedButton = new ModifierButton("x\u00B3");
		cubedButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.pow(val, 3);
			}

			public String getStringRepresentation(String val) {
				return "cube( " + val + " )";
			}
		});

		ModifierButton sqrtButton = new ModifierButton("\u221A");
		sqrtButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				if (val < 0) {
					emitError();
					return 0;
				}
				return Math.sqrt(val);
			}

			public String getStringRepresentation(String val) {
				return "sqrt( " + val + " )";
			}
		});

		ModifierButton percentButton = new ModifierButton("%");
		percentButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return lastMathNode != null ? ( val * lastMathNode.getNum()) / 100 : val / 100;
			}

			public String getStringRepresentation(String val) {
				return getCurrentMathNode().numToString();
			}
		});

		ModifierButton oneByNButton = new ModifierButton("1/n");
		oneByNButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				if (val < 0) {
					emitError();
					return 0;
				}
				return 1 / val;
			}

			public String getStringRepresentation(String val) {
				return "1/" + val;
			}
		});

		// 	}
		// });
		addToPanel(topButtonPanel, squaredButton, 6, 2, 1, false);
		addToPanel(topButtonPanel, cubedButton, 6, 3, 1, false);
		addToPanel(topButtonPanel, sqrtButton, 6, 5, 1, false);
		addToPanel(topButtonPanel, percentButton, 5, 5, 1, false);
		addToPanel(topButtonPanel, oneByNButton, 4, 5, 1, false);


		constraints.insets = new Insets(0, 0,0,0);
		addToFrame(topButtonPanel, 0, 1, 1, true);
		// 
		
	}

	/**
	 * This function adds all of the modifier buttons such as 
	 * sin, cos, and tan; these buttons are located at the
	 * bottom-most panel.
	 */
	private static void buildBottomButtonPanel() {
		constraints.insets = new Insets(0, 5, 5, 5);
		ModifierButton sinButton = new ModifierButton("sin");
		sinButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.sin(val);
			}

			public String getStringRepresentation(String val) {
				return "sin( " + val + " )";
			}
		});

		ModifierButton cosButton = new ModifierButton("cos");
		cosButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.cos(val);
			}

			public String getStringRepresentation(String val) {
				return "cos( " + val + " )";
			}
		});

		ModifierButton tanButton = new ModifierButton("tan");
		tanButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.tan(val);
			}

			public String getStringRepresentation(String val) {
				return "tan( " + val + " )";
			}
		});

		ModifierButton asinButton = new ModifierButton("asin");
		asinButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.asin(val);
			}

			public String getStringRepresentation(String val) {
				return "asin( " + val + " )";
			}
		});

		ModifierButton acosButton = new ModifierButton("acos");
		acosButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.acos(val);
			}

			public String getStringRepresentation(String val) {
				return "acos( " + val + " )";
			}
		});

		ModifierButton atanButton = new ModifierButton("atan");
		atanButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.atan(val);
			}

			public String getStringRepresentation(String val) {
				return "atan( " + val + " )";
			}
		});

		ModifierButton sinhButton = new ModifierButton("sinh");
		sinhButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.sinh(val);
			}

			public String getStringRepresentation(String val) {
				return "sinh( " + val + " )";
			}
		});

		ModifierButton coshButton = new ModifierButton("cosh");
		coshButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.cosh(val);
			}

			public String getStringRepresentation(String val) {
				return "cosh( " + val + " )";
			}
		});

		ModifierButton tanhButton = new ModifierButton("tanh");
		tanhButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.tanh(val);
			}

			public String getStringRepresentation(String val) {
				return "tanh( " + val + " )";
			}
		});

		ModifierButton logButton = new ModifierButton("log");
		logButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				if (val < 0) {
					emitError();
					return 0;
				}
				return Math.log10(val);
			}

			public String getStringRepresentation(String val) {
				return "log( " + val + " )";
			}
		});

		ModifierButton lnButton = new ModifierButton("ln");
		lnButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				if (val < 0) {
					emitError();
					return 0;
				}
				return Math.log(val);
			}

			public String getStringRepresentation(String val) {
				return "ln( " + val + " )";
			}
		});

		ModifierButton powerOfTenButton = new ModifierButton("10^n");
		powerOfTenButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.pow(10, val);
			}

			public String getStringRepresentation(String val) {
				return "10 ^ " + val;
			}
		});

		ModifierButton absButton = new ModifierButton("abs");
		absButton.registerMathFunction(new MathFunc() {
			public double calculate(double val) {
				return Math.abs(val);
			}

			public String getStringRepresentation(String val) {
				return "abs( " + val + " )";
			}
		});


		addToPanel(bottomButtonPanel, sinButton, 0, 0, 1, false);
		addToPanel(bottomButtonPanel, cosButton, 1, 0, 1, false);
		addToPanel(bottomButtonPanel, tanButton, 2, 0, 1, false);
		addToPanel(bottomButtonPanel, asinButton, 0, 1, 1, false);
		addToPanel(bottomButtonPanel, acosButton, 1, 1, 1, false);
		addToPanel(bottomButtonPanel, atanButton, 2, 1, 1, false);
		addToPanel(bottomButtonPanel, logButton, 3, 0, 1, false);
		addToPanel(bottomButtonPanel, lnButton, 4, 0, 1, false);
		addToPanel(bottomButtonPanel, powerOfTenButton, 3, 1, 1, false);
		addToPanel(bottomButtonPanel, absButton, 4, 1, 1, false);
		addToPanel(bottomButtonPanel, sinhButton, 0, 2, 1, false);
		addToPanel(bottomButtonPanel, coshButton, 1, 2, 1, false);
		addToPanel(bottomButtonPanel, tanhButton, 2, 2, 1, false);
		
		Insets prevInsets = constraints.insets;
		constraints.insets = new Insets(30, 0, 10, 0);

		addToFrame(bottomButtonPanel, 0, 2, 1, true);
		constraints.insets = prevInsets;
	}
	
	/**
	 * Builds the UI.
	 */
	private static void buildInterface() {
		constraints.fill = GridBagConstraints.BOTH;
		mainFrame.setLayout(new GridBagLayout());
		constraints.insets = new Insets(0, 5, 20, 5);
		constraints.weightx = .1;
		buildCalcHeader();
		buildTopButtonPanel();
		buildBottomButtonPanel();
		buildCoreButtons();
	}
	
	public static void main(String[] args) {
		buildOperatorMap();
		buildInterface();
		MathNode.initialize();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	/**
	 * Returns the last math node.
	 * @return the math node; may be null.
	 */
	public static MathNode getLastMathNode() {
		return lastMathNode;
	}
	
	/**
	 * Returns the current math node.
	 * @return the mathnode; never null.
	 */
	public static MathNode getCurrentMathNode() {
		return currentMathNode;
	}

	/**
	 * Returns the current math operation.
	 * @return an Operator enum.
	 */
	public static Operator getCurrentOperator() {
		return mathOp;
	}

	/**
	 * Returns whether or not the current node is also the same
	 * as the dummy node.
	 * @return true if it is, otherwise false.
	 */
	public static boolean isCurrentNodeDumb() {
		return currentMathNode == dummyNode;
	}

	/**
	 * Returns whether or not the calculator is accepting input.
	 * @return true if it is, otherwise false.
	 */
	public static boolean isAccepingInput() {
		return canAcceptInput;
	}

	/**
	 * Sets the dummyNode to node.
	 * @param node - the dummyNode to be set.
	 */
	public static void setDummyNode(MathNode node) {
		dummyNode = node;
	}

	/**
	 * Sets the current math operation to the parameter.
	 * @param op - the operator to set.
	 */
	public static void setCurrentOperator(Operator op) {
		mathOp = op;
	}
	
	/**
	 * This function adds a digit to the working value. This is only called
	 * through numeric buttons. If it can not accept input, then the digit
	 * is not appended.
	 * @param num - The number to append.
	 */
	public static void addToWorkingValue(Byte num) {
		if (!canAcceptInput) return;
		if (currentMathNode == dummyNode) {
			currentMathNode = new MathNode(num, num.toString());
		}
		else if (currentMathNode.canAddDigit()) {
			currentMathNode.addDigit(num);
		} else {
			clear();
			currentMathNode = new MathNode(num, num.toString());
		}
		currentHistoryField.setText(currentMathNode.toString());
		
	}
	
	/**
	 * This function does math from the previous math node and the current math node.
	 * If there is no previous math node, then it is simply set as the previous node.
	 * Otherwise, the previous node will be modified to apply the math and update its
	 * presentation.
	 * This function will emit errors.
	 * @param nextOp - The next operation to set after doing math.
	 */
	public static void doMath(Operator nextOp) {
		// If we have errored, do not accept input until user cleared.
		if (!canAcceptInput) {
			return;
		}
		// Format the number to show only the required decimal digits.
		currentMathNode.finalizePresentation();

		// If the current math operation is none, then simply move the working
		// number to the last math node which will no longer be edited.
		if (mathOp == Operator.NO_OP) {
			lastMathNode = new MathNode(currentMathNode);
			// Change the math operation to the given nextOp parameter.
			mathOp = nextOp;
			// Update the view for user to see.
			updateView();
			return;
		}
		
		try {
			MathNode mergedNode;
			// Do math based on the current math operation.
			// For all cases, an error will throw if the result will return NaN.
			// In some cases, such as mod and division, it will throw an error if divison by zero.
			switch (mathOp) {
				case MULTIPLICATION:
					if (Double.isNaN(lastMathNode.getNum() * currentMathNode.getNum())) {
						emitError();
						return;
					}
					mergedNode = MathNode.finalizeNode(lastMathNode, 
							currentMathNode, lastMathNode.getNum() * currentMathNode.getNum(), mathOp);
					lastMathNode = mergedNode;
					break;
				case DIVISION:
					if (currentMathNode.getNum() == 0 || Double.isNaN(lastMathNode.getNum() / currentMathNode.getNum())) {
						emitError();
						return;
					}
					mergedNode = MathNode.finalizeNode(lastMathNode, 
							currentMathNode, lastMathNode.getNum() / currentMathNode.getNum(), mathOp);
					lastMathNode = mergedNode;
					break;
				case ADDITION:
					if (Double.isNaN(lastMathNode.getNum() + currentMathNode.getNum())) {
						emitError();
						return;
					}
					mergedNode = MathNode.finalizeNode(lastMathNode, 
							currentMathNode, lastMathNode.getNum() + currentMathNode.getNum(), mathOp);
					lastMathNode = mergedNode;
					break;
				case SUBTRACTION:
					if (Double.isNaN(lastMathNode.getNum() - currentMathNode.getNum())) {
						emitError();
						return;
					}
					mergedNode = MathNode.finalizeNode(lastMathNode, 
							currentMathNode, lastMathNode.getNum() - currentMathNode.getNum(), mathOp);
					lastMathNode = mergedNode;
					break;
				case MOD:
					if (currentMathNode.getNum() == 0 || Double.isNaN(lastMathNode.getNum() % currentMathNode.getNum())) {
						emitError();
						return;
					}
					mergedNode = MathNode.finalizeNode(lastMathNode, 
							currentMathNode, Math.abs(lastMathNode.getNum() % currentMathNode.getNum()), mathOp);
					lastMathNode = mergedNode;
					break;
				default:
					lastMathNode = currentMathNode;	
			}
			// If everything when smoothly, create a new math node copying the last math node.
			// The presentation will be the result of the math operation if successful.
			currentMathNode = new MathNode(lastMathNode);
			currentMathNode.setPresentation(currentMathNode.numToString());
			mathOp = nextOp;
			
		} catch (Exception ex) {
			// An unexpected error occurred.
			emitError();
			return;
		}
		// Finally update the view for user to see.
		updateView();
		// Everything went smoothly so return true.
		return;
	}
	/**
	 * Updates the history and current history field for user.
	 */
	public static void updateView() {
		String workingString = "";
		if (lastMathNode != null) workingString += lastMathNode;
		if (mathOp == Operator.NO_OP) {
			historyField.setText(workingString);			
		} else {
			historyField.setText(workingString + " " + getCorrespondingOperator(mathOp) + " ");
		}
		currentHistoryField.setText(currentMathNode.toString());
	}
	
	/**
	 * Called when pressing the equal button.
	 */
	public static void finalizeAnswer() {
		if (currentMathNode == null) return;
		doMath(Operator.NO_OP);
		if (canAcceptInput) {
			currentHistoryField.setText(lastMathNode.numToString());
			currentMathNode = dummyNode = new MathNode(lastMathNode.getNum());
		}
		
	}
	
	/**
	 * Clears the state of the calculator. Fixes errors as well.
	 */
	public static void clear() {
		lastMathNode = null;
		currentMathNode = dummyNode = MathNode.ZERO_MATH_NODE;
		mathOp = Operator.NO_OP;
		canAcceptInput = true;
		historyField.setText("");
		currentHistoryField.setText("0");
		currentHistoryField.setBackground(BACKGROUND_GREEN_COLOR);
		
	}
	
	public static void closeApp() {
		mainFrame.dispose();
	}

	/**
	 * Initalizes the `operatorToStringMap` hashmap.
	 */
	private static void buildOperatorMap() {
		operatorToStringMap.put(Operator.ADDITION, "+");
		operatorToStringMap.put(Operator.SUBTRACTION, "-");
		operatorToStringMap.put(Operator.MULTIPLICATION, "\u00D7");
		operatorToStringMap.put(Operator.DIVISION, "\u00F7");
		operatorToStringMap.put(Operator.MOD, "mod");
		
	}
	
	/**
	 * Returns the string operator based on Operator enum.
	 * @param op - The operation.
	 * @return the operator's string representation.
	 */
	public static String getCorrespondingOperator(Operator op) {
		return operatorToStringMap.getOrDefault(op, null);
	}

	/**
	 * Returns the Operator enum based on the String given.
	 * @param op - The string operation.
	 * @return the operation.
	 */
	public static String getCorrespondingOperatorString(String op) {
		return operatorToStringMap.getOrDefault(op, null);
	}

	/**
	 * Sets the calculator to a state where it can no longer accept input
	 * and shows "ERROR" message along with a red background.
	 */
	public static void emitError() {
		canAcceptInput = false;
		currentHistoryField.setText("ERROR");
		currentHistoryField.setBackground(BACKGROUND_RED_COLOR);
	}
}
