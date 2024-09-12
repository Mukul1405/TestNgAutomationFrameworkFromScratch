package org.orangehrmautomation.tests;

import org.orangehrmautomation.base.Base;
import org.testng.annotations.Test;

public class Login extends Base {

    @Test
    public void failedLogin(){
        try {
            Thread.sleep(2000);
            System.out.println("Failed Login Method");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
