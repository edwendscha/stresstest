package at.rvs.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestRestController {

	@Autowired
	StressTest stressTest;

	@RequestMapping(value = "/stresstestpost", method = RequestMethod.POST)
	public Boolean startStressPost(@RequestBody Testparameter parameter) throws Exception {
		stressTest.startStressTestPost(parameter.getAppname(), parameter.getZeitInMillisekunden(), parameter.getDelay(),
				parameter.getBody());
		return true;
	}
	@RequestMapping(value = "/stresstestget", method = RequestMethod.POST)
	public Boolean startStressGet(@RequestBody Testparameter parameter) throws Exception {
		stressTest.startStressTest(parameter.getAppname(), parameter.getZeitInMillisekunden(), parameter.getDelay(),
				parameter.getBody());
		return true;
	}
//
//	@RequestMapping(value = "/stresstest/{url}/{zeit}/{delay}", method = RequestMethod.GET)
//	public Boolean startStress(@PathVariable("url") String url, @PathVariable("zeit") long zeit,
//			@PathVariable("delay") long delay) throws Exception {
//		System.out.println(url);
//		stressTest.startStressTest(URLDecoder.decode(url, StandardCharsets.UTF_8.name()), zeit, delay);
//		return true;
//	}

}