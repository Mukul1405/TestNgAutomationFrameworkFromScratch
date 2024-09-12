package org.orangehrmautomation.tests;

import org.orangehrmautomation.base.Base;
import org.testng.annotations.Test;

public class Login extends Base {

    @Test
    public void failedLogin(){
        try {
            String wrong_user= prop.getProperty("user");
            String wrong_password= prop.getProperty("pass");
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }
}
