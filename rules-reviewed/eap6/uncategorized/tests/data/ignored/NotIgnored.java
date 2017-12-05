public class NotIgnored {
   public void notIgnored() {
	weblogic.jndi.Environment foo = null;
        foo.setProviderUrl("fakevalue");
        System.out.println("Foo: " + foo);
   }
}
