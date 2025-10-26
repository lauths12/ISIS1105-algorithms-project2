# 🧩 Project Part II – Alan Turing and the Box Machine

**Course:** ISIS 1105 – Design and Analysis of Algorithms
**Semester:** 2023-20
**Authors:**

* Juan David Ríos Nisperuza – 202215787
* Laura Julieth Carretero Serrano – 202214922

---

## 📘 Problem Description

While working on the Enigma code, Alan Turing faced a logical challenge:
Given a rectangular machine with **M rows** and **N columns**, each cell contains a box that can only be opened with a specific **key type** (there are **k** key types in total).
The goal is to determine whether it is possible to open all boxes (i.e., decode the hidden message) **without breaking the machine**, using a sequence of operations that each apply one key to a rectangular subregion.

Rules:

* Each operation applies one key type to a **rectangular submatrix**.
* Boxes that don’t match the key type remain closed.
* If a key is applied to a box that is already open, the machine breaks and the message is lost.
* Each key can be used **exactly once**.

The task is to decide if there exists a valid sequence of key applications, and if so, output the correct order and rectangular ranges for each key.

---

## 💡 Solution Overview

The algorithm proceeds in **three main stages**:

1. **Submatrix detection per key**
   Each unique key type is analyzed to determine the **smallest bounding rectangle** that includes all of its occurrences in the matrix.

2. **Dependency graph construction**
   If one key’s rectangle fully contains another key’s area, a **dependency** is formed — meaning one key must be applied before the other.

3. **Topological sorting**
   Using **Kahn’s Algorithm**, the dependency graph is checked:

   * If the graph contains a cycle → there is no valid order → output `NO SE PUEDE`.
   * Otherwise, the topological order provides the valid **sequence of key usages**.

---

## ⚙️ Code Structure

Main source file: `Main.java`

### Core Functions

| Method                           | Description                                                                                                       |
| -------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| `main()`                         | Reads all test cases from standard input and calls `solve()` for each case.                                       |
| `solve()`                        | Coordinates the main process: identifies submatrices, builds the dependency graph, and runs the topological sort. |
| `encontrarEsquinasSubmatrices()` | Finds the top-left and bottom-right corners for each key’s submatrix.                                             |
| `obtenerValoresEncerrados()`     | Builds the dependency graph by detecting which key areas are enclosed by others.                                  |
| `ordenamientoTopologico()`       | Implements **Kahn’s Algorithm** for topological sorting and cycle detection.                                      |

---

## 🧮 Complexity Analysis

| Type                 | Description                                                                        |
| -------------------- | ---------------------------------------------------------------------------------- |
| **Time Complexity**  | O(M × N + K + E), where *E* is the number of dependency edges (worst case O(M×N)). |
| **Space Complexity** | O(M × N + K²) due to the matrix and adjacency structures.                          |

The approach efficiently handles the given constraints:
2 ≤ M, N ≤ 1000 and 1 ≤ K ≤ 10⁵.

---

## 🧰 How to Run

### 🖥️ Compilation

```bash
javac Main.java
```

### ▶️ Execution

```bash
java Main < input.txt > output.txt
```

### 📥 Input Format

```
T
M N K
matrix of size MxN with values between 1 and K
...
```

### 📤 Output Format

* If the message can be decoded:

  ```
  key row_start row_end col_start col_end
  ```

  (one line per key, in the correct order)
* If it’s not possible:

  ```
  NO SE PUEDE
  ```

---

## 🧪 Example

### Input

```
2
4 5 5
2 2 2 2 1
2 3 5 5 1
2 3 5 5 1
2 3 4 4 5
4 5 4
2 2 2 2 1
2 3 4 4 1
2 3 4 4 1
2 3 1 3 4
```

### Output

```
2 1 4 1 4
3 2 4 2 2
5 2 4 3 5
1 1 3 5 5
4 4 4 3 4
NO SE PUEDE
```

---

## 🧠 Additional Scenarios

| Scenario                        | Implication                                                            | Required Adjustment                                                    |
| ------------------------------- | ---------------------------------------------------------------------- | ---------------------------------------------------------------------- |
| **1. A key can be used twice.** | The dependency graph may include multiple edges between the same keys. | Modify the topological algorithm to allow repeated operations.         |
| **2. L-shaped structures.**     | The bounding-box approach is insufficient for non-rectangular shapes.  | Represent key regions explicitly (e.g., as coordinate lists or masks). |

---
