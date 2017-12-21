package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//Разреженная матрица

public class SparseMatrix implements Matrix
{
  private int[] indexI;
  private int[] indexJ;
  private double[] value;
  private int N; //Размер мартрицы
  private int M; //Число не нулевых

  public SparseMatrix (DenseMatrix B){
    double[][] BMatrix = B.getMatrix();
    ArrayList<Integer> resultI = new ArrayList<>();
    ArrayList<Integer> resultJ = new ArrayList<>();
    ArrayList<Double> resultV = new ArrayList<>();
    for (int i = 0; i < B.Nmatrix(); i++){
      for (int j = 0; j < B.Nmatrix(); j++){
        if (BMatrix[i][j] != 0){
          resultI.add(i);
          resultJ.add(j);
          resultV.add(BMatrix[i][j]);
        }
      }
    }
    M = resultI.size();
    indexI = new int[M];
    indexJ = new int[M];
    value = new double[M];
    for (int i = 0; i < M; i++){
      indexI[i] = resultI.get(i);
    }
    for (int i = 0; i < M; i++){
      indexJ[i] = resultJ.get(i);
    }
    for (int i = 0; i < M; i++){
      value[i] = resultV.get(i);
    }
  }

  public SparseMatrix(ArrayList<Integer> a, ArrayList<Integer> b, ArrayList<Double> d){
    indexI = new int[a.size()];
    indexJ = new int[a.size()];
    value = new double[a.size()];
    M = a.size();
    for (int i = 0; i < M; i++){
      indexI[i] = a.get(i);
    }
    for (int i = 0; i < M; i++){
      indexJ[i] = b.get(i);
    }
    for (int i = 0; i < M; i++){
      value[i] = d.get(i);
    }
  }

  public SparseMatrix(String fileName) throws IOException {
    BufferedReader input = new BufferedReader(new FileReader(fileName));
    String str = input.readLine();
    String[] str_1 = str.split(" ");
    N = str_1.length;
    ArrayList<Integer> I = new ArrayList<>();
    ArrayList<Integer> J = new ArrayList<>();
    ArrayList<Double> V = new ArrayList<>();
    double var = 0;
    for(int i = 0; i < str_1.length; i++){
      for(int j = 0; j < str_1.length; j++){
        if (!str_1[j].equals("0.0")){
          var = Double.parseDouble(str_1[j]);
          I.add(i);
          J.add(j);
          V.add(var);
        }
      }
      if (i < N-1) {
        str = input.readLine();
        str_1 = str.split(" ");
      }
    }
    M = I.size();
    indexI = new int[M];
    for (int i = 0; i < M; i++){
      indexI[i] = I.get(i);
    }
    indexJ = new int[M];
    for (int i = 0; i < M; i++){
      indexJ[i] = J.get(i);
    }
    value = new double[M];
    for (int i = 0; i < M; i++){
      value[i] = V.get(i);
    }

  }

  public int Mvalue(){
    return M;
  }

  public void print(){
    for(int i = 0; i < M; i++) System.out.print(indexI[i] + " ");
    System.out.println();
    for(int i = 0; i < M; i++) System.out.print(indexJ[i] + " ");
    System.out.println();
    for(int i = 0; i < M; i++) System.out.print(value[i] + " ");
  }

  public int[] getIndexI(){
    return indexI;
  }

  public int[] getIndexJ(){
    return indexJ;
  }
  public double[] getValue(){
    return value;
  }
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o) {
    if (o instanceof SparseMatrix){
      SparseMatrix B = (SparseMatrix) o;
      int[] I_index = B.getIndexI();
      int[] J_index = B.getIndexJ();
      double[] Values = B.getValue();

      ArrayList<Integer> resultI = new ArrayList<>();
      ArrayList<Integer> resultJ = new ArrayList<>();
      ArrayList<Double> resultV = new ArrayList<>();
      ArrayList<Integer> tmp = new ArrayList<>();

      for (int i = 0; i < M; i++){
        for (int j = 0; j < B.Mvalue(); j++){
          if (indexJ[i] == I_index[j]) {
            tmp.clear();
            if (resultI.size() == 0) {
              resultI.add(indexI[i]);
              resultJ.add(J_index[j]);
              resultV.add(value[i] * Values[j]);
            } else {
              for (int k = 0; k < resultI.size(); k++) {
                if (resultI.get(k) == indexI[i]) {
                  tmp.add(k);
                }
              }
              if (tmp.size() == 0 ){
                resultI.add(indexI[i]);
                resultJ.add(J_index[j]);
                resultV.add(value[i] * Values[j]);
              }
              else{
                int count = 0;
                for (int k = 0; k < tmp.size(); k++) {
                  if (resultJ.get(tmp.get(k)) == J_index[j]) {
                    count++;
                    resultV.set(tmp.get(k), resultV.get(tmp.get(k)) + value[i] * Values[j]);
                  }
                }
                if (count == 0){
                  resultI.add(indexI[i]);
                  resultJ.add(J_index[j]);
                  resultV.add(value[i] * Values[j]);
                }
              }
            }
          }
        }
      }
      SparseMatrix result = new SparseMatrix(resultI, resultJ, resultV );
      return result;
    }
    else {
      if (o instanceof DenseMatrix){
        DenseMatrix B = (DenseMatrix) o;
        SparseMatrix A = new SparseMatrix(B);
        return this.mul(A);
      }
    }
    return null;
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    if (o instanceof SparseMatrix){
      SparseMatrix B = (SparseMatrix) o;
      if ((Arrays.equals(indexI, B.getIndexI())) && (Arrays.equals(indexJ, B.getIndexJ())) && (Arrays.equals(value, B.getValue()))){
        return true;
      }
      else return false;
    }
    else{
      if (o instanceof DenseMatrix){
        DenseMatrix B = (DenseMatrix) o;
        SparseMatrix A = new SparseMatrix(B);
        if ((Arrays.equals(indexI, A.getIndexI())) && (Arrays.equals(indexJ, A.getIndexJ())) && (Arrays.equals(value, A.getValue()))){
          return true;
        }
        else return false;
      }
    }
    return false;
  }
}
