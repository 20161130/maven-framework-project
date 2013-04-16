package com.fengjing.framework.spring.jax.ws.client;

/**
 * ��maven������ִ��mvn jaxws:wsimport ����client�˴���
 * @author scott
 *
 */
public class HelloWebServiceClient {

    public static void main(String[] args)
    {
        //To Call webservice you need its service object
        //then port to call exposed methods.
        
    	/**
         * HelloService hs = new HelloService();
         * Hello hello = hs.getHelloPort();
         * hello.sayHello("Sanumala");
         */
    	
        Hello hello = new HelloService().getHelloPort();
        System.out.println(hello.sayHello("Sanumala"));
        System.out.println(hello.sayHelloUsingSpring("Github"));
    }
}
