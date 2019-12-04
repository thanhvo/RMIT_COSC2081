public class DriverTest
{
    public static void main(String args[])
    {
        CreditCardAccount account = new CreditCardAccount ("c1234", "Smith", 1000.0, 5.0, 5000.0);
        account.chargeInterest(); // interest charge for Jun
        account.chargeInterest(); // interest charge for July
        account.chargeInterest(); // interest charge for Aug
        System.out.println(account);
    }
}
