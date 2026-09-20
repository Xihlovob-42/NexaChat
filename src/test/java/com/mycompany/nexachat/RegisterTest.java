package com.mycompany.nexachat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {


@Test
public void testValidUsername() {
    Register user = new Register();
    user.setUsername("kyl_1");
    assertTrue(user.checkUserName());
}

@Test
public void testInvalidUsername() {
    Register user = new Register();
    user.setUsername("kyle !!!!!!");
    assertFalse(user.checkUserName());
}

@Test
public void testValidPassword() {
    Register user = new Register();
    user.setPassword("Ch&&sec@ke99!");
    assertTrue(user.checkPasswordComplexity());
}

@Test
public void testInvalidPassword() {
    Register user = new Register();
    user.setPassword("password");
    assertFalse(user.checkPasswordComplexity());
}

@Test
public void testValidCellPhoneNumber() {
    Register user = new Register();
    user.setCellPhoneNumber("+27896655321");
    assertTrue(user.checkCellPhoneNumber());
}

@Test
public void testInvalidCellPhoneNumber() {
    Register user = new Register();
    user.setCellPhoneNumber("08966553");
    assertFalse(user.checkCellPhoneNumber());
}

}
