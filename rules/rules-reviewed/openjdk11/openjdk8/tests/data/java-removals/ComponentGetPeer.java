import java.awt.*;

public class ComponentGetPeer {
    
    public static void main(String[] args) {
        java.awt.peer.FontPeer peer = new java.awt.Font("Sans", 1, 1).getPeer();
        String peerName = peer.toString();
        java.awt.Container cont = new java.awt.Container();
        Container c = new Container();
        int count = c.getComponentCount();
        if (count != 0) 
         {
            Component comp = c.getComponent(0);
            java.awt.ComponentPeer peer1 = comp.getPeer();
         }
   }
}