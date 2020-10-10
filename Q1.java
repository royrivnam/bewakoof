import java.util.*;

/*
Time Complexity: O(m*n))
Auxiliary Space: O(n)
*/
class FindPairs {

    // Function to print all pairs whose sum is equal to given input value x
    public static void printAllPairs(int arr1[], int arr2[], int x) {
        int n = arr1.length;
        int m = arr2.length;

        // Creating hash map of all elements of the first array
        HashMap<Integer, List<Integer>> s = new HashMap<>();
        for (int i = 0; i < n; i++)
            if(!s.containsKey(arr1[i]))
                s.put(arr1[i], new ArrayList<>(Arrays.asList(i)));
            else
                s.get(arr1[i]).add(i);

        /*
        Subtract each of the second array elements from sum
        and check the result in previously created hashmap
        */
        System.out.println("Array_1_Position    Array_2_Position");
        for (int j = 0; j < m; j++)
            if (s.containsKey(x - arr2[j]))
            {
                for(int k=0; k<s.get(x - arr2[j]).size(); k++)
                {
                    System.out.println(s.get(x - arr2[j]).get(k) + "                     " + j);
                }
            }
    }

    // Test
    public static void main(String[] args) {
        int arr1[] = {1, 0, -4, 7, 6, 4};
        int arr2[] = {0, 2, 4, -3, 2, 1};
        int x = 8;
        printAllPairs(arr1, arr2, x);
    }
}