using System;
using Microsoft.SharePoint;

namespace HelloWorld
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
			EventLog.log("exiting");
			
			MessageQueue mq = new MessageQueue("world");
			
			X509Store store = X509Store.load("world");
			
			SharePoint sp = new Microsoft.SharePoint.Instance();
			
			TransactionScope scope = new TransactionScope();
        }
    }
}