package junitlab.bank;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import junitlab.bank.impl.FirstNationalBank;

public class BankTestFixture {

	FirstNationalBank fnb;
	String acc1, acc2;
	
	@Before
	public void setUp() throws AccountNotExistsException
	{
		fnb = new FirstNationalBank();
		acc1 = fnb.openAccount();
		acc2 = fnb.openAccount();
		
		fnb.deposit(acc1, 1500);
		fnb.deposit(acc2, 12000);
	}
	
	@Test
	public void testTransfer() throws Exception
	{
		fnb.transfer(acc2, acc1, 3456);
		Assert.assertEquals(4956, fnb.getBalance(acc1));
		Assert.assertEquals(8544, fnb.getBalance(acc2));
	}
	
	@Test
	public void testTransferWithoutEnoguhFunds() throws Exception
	{
		try 
		{
			fnb.transfer(acc1, acc2, 3456);
		}
		catch (NotEnoughFundsException e)
		{
			//empty
		}
	}

}
