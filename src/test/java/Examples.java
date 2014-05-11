import static org.junit.Assert.*;

import java.util.Observable;

import org.junit.Test;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;


public class Examples {
	


	public class CommandHelloWorld extends HystrixCommand<String> {
	
	    private final String name;
	
	    public CommandHelloWorld(String name) {
	        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
	        this.name = name;
	    }
	
	    @Override
	    protected String run() {
	        return "Hello " + name + "!";
	    }
	}
	


	@Test
	public void syncTest() {
		
		 assertEquals("Hello World!", new CommandHelloWorld("World").execute());
         assertEquals("Hello Bob!", new CommandHelloWorld("Bob").execute());
         
	    
	}

}
