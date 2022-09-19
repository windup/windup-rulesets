import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloNumVarType;
import ilog.cplex.IloCplex.CplexEndedException


public class IlogExample {
   static class Data {
      int        nFoods;
      int        nNutrs;
      double[]   foodCost;
      double[]   foodMin;
      double[]   foodMax;
      double[]   nutrMin;
      double[]   nutrMax;
      double[][] nutrPerFood;

      Data(String filename) throws IloException, java.io.IOException,
                                   InputDataReader.InputDataReaderException {
         InputDataReader reader = new InputDataReader(filename);

         foodCost = reader.readDoubleArray();
         foodMin  = reader.readDoubleArray();
         foodMax  = reader.readDoubleArray();
         nutrMin  = reader.readDoubleArray();
         nutrMax  = reader.readDoubleArray();
         nutrPerFood = reader.readDoubleArrayArray();

         nFoods = foodMax.length;
         nNutrs = nutrMax.length;

         if ( nFoods != foodMin.length  ||
              nFoods != foodMax.length    )
            throw new IloException("inconsistent data in file " + filename);
         if ( nNutrs != nutrMin.length    ||
              nNutrs != nutrPerFood.length  )
            throw new IloException("inconsistent data in file " + filename);
         for (int i = 0; i < nNutrs; ++i) {
            if ( nutrPerFood[i].length != nFoods )
               throw new IloException("inconsistent data in file " + filename);
         }
      }
   }

   static void buildModelByRow(IloModeler    model,
                               Data          data,
                               IloNumVar[]   Buy,
                               IloNumVarType type) throws IloException {
      int nFoods = data.nFoods;
      int nNutrs = data.nNutrs;

      for (int j = 0; j < nFoods; j++) {
         Buy[j] = model.numVar(data.foodMin[j], data.foodMax[j], type);
      }
      model.addMinimize(model.scalProd(data.foodCost, Buy));

      for (int i = 0; i < nNutrs; i++) {
         model.addRange(data.nutrMin[i],
                        model.scalProd(data.nutrPerFood[i], Buy),
                        data.nutrMax[i]);
      }
   }

   static void buildModelByColumn(IloMPModeler  model,
                                  Data          data,
                                  IloNumVar[]   Buy,
                                  IloNumVarType type) throws IloException {
      int nFoods = data.nFoods;
      int nNutrs = data.nNutrs;

      IloObjective cost       = model.addMinimize();
      IloRange[]   constraint = new IloRange[nNutrs];

      for (int i = 0; i < nNutrs; i++) {
         constraint[i] = model.addRange(data.nutrMin[i], data.nutrMax[i]);
      }

      for (int j = 0; j < nFoods; j++) {
         IloColumn col = model.column(cost, data.foodCost[j]);
         for (int i = 0; i < nNutrs; i++) {
            col = col.and(model.column(constraint[i], data.nutrPerFood[i][j]));
         }
         Buy[j] = model.numVar(col, data.foodMin[j], data.foodMax[j], type);
      }
   }


   public static void main(String[] args) {

      try {
         String          filename  = "../../../examples/data/diet.dat";
         boolean         byColumn  = false;
         IloNumVarType   varType   = IloNumVarType.Float;

         for (int i = 0; i < args.length; i++) {
            if ( args[i].charAt(0) == '-') {
               switch (args[i].charAt(1)) {
               case 'c':
                  byColumn = true;
                  break;
               case 'i':
                  varType = IloNumVarType.Int;
                  break;
               default:
                  usage();
                  return;
               }
            }
            else {
               filename = args[i];
               break;
            }
         }

         Data data = new Data(filename);
         int nFoods = data.nFoods;
         int nNutrs = data.nNutrs;

         // Build model
         IloCplex     cplex = new IloCplex();
         IloNumVar[]  Buy   = new IloNumVar[nFoods];

         if ( byColumn ) buildModelByColumn(cplex, data, Buy, varType);
         else            buildModelByRow   (cplex, data, Buy, varType);

         // Solve model

         if ( cplex.solve() ) {
            System.out.println();
            System.out.println("Solution status = " + cplex.getStatus());
            System.out.println();
            System.out.println(" cost = " + cplex.getObjValue());
            for (int i = 0; i < nFoods; i++) {
               System.out.println(" Buy" + i + " = " + cplex.getValue(Buy[i]));
            }
            System.out.println();
         }
         cplex.end();
      }
      catch (IloException ex) {
         System.out.println("Concert Error: " + ex);
      }
      catch (InputDataReader.InputDataReaderException ex) {
         System.out.println("Data Error: " + ex);
      }
      catch (java.io.IOException ex) {
         System.out.println("IO Error: " + ex);
      }
   }

}
