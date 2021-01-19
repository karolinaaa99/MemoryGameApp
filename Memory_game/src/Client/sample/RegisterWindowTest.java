package Client.sample;

import org.junit.Assert;
import org.junit.Test;

//test jednostkowy metody checkPasswords z klasy RegisterWindow
public class RegisterWindowTest
{
    @Test
    public void check_passwords()
    {
        //metoda assertTrue sprawdza, czy przekazany argument to true
        Assert.assertTrue(RegisterWindow.checkPasswords("dominik", "dominik"));
    }
}