/**
 *
 */
package com.massa.mrules.jbosstest.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.massa.mrules.context.execute.IExecutionContext;
import com.massa.mrules.context.execute.MExecutionContext;
import com.massa.mrules.executable.IExecutionResult;
import com.massa.mrules.inject.MRulesInject;
import com.massa.mrules.jbosstest.model.User;
import com.massa.mrules.set.IMruleExecutionSet;

/**
 * Rest Service
 */
@Path("/simple")
public class SimpleModelService {
	@Inject
	@MRulesInject(
			uri = "TEST",
			configHolderImpl = "com.massa.mrules.builder.XmlRuleEngineConfigHolder",
			factoryImpl = "com.massa.mrules.factory.xml.RichXMLFactory",
			xmlFile = "hello.xml"
			)
	private IMruleExecutionSet set;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mrules/{firstName}/{lastName}")
	public String mrules(@PathParam("firstName") final String firstName, @PathParam("lastName") final String lastName) throws Exception {
		final IExecutionContext context = new MExecutionContext<User>(this.set, new User(firstName, lastName));
		final IExecutionResult res = context.execute();
		return res.getReturnedValue() == null ? "" : res.getReturnedValue().toString();
	}
}
