using System;

namespace HelloWCF
{
    class Program
    {
        static void Main(string[] args)
        {
			
			NetNamedPipeBinding netNamedPipeBinding = new NetNamedPipeBinding(NetNamedPipeSecurityMode.None);
			
			NetMsmqBinding netMsmqBinding = new NetMsmqBinding();
			
			NetTcpBinding netTcpBinding = new NetTcpBinding();
			
			
			WSHttpBinding wsHttpBinding = new WSHttpBinding();
			
			wsHttpBinding.Security.Mode = SecurityMode.Transport;
			
			WSDualHttpBinding wSDualHttpBinding = new WSDualHttpBinding();
			
			WSFederationHttpBinding wSFederationHttpBinding = new WSFederationHttpBinding();
			
			wSFederationHttpBinding.Security.Mode = SecurityMode.TransportWithMessageCredential;
			
			
        }
    }
}