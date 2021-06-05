package vip.creatio.gca.type;

// representing a class info which may not exist.
public class TypeInfo implements Type {

    protected String name;

    public TypeInfo(String name) {
        this.name = name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTypeName() {
        return name;
    }
}
