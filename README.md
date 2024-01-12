# SimpleCalculator

This is a simple calculator application built in Java. The application uses Swing for the GUI and follows a modular approach for easy maintenance and scalability.

## File Structure

The project is structured as follows:

- `Main.java`: This is the main entry point of the application. It sets up the GUI and handles the main logic of the calculator.

- `CoreButton.java`: This file defines the core functionality of a button in the calculator.

- `MathNode.java`: This file defines the `MathNode` class, which is used to represent mathematical operations in the calculator.

- `ModifierButton.java`: This file defines the `ModifierButton` class, which is used to represent buttons that modify the state of the calculator.

- `NumericButton.java`: This file defines the `NumericButton` class, which is used to represent numeric buttons in the calculator.

- `OperationFunc.java`: This file defines the `OperationFunc` class, which is used to represent mathematical operations in the calculator.

- `Operator.java`: This file defines the `Operator` class, which is used to represent mathematical operators in the calculator.

- `OperatorButton.java`: This file defines the `OperatorButton` class, which is used to represent operator buttons in the calculator.

- `StateButton.java`: This file defines the `StateButton` class, which is used to represent buttons that change the state of the calculator.

## How to Run

To run the application, compile all the `.java` files and then run the `Main` class.

```shell
cd CalculatorApp\src
javac *.java
java Main
```

Please note that you need to have Java installed on your machine to run this application.