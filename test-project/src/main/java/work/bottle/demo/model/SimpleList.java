package work.bottle.demo.model;

import java.util.List;

public class SimpleList<T> {

    private int count;
    private List<T> list;

    public SimpleList() {
        this(0, null);
    }

    public SimpleList(int count, List<T> list) {
        this.count = count;
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
