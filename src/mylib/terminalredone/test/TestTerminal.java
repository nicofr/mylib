package mylib.terminalredone.test;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import mylib.terminalredone.TerminalApplication;
import mylib.terminalredone.exceptions.IllegalPerformException;
import mylib.terminalredone.exceptions.IllegalTerminalFunctionException;

public class TestTerminal {
	
	static TerminalApplication prepareTerminal() throws IllegalTerminalFunctionException {
		TerminalApplication.the().registerTerminalFunctions(Features.class);
		return TerminalApplication.the();
	}
	
	@Test
	public void test() {
		try {
			TerminalApplication app = prepareTerminal();
			try {
				System.out.println(app.perform(new String[] {"help"}));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e1) {
				Assert.fail();
			}
			try {
				Assert.assertEquals("done",app.perform(new String[] {"testnoparam"}));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
				Assert.fail();
			}
			try {
				Assert.assertEquals("echo",app.perform(new String[] {"testbase", "name=echo"}));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
				Assert.fail();
			}
			try {
				Assert.assertEquals("first",app.perform(new String[] {"testoptional", "notoptional=first"}));
				Assert.assertEquals("first second",app.perform(new String[] {"testoptional", "notoptional=first", "optional=second"}));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
				Assert.fail();
			}
			try {
				Assert.assertEquals("test1",app.perform(new String[] {"testarray", "arg0=test1"}));
				Assert.assertEquals("test1test2test3",app.perform(new String[] {"testarray", "arg0=test1,test2,test3"}));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
				Assert.fail();
			}
			try {
				Assert.assertEquals("test0test1test2test3test4",app.perform(new String[] {"testmoreparams", "arg0=test0", "arg1=test1" ,"arg2=test2,test3", "arg3=test4"}));
				Assert.assertEquals("test0test1test2test3test4test5",app.perform(new String[] {"testmoreparams" ,"arg1=test1" ,"arg2=test2,test3", "arg0=test0" ,"arg4=test5", "arg3=test4"}));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
				Assert.fail();
			}
			try {
				app.perform(new String[] {"testbase", "bla=bla"});
				Assert.fail();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
			}
			try {
				app.perform(new String[] {"testbase"});
				Assert.fail();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| IllegalPerformException e) {
			} 
		} catch (IllegalTerminalFunctionException e) {
			Assert.fail();
		}
		
	}
	

}
