package vip.creatio.gca.type;

// representing a class info which may not exist.
public class TypeInfo implements Type {

    protected String name;

    public TypeInfo(String name) {
        this.name = name;
    }

    protected TypeInfo() {}

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    public static class Mutable extends TypeInfo {

        public Mutable(String name) {
            super(name);
        }

        public void setName(String name) {
            super.name = name;
        }

        @Override
        public boolean mutable() {
            return true;
        }
    }
}
