import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PermutationIterable<T> implements Iterable<List<T>> {

    private List<T> base;
    private PermutationResolver<T> resolver;

    public PermutationIterable(List<T> base, PermutationResolver<T> resolver) {
        super();
        this.base = base;
        this.resolver = resolver;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new PermutationIterator<T>(base, resolver);
    }

    public static void main(String[] args) {
        List<Integer> l = Arrays.asList(1, 2, 3);

        PermutationIterable<Integer> permutationIterable1 = new PermutationIterable<Integer>(l, new IndicesWalker<Integer>(l.size()));

        for (List<Integer> permutation : permutationIterable1) {
            System.out.println(permutation);
        }
    }

}