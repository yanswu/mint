package idv.mint.context;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;
import idv.mint.context.config.WebConfig;
import idv.mint.context.enums.EncodingType;

public class WebXmlDispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
	return new Class[] { AppConfig.class, JpaConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
	return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
	 return new String[]{"/"};  
    }
    
    @Override  
    protected Filter[] getServletFilters() {  
	// UTF-8
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter(EncodingType.UTF8.getValue());  
        characterEncodingFilter.setForceEncoding(true); 
        
        OpenEntityManagerInViewFilter entityManagerInViewFilter = new OpenEntityManagerInViewFilter();
        return new Filter[] {characterEncodingFilter,entityManagerInViewFilter};  
    }     
    
    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
	
        super.registerDispatcherServlet(servletContext);

    }
    
//    @Override
//    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
//
//        File uploadDirectory = ServiceConfiguration.CRM_STORAGE_UPLOADS_DIRECTORY;
//
//        MultipartConfigElement multipartConfigElement = 
//            new MultipartConfigElement(uploadDirectory.getAbsolutePath(),
//                maxUploadSizeInMb, maxUploadSizeInMb * 2, maxUploadSizeInMb / 2);
//
//        registration.setMultipartConfig(multipartConfigElement);
//
//    }

}
