using System;
using System.Windows.Forms;
using System.Security.Principal.Windows;
using System.ServiceProcess;

internal static class HelloWindow
{
   private static void Main()
   {
         Form form = new Form();
		 form.show();
		 
		 WindowPrincipal p = Thread.CurrentPrincipal;
		 
		 RegistryKey test9999 = Registry.CurrentUser.CreateSubKey("Test9999");
		 
		 ServiceController sc = new ServiceController();
		 
		 ServiceBase[] ServicesToRun;
		 ServicesToRun = new ServiceBase[] { new Service1() };
		 ServiceBase.Run(ServicesToRun);
   }
} 