package org.example.dandd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

	@RequestMapping(value = {
			"/",
			"/{path:^(?!api$|index\\.html$|static$|.*\\.js$|.*\\.css$|.*\\.ico$|.*\\.png$|.*\\.jpg$|.*\\.jpeg$|.*\\.svg$|.*\\.woff$|.*\\.woff2$|.*\\.ttf$|.*\\.eot$).*$}",
			"/{path:^(?!api$|index\\.html$|static$|.*\\.js$|.*\\.css$|.*\\.ico$|.*\\.png$|.*\\.jpg$|.*\\.jpeg$|.*\\.svg$|.*\\.woff$|.*\\.woff2$|.*\\.ttf$|.*\\.eot$).*$}/**"
	})
	public String forwardToIndex() {
		return "forward:/index.html";
	}
}
