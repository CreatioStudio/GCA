package vip.creatio.gca.code;

public class Label {

    private OpCode anchor;
    private String name;

    public Label(String name, OpCode anchor) {
        this.name = name;
        this.anchor = anchor;
    }

    public OpCode getAnchor() {
        return anchor;
    }

    public void setAnchor(OpCode anchor) {
        this.anchor = anchor;
    }

    public int getOffset() {
        return anchor.getOffset();
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
}
