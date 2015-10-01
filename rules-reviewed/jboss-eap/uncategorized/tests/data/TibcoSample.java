import java.util.HashMap;
import java.util.Iterator;
import com.tibco.expresso.providers.api.AbstractExpressoEvent;
import com.tibco.expresso.providers.api.AbstractExpressoProvider;
import amx_.SomeStub;

public class TimerProvider extends AbstractExpressoProvider{
    
//These are user defined fields, specific to the provider

private static final String TIMER_PROPS = "expresso.providers.timer";
public static HashMap<String, String> timerData = new HashMap<String, String>();
public static final String TIMER_INTERVAL = "expresso.providers.timer.interval_ms";
public static final String TIMER_MESSAGE = "expresso.providers.timer.message"

@Override
public String getName() 
{
    // TODO Auto-generated method stub
    // name of the provider should be returned here

    return "Timer";
}
@Override
public String getDescription() 
{
    // TODO Auto-generated method stub
    // description of the provider should be returned here.

    return "Provides Timer events";
}

// The getProviderEvents() method will be explained and overridden 
//in later parts once we create events using the AbstractExpressoEvent 
//class as it returns a map of events that the provider has to offer.

@Override
public HashMap<String, AbstractExpressoEvent> getProviderEvents() 
{
    return null;
}

//This method can be used to initialize and load data specific to the 
//provider on its start up. All the properties mentioned in the 
//pojoprovider.properties file are available to the “config”  
//object of every provider and can be retrieved as shown below. 
//For this provider, we are saving them in timerData HashMap to 
//use later on. 

@Override
public void init() 
{

//Set the provider colour if required. 
//This colour will reflect in all the event pods specific to this 
//provider in the mobile app. The default value of colour is taken as 
//white if not set by the provider. 
 
  setProviderColour(Colour.Grey)

        Iterator<String> timerKeys = config.getKeys(TIMER_PROPS);
        while(timerKeys.hasNext()) 
        {
             String key = timerKeys.next();
             timerData.put(key, config.getString(key));
        }
        
    }
}