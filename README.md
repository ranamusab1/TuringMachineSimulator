# TuringMachineSimulator

This is a Java-based Turing Machine Simulator with a stylish GUI. It copies binary strings (e.g., `01` to `01P01`) using an interactive tape visualization, built to demonstrate Turing Machine concepts in a fun way.

## Overview

This project simulates a Turing Machine that copies binary strings (0 and 1) on a tape, using a transition file (`Unary.txt`). It has a dark-themed GUI with a red "Run Simulation" button and tape cells (white with yellow head) to show real-time steps. The tape and output text update together — perfect for learning automata theory.

Developed by **[Your Name]** and **Azmeer** for a university project.

## Features

- Dark GUI with red button `RGB(255, 5, 5)` and lighter red hover `RGB(255, 50, 50)`
- Tape shows white cells, yellow head `RGB(255, 193, 7)` for current position
- Tape and text output sync with no delay
- Uses `Unary.txt` for transitions, accepts binary strings (e.g., `01`)
- Clear error messages for invalid inputs
- 400ms delay for smooth step visualization

## Prerequisites

- Java Development Kit (JDK) 8 or higher (**JDK 17 recommended**)
- Visual Studio Code or any Java IDE
- **Extension Pack for Java** for VS Code

## Installation

Clone the repository:

```bash
git clone https://github.com/[your-username]/TuringMachineSimulator.git
cd TuringMachineSimulator
`````

## Ensure directory structure:
```bash
TuringMachineSimulator/
├── README.md
├── TuringMachineProject/
│   ├── Main.java
│   ├── Unary.txt
`````

## Sample Unary.txt content:
```bash
String Copy Turing Machine
0 1
X Y P
B
5
0
2
0,X,R,1
1,Y,R,2
1
B,P,L,3
1
B,P,L,3
2
X,0,R,4
Y,1,R,4
0
`````

### Check JDK:
```bash
java -version
`````
Download JDK 17 from Adoptium if needed.

# Running the Simulator
Open TuringMachineSimulator in VS Code and make sure:

- Main.java is in TuringMachineProject/turing_machine/

- Main.java has: package turing_machine;

## Run via VS Code
- Click Run or press F5

# Run via Terminal
```bash
cd TuringMachineProject
javac turing_machine/Main.java
java turing_machine.Main
`````

## In the GUI
- Enter path: TuringMachineProject/Unary.txt

- Input a binary string (e.g., 01)

- Click the red button

- Watch tape cells and output update together

#Example Output
For input 01, the output may look like:
```bash
String Copy Turing Machine
Input symbols: 0 1
Blank symbol: B
Number of States: 5
Start State: 0
Tape: $BBBBBB01BBBB...
      ^q0
Tape: $BBBBBBX1BBBB...
       ^q1
...
Tape: $BBBBBB01P01BB...
        ^q4
Machine halted at state: q4
`````

- Tape shows white cells with yellow head

- Output shows step-by-step execution

# Troubleshooting
## Button Not Red
Check in Main.java:
```bash
runButton.setBackground(new Color(255, 5, 5));
`````
## Tape Not Showing
- Ensure Unary.txt is present in TuringMachineProject/

- Add debug print:

```bash
System.out.println("Tape cells rendered: " + Tape.length());
`````

## File Not Found
Use full path, e.g.:
```
TuringMachineProject/Unary.txt
`````
## Main Class Not Found
- Make sure Main.java is inside TuringMachineProject/turing_machine/

- File should have: package turing_machine;

Run again:

```bash
cd TuringMachineProject
javac turing_machine/Main.java
java turing_machine.Main
`````

Swing Errors
Check Java version:
```bash
java -version
`````
If needed, reinstall JDK 17.

Still stuck? Try IntelliJ IDEA or open an issue on GitHub.

# Credits
- Rana Muhammad Musab Akram: GUI designer 

- Azmeer Hassan Ammad: Lead developer, Collaborator

- Built for a university automata theory project

# License
Open-source under the MIT License.
Use, modify, and share freely.

# Happy Simulating 🚀
