package edu.spbu.matrix;

import java.io.*;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  private double[][] matrix;
  private int N;
  public int getN(){
    return N;
  }

  public DenseMatrix (double[][] matr){
    matrix = matr;
    N = matr.length;
  }

  public DenseMatrix(String fileName) throws IOException{
    BufferedReader input = new BufferedReader(new FileReader(fileName));
    String str = input.readLine();
    String[] str_1 = str.split(" ");
    N = str_1.length;
    double[] inMatrix = new double[N];
    matrix = new double[N][N];
    double var = 0;
    for (int i = 0; i < str_1.length; i++){
      var = Double.parseDouble(str_1[i]);
      inMatrix[i] = var;
    }
    for (int i = 0; i < str_1.length; i++) matrix[0][i] = inMatrix[i];
    for (int i = 1; i < str_1.length; i++){
      str = input.readLine();
      str_1 = str.split(" ");
      for (int j = 0; j < str_1.length; j++){
        var = Double.parseDouble(str_1[j]);
        inMatrix[j] = var;
      }
      for (int k = 0; k < str_1.length; k++) matrix[i][k] = inMatrix[k];
    }
  }
  //Полуение матрицы
  public double[][] getMatrix (){
    return matrix;
  }
  public int Nmatrix(){
    return N;
  }
  //Вывод матрицы
  public void print (){
    for (int i = 0; i < N; i++){
      for (int j = 0; j < N; j++){
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   *
   */
  @Override public Matrix mul(Matrix o)
  {
    double[][] result = new double[N][N];
    if (o instanceof DenseMatrix){
      DenseMatrix mat = (DenseMatrix) o;
      double[][] B = mat.getMatrix();
      for (int i = 0; i < N; i++){
        for (int j = 0; j < N; j++){
          result[i][j] = 0;
          for (int k = 0; k < N; k++){
            result[i][j] += matrix[i][k] * B[k][j];
          }
        }
      }
    }
    else{
      if (o instanceof SparseMatrix){
        SparseMatrix B = (SparseMatrix) o;
        SparseMatrix A = new SparseMatrix(this);
        return A.mul(B);
      }
    }
    DenseMatrix result_1 = new DenseMatrix(result);
    return result_1;
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  public int num = 0;
  public double[][] result_matrix;

  public class MullClass implements Runnable{
    private double[][] matr;
    public MullClass(double[][] A){
      matr = A;
    }
    @Override
    public void run(){
      if (num == 0){
        for(int i = 0; i < N/2; i++){
          for(int j = 0; j < N; j++){
            result_matrix[i][j] = 0;
            for (int k = 0; k < N; k++){
              result_matrix[i][j] += matrix[i][k] * matr[k][j];
            }
          }
        }
        num++;
      }
      else{
        for(int i = N/2; i < N; i++){
          for(int j = 0; j < N; j++){
            result_matrix[i][j] = 0;
            for (int k = 0; k < N; k++){
              result_matrix[i][j] += matrix[i][k] * matr[k][j];
            }
          }
        }
      }
    }
  }
  @Override public Matrix dmul(Matrix o) throws InterruptedException{
    if (o instanceof DenseMatrix){
      DenseMatrix B = (DenseMatrix) o;
      double[][] B_matr = B.getMatrix();
      result_matrix = new double[N][N];
      num = 0;
      MullClass mull = new MullClass(B_matr);
      Thread t1 = new Thread(mull);
      t1.start();
      Thread t2 = new Thread(mull);
      t2.start();
      t1.join();
      t2.join();

      DenseMatrix result = new DenseMatrix(result_matrix);
      return result;
    }
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    if (o instanceof DenseMatrix){
      DenseMatrix B = (DenseMatrix) o;
      double[][] Bmat = B.getMatrix();

      for (int i = 0; i < Bmat.length; i++){
        for (int j = 0; j < Bmat.length; j++){
          if (matrix[i][j] != Bmat[i][j]){
            return false;
          }
        }
      }
    return true;
    }
    else{
      if (o instanceof SparseMatrix){
        SparseMatrix B = (SparseMatrix) o;
        return B.equals(this);
      }
    }
    return false;
  }
}

