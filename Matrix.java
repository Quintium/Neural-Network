import java.util.function.*;
import java.util.Arrays;

// simple matrix class with basic operations
public class Matrix {
    public int rows;
    public int cols;
    public float[][] data;

    // constructor for empty matrix
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        data = new float[rows][cols];
    }

    // load 1d-matrix from array
    public Matrix(float[] arr) {
        this.rows = arr.length;
        this.cols = 1;

        data = new float[this.rows][this.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j == 0) {
                    data[i][j] = arr[i];
                } else {
                    data[i][j] = 0;
                }
            }
        }
    }

    // convert matrix to array
    public float[] toArray() {
        float[] result = new float[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i * cols + j] = data[i][j];
            }
        }

        return result;
    }

    // copy the matrix
    public Matrix copy() {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = data[i][j];
            }
        }
        return result;
    }

    // printing matrix
    public void print() {
        for (float[] row : data) {
            System.out.println(Arrays.toString(row));
        }

        System.out.println();
    }

    // check if two matrices have equal dimensions
    public static void checkDimensions(Matrix m1, Matrix m2) {
        if (m1.rows != m2.rows || m1.cols != m2.cols) {
            throw new RuntimeException("Dimensions of matrices aren't equal.");
        }
    }

    // mapping a function to each element
    public void map(Function<Float, Float> func) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = func.apply(data[i][j]);
            }
        }
    }

    // mapping a function to each element
    public static Matrix map(Matrix m, Function<Float, Float> func) {
        Matrix result = new Matrix(m.rows, m.cols);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                result.data[i][j] = func.apply(m.data[i][j]);
            }
        }
        return result;
    }

    // fill matrix
    public void fill(float n) {
        map(x -> n);
    }

    // static filling
    public Matrix fill(Matrix m, float n) {
        return Matrix.map(m, x -> n);
    }

    // scalar addition
    public void add(float n) {
        map(x -> x + n);
    }

    // scalar addition
    public static Matrix add(Matrix m, float n) {
        return Matrix.map(m, x -> x + n);
    }

    // scalar multiplication
    public void multiply(float n) {
        map(x -> x * n);
    }

    // scalar multiplication
    public static Matrix multiply(Matrix m, float n) {
        return Matrix.map(m, x -> x * n);
    }

    // element-wise addition
    public void add(Matrix m) {
        checkDimensions(this, m);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += m.data[i][j];
            }
        }
    }

    // element-wise addition
    public static Matrix add(Matrix m1, Matrix m2) {
        checkDimensions(m1, m2);

        Matrix result = new Matrix(m1.rows, m1.cols);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m2.cols; j++) {
                result.data[i][j] = m1.data[i][j] + m2.data[i][j];
            }
        }

        return result;
    }

    // element-wise subtraction
    public void subtract(Matrix m) {
        checkDimensions(this, m);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] -= m.data[i][j];
            }
        }
    }

    // element-wise subtraction
    public static Matrix subtract(Matrix m1, Matrix m2) {
        checkDimensions(m1, m2);

        Matrix result = new Matrix(m1.rows, m1.cols);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m2.cols; j++) {
                result.data[i][j] = m1.data[i][j] - m2.data[i][j];
            }
        }

        return result;
    }

    // element-wise multiplication
    public void elementMultiply(Matrix m) {
        checkDimensions(this, m);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= m.data[i][j];
            }
        }
    }

    // element-wise multiplication
    public static Matrix elementMultiply(Matrix m1, Matrix m2) {
        checkDimensions(m1, m2);

        Matrix result = new Matrix(m1.rows, m1.cols);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m2.cols; j++) {
                result.data[i][j] = m1.data[i][j] * m2.data[i][j];
            }
        }

        return result;
    }

    // element-wise division
    public void elementDivide(Matrix m) {
        checkDimensions(this, m);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] /= m.data[i][j];
            }
        }
    }

    // element-wise division
    public static Matrix elementDivide(Matrix m1, Matrix m2) {
        checkDimensions(m1, m2);

        Matrix result = new Matrix(m1.rows, m1.cols);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m2.cols; j++) {
                result.data[i][j] = m1.data[i][j] / m2.data[i][j];
            }
        }

        return result;
    }

    // matrix multiplication
    public static Matrix multiply(Matrix m1, Matrix m2) {
        if (m1.cols != m2.rows) {
            throw new RuntimeException("Matrix1 columns and Matrix2 rows aren't equal.");
        }

        Matrix result = new Matrix(m1.rows, m2.cols);

        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                // add up all products of m1's row i and m2's column j
                float sum = 0;
                for (int k = 0; k < m1.cols; k++) {
                    sum += m1.data[i][k] * m2.data[k][j];
                }

                result.data[i][j] = sum;
            }
        }

        return result;
    }

    // transposing a matrix
    public static Matrix transpose(Matrix m) {
        Matrix result = new Matrix(m.cols, m.rows);

        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                result.data[j][i] = m.data[i][j];
            }
        }

        return result;
    }

    // concatenate two matrices horizontally
    public static Matrix horzCat(Matrix m1, Matrix m2) {
        if (m1.rows != m2.rows) {
            throw new RuntimeException("Matrix1 rows and Matrix2 rows aren't equal.");
        }

        Matrix result = new Matrix(m1.rows, m1.cols + m2.cols);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m1.cols; j++) {
                result.data[i][j] = m1.data[i][j];
            }
            for (int j = 0; j < m2.cols; j++) {
                result.data[i][j + m1.cols] = m2.data[i][j];
            }
        }

        return result;
    }

    // concatenate two matrices vertically
    public static Matrix vertCat(Matrix m1, Matrix m2) {
        if (m1.cols != m2.cols) {
            throw new RuntimeException("Matrix1 columns and Matrix2 columns aren't equal.");
        }

        Matrix result = new Matrix(m1.rows + m2.rows, m1.cols);
        for (int j = 0; j < m1.cols; j++) {
            for (int i = 0; i < m1.rows; i++) {
                result.data[i][j] = m1.data[i][j];
            }
            for (int i = 0; i < m2.rows; i++) {
                result.data[i + m1.rows][j] = m2.data[i][j];
            }
        }
        
        return result;
    }

    // clip matrix horizontally
    public static Matrix horzClip(Matrix m, int a, int b) {
        Matrix result = new Matrix(m.rows, b - a);
        
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.data[i][j] = m.data[i][j + a];
            }
        }

        return result;
    }

    // clip matrix vertically
    public static Matrix vertClip(Matrix m, int a, int b) {
        Matrix result = new Matrix(b - a, m.cols);
        
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.data[i][j] = m.data[i + a][j];
            }
        }

        return result;
    }

    public boolean check(Function<Float, Boolean> func) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (func.apply(data[i][j])) {
                    return true;
                }
            }
        }

        return false;
    }
}