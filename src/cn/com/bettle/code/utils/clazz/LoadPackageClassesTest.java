package cn.com.bettle.code.utils.clazz;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.com.bettle.code.annotation.BettleContextListener;

public class LoadPackageClassesTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetClassSet() {
		String[] st = {"cn.com.bettle.logic.listener"};
		LoadPackageClasses loadPackageClasses = new LoadPackageClasses(st,BettleContextListener.class);
		try {
			Set<Class<?>> classSet = loadPackageClasses.getClassSet();
			
			System.out.println(classSet.size());  
			
			Assert.assertEquals(1, classSet.size());
			
			for (Class cl : classSet) {  
			      System.out.println(cl.getName());  
			}  
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
