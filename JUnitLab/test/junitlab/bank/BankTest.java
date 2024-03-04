package junitlab.bank;

import org.junit.Assert;
import org.junit.Test;
import junitlab.bank.impl.FirstNationalBank;

public class BankTest {

	@Test
	public void testOpenAccount() throws AccountNotExistsException
	{
		FirstNationalBank fnb = new FirstNationalBank();
		String acc = fnb.openAccount();
		Assert.assertEquals(0, fnb.getBalance(acc));
	}
	
	@Test
	public void testUniqueAccount()
	{
		FirstNationalBank fnb = new FirstNationalBank();
		String acc1 = fnb.openAccount();
		String acc2 = fnb.openAccount();
		Assert.assertNotEquals(acc1, acc2);
	}
	
	@Test
	public void testInvalidAccount()
	{
		FirstNationalBank fnb = new FirstNationalBank();
		Boolean boo = false; 
		try 
		{
			fnb.getBalance("12345");
		}
		catch (AccountNotExistsException e)
		{
			boo = true;
		}
		
		Assert.assertTrue(boo);
	}
	
	@Test
	public void testDeposit() throws Exception
	{
		FirstNationalBank fnb = new FirstNationalBank();
		String acc = fnb.openAccount();
		fnb.deposit(acc, 2000);
		Assert.assertEquals(2000, fnb.getBalance(acc));
	}

}
