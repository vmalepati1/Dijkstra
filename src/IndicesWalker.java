import java.util.ArrayList;
import java.util.List;

public class IndicesWalker<T> implements PermutationResolver<T> {

    private int[] indices;

    public IndicesWalker(int elements) {
        indices = new int[elements];
        for (int i = 0; i < indices.length; ++i) {
            indices[i] = i;
        }
    }

    @Override
    public List<T> resolvePermutation(List<T> base) {
        List<T> newPermutation = new ArrayList<>(indices.length);

        for (int i : indices) {
            newPermutation.add(base.get(i));
        }

        return newPermutation;
    }

    @Override
    public boolean nextStep() {

        if (indices.length == 0) return false;

        int i = indices.length - 2;

        while (i >= 0 && indices[i] > indices[i + 1]) {
            --i;
        }

        if (i == -1) {
            // No more new permutations.
            return false;
        }

        int j = i + 1;
        int min = indices[j];
        int minIndex = j;

        while (j < indices.length) {
            if (indices[i] < indices[j] && indices[j] < min) {
                min = indices[j];
                minIndex = j;
            }

            ++j;
        }

        swap(indices, i, minIndex);

        ++i;
        j = indices.length - 1;

        while (i < j) {
            swap(indices, i++, j--);
        }
        return true;
    }

    private void swap(int[] array, int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

}