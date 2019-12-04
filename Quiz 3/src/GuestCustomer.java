
public class GuestCustomer extends Customer {
	private final int max_borrow;
	
	public GuestCustomer(String name, String address, String iD, String phone) {
		super(name, address, iD, phone);
		this.max_borrow = 2;
	}

	public int getMax_borrow() {
		return max_borrow;
	}

	@Override
	public boolean borrowVid(Video rental) {
		if (this.getNumRentals() >= max_borrow) {
			System.out.println("Customer " + this.getName() + " has already borrowed 2 Video items");
			return false;
		}
		if (rental.getRental_type() == 2) {
			System.out.println("Guest customer can not borrow 2-day video items!");
			return false;
		}
		return super.borrowVid(rental);
	}
}
