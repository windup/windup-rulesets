/*
 * JBoss, Home of Professional Open Source 
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors 
 * by the @authors tag. See the copyright.txt in the distribution for a 
 * full listing of individual contributors. 
 * 
 * This is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation; either version 2.1 of 
 * the License, or (at your option) any later version. 
 * 
 * This software is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * Lesser General Public License for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this software; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org. 
 */
package org.jboss.seam.resteasy; 
 
import org.jboss.resteasy.spi.ResourceFactory; 
import org.jboss.resteasy.spi.InjectorFactory; 
import org.jboss.resteasy.spi.HttpRequest; 
import org.jboss.resteasy.spi.HttpResponse; 
import org.jboss.resteasy.spi.ResteasyProviderFactory; 
import org.jboss.resteasy.core.PropertyInjectorImpl; 
 
import org.jboss.seam.contexts.Contexts; 
import org.jboss.seam.Component; 
import org.jboss.seam.log.Log; 
import org.jboss.seam.log.Logging; 
 
/**
 * Looks up Seam component in Seam contexts when a JAX-RS resource is requested. 
 * 
 * @author Christian Bauer 
 */ 
public class SeamResteasyResourceFactory implements ResourceFactory 
{ 
   Log log = Logging.getLog(SeamResteasyResourceFactory.class); 
 
   private final Class<?> resourceType; 
   private final org.hibernate.mapping.Component seamComponent; 
   private final ResteasyProviderFactory providerFactory; 
 
   public SeamResteasyResourceFactory(Class<?> resourceType, Component seamComponent, ResteasyProviderFactory providerFactory) 
   { 
      this.resourceType = resourceType; 
      this.seamComponent = seamComponent; 
      this.providerFactory = providerFactory; 
   } 
 
   public Class<?> getScannableClass() 
   { 
      return resourceType; 
   } 
 
   public void registered(InjectorFactory factory) 
   { 
      // Wrap the Resteasy PropertyInjectorImpl in a Seam interceptor (for @Context injection) 
      seamComponent.addInterceptor( 
            new ResteasyContextInjectionInterceptor( 
                  new PropertyInjectorImpl(getScannableClass(), providerFactory) 
            ) 
      ); 
 
      factory.createPropertyInjector(seamComponent.getClass(), factory);
 
   } 
 
   public Object createResource(HttpRequest request, HttpResponse response, InjectorFactory factory) 
   { 
      // Push this onto event context so we have it available in ResteasyContextInjectionInterceptor 
      Contexts.getEventContext().set(ResteasyContextInjectionInterceptor.RE_HTTP_REQUEST_VAR, request); 
      Contexts.getEventContext().set(ResteasyContextInjectionInterceptor.RE_HTTP_RESPONSE_VAR, response); 
      log.debug("creating RESTEasy resource instance by looking up Seam component: " + seamComponent.getName()); 
      return Component.getInstance(seamComponent.getName()); 
   } 
 
   public void requestFinished(HttpRequest request, HttpResponse response, Object resource) 
   { 
   } 
 
   public void unregistered() 
   { 
   } 
 
}