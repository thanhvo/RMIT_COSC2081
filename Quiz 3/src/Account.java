class Account

{
    private String accID;
    private String name;
    protected double balance;
    public Account(String accountID, String name, double balance)
    {
        accID = accountID;
        this.name = name;
        this.balance = balance;
    }
    public void deposit(double amount)
    {
        balance = balance + amount;
    }
    public boolean withdraw(double amount)
    {
        if (balance >= amount)
        {
            balance = balance - amount;
            return true;
        } else
            return false;
    }
    public double getBalance()
    {
        return balance;
    }
    public String toString()
    {
        return String.format("ID = %s, name = %s, Bal = %s", accID, name, balance);
    }
}


