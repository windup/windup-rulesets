using System;
using IBM.Data.DB2;
using Oracle.DataAccess;

namespace HelloDB
{
    class Program
    {
        static void Main(string[] args)
        {
			
			DB2.open("world");
			Oracle.DataAccess.open("world");
        }
    }
}