package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IntSort {
  public static void merge(int[] result, int[] array1, int[] array2){
    int index1 = 0;
    int index2 = 0;
    for (int i = 0; i < result.length; i++){
      if (index1 == array1.length) result[i] = array2[index2++];
      else if (index2 == array2.length) result[i] = array1[index1++];
      else if (array1[index1] < array2[index2]) result[i] = array1[index1++];
      else result[i] = array2[index2++];
    }
  }
  public static void merge_sort(int[] array) {
      if (array.length > 1) {
          int half = array.length / 2;
          int[] l_array = Arrays.copyOfRange(array, 0, half);
          int[] r_array = Arrays.copyOfRange(array, half, array.length);

          merge_sort(l_array);
          merge_sort(r_array);
          merge(array, l_array, r_array);
      }
  }
  public static void sort (int array[]) {
      merge_sort(array);
  }

  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }
}
