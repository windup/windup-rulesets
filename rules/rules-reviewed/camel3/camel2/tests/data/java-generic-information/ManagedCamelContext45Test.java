package camel2.org.apache.camel.spring.boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;

/**
 * Container for exposing {@link org.apache.camel.Route} information
 * with route details as JSON. Route details are retrieved from JMX.
 */
public class RouteDetailsInfo extends RouteInfo {

    @JsonProperty("details")
    private RouteDetails routeDetails;

    public RouteDetailsInfo(final CamelContext camelContext, final Route route) {
        super(route);

        if (camelContext.getManagementStrategy().getManagementAgent() != null) {
            this.routeDetails = new RouteDetails(camelContext.getManagedRoute(route.getId(), ManagedRouteMBean.class));
        }
        context.getManagedProcessor(name, ManagedProcessorMBean.class);
        Map<String, String> row = new LinkedHashMap<>();
        row.put("name", camelContext.getName());
        row.put("state", camelContext.getStatus().name());
        row.put("uptime", camelContext.getUptime());
        if (camelContext.getManagedCamelContext() != null) {
            row.put("exchangesTotal", "" + camelContext.getManagedCamelContext().getExchangesTotal());
            row.put("exchangesInflight", "" + camelContext.getManagedCamelContext().getExchangesInflight());
            row.put("exchangesFailed", "" + camelContext.getManagedCamelContext().getExchangesFailed());
        } else {
            row.put("exchangesTotal", "0");
            row.put("exchangesInflight", "0");
            row.put("exchangesFailed", "0");
        }
    }

}
