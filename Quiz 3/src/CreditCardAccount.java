public class CreditCardAccount extends Account
{
    private double intRate;
    private double creditLimit;

    public CreditCardAccount(String ID, String name, double balance, double intRate, double creditLimit)
    {
        super(ID, name, balance);
        this.intRate = intRate;
        this.creditLimit = creditLimit;
    }
    public void chargeInterest() {
        balance += balance * intRate / 100.0;
    }
}
