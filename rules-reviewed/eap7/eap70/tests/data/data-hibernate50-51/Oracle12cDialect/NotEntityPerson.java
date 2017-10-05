public class NotEntityPerson {
    private int id;
    private String name;
    private byte[] photo;
    private Byte[] foo;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Byte[] getFoo() {
        return foo;
    }

    public void setFoo(Byte[] foo) {
        this.foo = foo;
    }

}