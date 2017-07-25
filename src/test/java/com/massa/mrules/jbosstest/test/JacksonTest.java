/**
 *
 */
package com.massa.mrules.jbosstest.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.ws.rs.core.MediaType;

/**
 * Rest Service Test
 */
@RunWith(Arquillian.class)
@RunAsClient
public class JacksonTest {

	/**
	 * Create test war archive
	 */
	@Deployment(testable = false)
	public static Archive<?> createDeployment() throws FileNotFoundException, IOException {
		final File[] libsMRules = Maven.resolver()
				.loadPomFromFile("pom.xml")
				.resolve("com.massa.mrules:MRules:jar:jdk17:1.9.2")
				.withTransitivity()
				.asFile();
		
		//Prevents weld from yielding errors while inspecting classes
		final File[] libsMBeanUtils = Maven.resolver()
				.loadPomFromFile("pom.xml")
				.resolve("commons-beanutils:commons-beanutils:1.9.2")
				.withTransitivity()
				.asFile();
		
		WebArchive arch = ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsLibraries(libsMRules)
				.addAsLibraries(libsMBeanUtils)
				.addPackage("com.massa.mrules.jbosstest.rest")
				.addPackage("com.massa.mrules.jbosstest.model")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("hello.xml")
				;
		
		if (System.getProperty("mrules.licence.file") != null) {
			String licenceFile = System.getProperty("mrules.licence.file");
			System.out.println("Using licence file: " + licenceFile);
			return arch.addAsResource(new File(licenceFile), "mrules.licence");
		}
		return arch.addAsResource("mrules.licence");
	}

	@ArquillianResource
	private URL url;

	@Test @RunAsClient 
	public void testMrules() throws Exception {
		final ClientRequest req = new ClientRequest(this.url.toString() + "rest/simple/mrules/john/doe");
		final ClientResponse<String> resp = req.accept(MediaType.APPLICATION_JSON).get(String.class);
		final String result = resp.getEntity();
		Assert.assertEquals("Hello john doe, how are you today ?", result);
	}
}
