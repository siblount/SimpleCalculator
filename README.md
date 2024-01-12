# SimpleCalculator

This is a simple calculator application built in Java. The application uses Swing for the GUI and follows a modular approach for easy maintenance and scalability.

(C) Solomon Blount

This was a school project where we had to do the following:
 - Implement the basic functionality of the calcualtor (layout design, input text field, result text field, numbers 0 to 9, buttons.
 - Data buttons (which I call modifier buttons) such as Power Buttons (x^2, x^3), Mod, Log Buttons, etc
 - Trigonometry Buttons including sin, cos, tan, asin, acos, atan, sinh, cosh, tanh
 - A clear and exit button.

# ⚠️⚠️⚠️ WARNING! ⚠️⚠️⚠️
Absoultely do **NOT** use this to cheat on a calculator project similar to this. Doing so may result in an academic integrity violation. 
You may, however, use this as motivation and strategize a unique way of making your calculator and/or use PARTS of this code.

There is no license for this, meaning this is open domain with no legal restrictions but should you choose to use this as an example 
PLEASE include my name, Solomon Blount as the copyright holder.

## File Structure

The project is structured as follows:

- `Main.java`: This is the main entry point of the application. It sets up the GUI and handles the main logic of the calculator.

- `CoreButton.java`: This file defines the core functionality of a button in the calculator such as `C`, `=`, and `EXIT`.

- `MathNode.java`: This file defines the `MathNode` class, which is used to represent mathematical operations in the calculator.

- `ModifierButton.java`: This file defines the `ModifierButton` class, which is used to represent buttons that modify the state of the calculator such as `Mod`, `Sin`, `Cos`, `1/n` and more.

- `NumericButton.java`: This file defines the `NumericButton` class, which is used to represent numeric buttons in the calculator (0-9).

- `OperationFunc.java`: This file defines the `OperationFunc` class, which is used to represent mathematical operations in the calculator.

- `Operator.java`: This file defines the `Operator` class, which is used to represent mathematical operators in the calculator.

- `OperatorButton.java`: This file defines the `OperatorButton` class, which is used to represent operator buttons in the calculator such as `+`, `-`, `/`, `*`, and more.

- `StateButton.java`: This file defines the `StateButton` class, which is used to represent buttons that change the state of the calculator. The only state buttons are the `.` and the `+/-` which may change the visual representation of the working operation.

You may find where all of these buttons are dynamically defined [here](https://github.com/siblount/SimpleCalculator/blob/24ea9a2dc5a68e5d3bc39f9e987efbc246189c9e/CalculatorApp/src/Main.java#L121-L446).
## How to Run

To run the application, compile all the `.java` files and then run the `Main` class.

```shell
cd CalculatorApp\src
javac *.java
java Main
```

Please note that you need to have Java installed on your machine to run this application.
