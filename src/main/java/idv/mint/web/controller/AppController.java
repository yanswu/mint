package idv.mint.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import idv.mint.context.AppSettings;

@Controller("/")
public class AppController {

    @RequestMapping(value= {"/","/app"})
    public String indexPage() {
	
	return AppSettings.HomePage.getValue();
    }
    
}
