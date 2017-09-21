<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<f:view>
	<html>
		<head><title>ICEfaces: TimeZone Sample Application</title></head>
		<body>
			<h3>ICEfaces: TimeZone Sample Application</h3>  
			<h:form>
				<h:panelGrid columns="2">
					<h:outputText style="font-weight:600" value="Server Time Zone"/>
					<h:outputText style="font-weight:600" value="Time Zone Selected from Map"/>
					<h:outputText value="#{timeZoneBean.selectedTimeZoneName}"/>
					<h:outputText style="font-weight:800" value="#{timeZoneBean.serverTime}"/>
					<h:outputText style="font-weight:800" 
					              value="#{timeZoneBean.selectedTime}"/>
				</h:panelGrid>
						<h:commandButton id="map" image="images/map.jpg"
									actionListener="#{timeZoneBean.listen}" />
			</h:form>        
		</body>
	</html>
</f:view>
