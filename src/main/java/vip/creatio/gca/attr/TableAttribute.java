package vip.creatio.gca.attr;

import vip.creatio.gca.Attribute;
import vip.creatio.gca.AttributeContainer;
import vip.creatio.gca.ClassFile;
import vip.creatio.gca.Serializer;
import vip.creatio.gca.constant.UTFConst;

import java.util.ArrayList;
import java.util.List;

public abstract class TableAttribute<T> extends Attribute {

    protected List<T> items = new ArrayList<>(4);

    protected TableAttribute(AttributeContainer container) {
        super(container);
    }

    public List<T> getTable() {
        return items;
    }

    public void remove(T item) {
        items.remove(item);
    }

    public void remove(int index) {
        items.remove(index);
    }

    public T get(int index) {
        return items.get(index);
    }

    public int indexOf(T item) {
        return items.indexOf(item);
    }

    public int size() {
        return items.size();
    }

    @Override
    public String toString() {
        return name() + "{" + items + "}";
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
