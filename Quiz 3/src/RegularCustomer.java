public class RegularCustomer extends Customer {
    private int borrowCount;
    public RegularCustomer(String name, String address, String iD, String phone) {
        super(name, address, iD, phone);
    }

    @Override
    public boolean promote() {
        if (borrowCount > 3) return true;
        else return false;
    }

    @Override
    public boolean borrowVid(Video rental) {
        if (!super.borrowVid(rental)) return false;
        borrowCount++;
        return true;
    }
}
