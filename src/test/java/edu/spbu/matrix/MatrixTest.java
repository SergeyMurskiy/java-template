package edu.spbu.matrix;


import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MatrixTest {
  @Test
  public void mulDD() throws Exception {
    Matrix A = new DenseMatrix("m1.txt");
    Matrix B = new DenseMatrix("m2.txt");
    Matrix D = new DenseMatrix("m3.txt");
    assertEquals(D, A.mul(B));
  }

  @Test
  public void mulSS() throws IOException {
    Matrix A = new SparseMatrix("m1.txt");
    Matrix B = new SparseMatrix("m2.txt");
    Matrix D = new SparseMatrix("m3.txt");
    assertEquals(D, A.mul(B));
  }

  @Test
  public void mulDS() throws IOException {
    Matrix A = new SparseMatrix("m1.txt");
    Matrix B = new SparseMatrix("m2.txt");
    Matrix D = new DenseMatrix("m3.txt");
    assertEquals(D, A.mul(B));
  }

  @Test
  public void mulSD() throws IOException {
    Matrix A = new SparseMatrix("m1.txt");
    Matrix B = new DenseMatrix("m2.txt");
    Matrix D = new DenseMatrix("m3.txt");
    assertEquals(D, A.mul(B));
  }

  @Test
  public void dmulDD() throws Exception {
    Matrix A = new DenseMatrix("m1.txt");
    Matrix B = new DenseMatrix("m2.txt");
    Matrix D = new DenseMatrix("m3.txt");
    assertEquals(D, A.dmul(B));
  }
}

