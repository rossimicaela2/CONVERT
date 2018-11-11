package ar.workInHouse.DocumentTranslate;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import ar.workInHouse.DocumentTranslate.resource.FileResource;
import ar.workInHouse.DocumentTranslate.resource.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

 /*
  * Example by: https://github.com/juanpabloprado/dw-multipart
  */
public class DropwizardApplication extends Application<DropwizardConfig> {
 
    public static void main(String[] args) throws Exception {
        new DropwizardApplication().run(args);
    }

	@Override
	public void run(DropwizardConfig arg0, Environment environment) throws Exception {
		configureCors(environment);
		environment.jersey().register(new MessageResource());
		environment.jersey().register(new FileResource());
		
	}
	
	@Override
	public void initialize(Bootstrap<DropwizardConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
        bootstrap.addBundle(new ViewBundle());
	}
	
	private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
    }	
 
}