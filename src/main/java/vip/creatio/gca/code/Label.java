package vip.creatio.gca.code;

public class Label {

    private OpCode anchor;
    private String name;

    public Label(String name, OpCode anchor) {
        this.name = name;
        this.anchor = anchor;
    }

    public Label(String name) {
        this.name = name;
    }

    public OpCode getAnchor() {
        return anchor;
    }

    public void setAnchor(OpCode anchor) {
        this.anchor = anchor;
    }

    public int offset() {
        return anchor.offset();
    }

    public int index() {
        return anchor.index();
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Label{name=" + name + ",anchor=" + anchor +  '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Label) {
            return ((Label) obj).name.equals(name)
                    && ((Label) obj).anchor.equals(anchor);
        }
        return false;
    }
}
