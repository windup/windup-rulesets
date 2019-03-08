/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
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
package org.picketlink.test.trust.tests;

import static org.picketlink.test.integration.util.PicketLinkConfigurationUtil.addKeyStoreAlias;
import static org.picketlink.test.integration.util.PicketLinkConfigurationUtil.addValidatingAlias;
import static org.picketlink.test.integration.util.TestUtil.getServerAddress;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient;
import org.picketlink.identity.federation.api.wstrust.WSTrustClient.SecurityInfo;
import org.picketlink.identity.federation.core.wstrust.WSTrustException;
import org.picketlink.identity.federation.core.wstrust.plugins.saml.SAMLUtil;
import org.picketlink.test.integration.util.MavenArtifactUtil;
import org.picketlink.test.integration.util.PicketLinkIntegrationTests;
import org.picketlink.test.integration.util.TestUtil;
import org.w3c.dom.Element;

/**
 * Base class for the PicketLink trust tests
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Apr 18, 2011
 */
@RunWith(PicketLinkIntegrationTests.class)
public abstract class TrustTestsBase {
    
    @Deployment(name = "picketlink-sts", testable = false)
    @TargetsContainer("jboss")
    public static WebArchive createSTSDeployment() throws GeneralSecurityException, IOException {
        WebArchive sts = MavenArtifactUtil.getQuickstartsMavenArchive("picketlink-sts");
        
        addValidatingAlias(sts, "/WEB-INF/classes/picketlink.xml", getServerAddress(), getServerAddress());
        addKeyStoreAlias(sts, "/WEB-INF/classes/sts_keystore.jks", "sts", "testpass", getServerAddress());
        
        return sts;
    }

    /**
     * Method gets a SAML assertion from the PicketLink STS
     * 
     * @param username username to send to STS
     * @param password password to send to STS
     * @return
     * @throws Exception
     */
    protected Element getAssertionFromSTS(String username, String password) throws Exception {
        // Step 1: Get a SAML2 Assertion Token from the STS
        WSTrustClient client = new WSTrustClient("PicketLinkSTS", "PicketLinkSTSPort", TestUtil.getTargetURL("/picketlink-sts/PicketLinkSTS"), new SecurityInfo(username,
                password));
        Element assertion = null;
        try {
            System.out.println("Invoking token service to get SAML assertion for " + username);
            assertion = client.issueToken(SAMLUtil.SAML2_TOKEN_TYPE);
            System.out.println("SAML assertion for " + username + " successfully obtained!");
        } catch (WSTrustException wse) {
            System.out.println("Unable to issue assertion: " + wse.getMessage());
            wse.printStackTrace();
            System.exit(1);
        }
        return assertion;
    }
}