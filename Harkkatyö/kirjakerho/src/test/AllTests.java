package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite kerho-ohjelmalle
 * @author vesal
 * @version 3.1.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    kerho.test.JasenTest.class,
    kerho.test.JasenetTest.class,
    kerho.test.KirjaTest.class,
    kerho.test.KerhoTest.class,
    kerho.test.LainaTest.class,
    kerho.test.LainatTest.class,
    kerho.test.KirjatTest.class
    })
public class AllTests {
 //
}