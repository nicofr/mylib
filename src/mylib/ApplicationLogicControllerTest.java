package mylib;

import org.junit.Assert;
import org.junit.Test;

public abstract class ApplicationLogicControllerTest
	<EX_TYPE extends Exception, RESULT_TYPE extends ControllerResult, CONTROLLERTYPE extends ApplicationLogicController<RESULT_TYPE, EX_TYPE>> {
	
	protected abstract boolean checkResult(RESULT_TYPE result);
	protected abstract ApplicationLogicController<RESULT_TYPE, EX_TYPE> getInstance();
	protected abstract boolean expectException();
	
	@Test
	public final void dotest() {
		if (! expectException())
			try {
				Assert.assertTrue(checkResult(getInstance().perform()));
			} catch (Exception e) {
				Assert.assertFalse(true);
			}
		else 
			try {
				getInstance().perform();
				Assert.assertFalse(true);
			} catch (Exception e) {
			}
	}
	
}
