import java.util.ArrayList;

public class VideoStore {
	private ArrayList<Customer> customerList;
	private ArrayList<Video> videoList;

	public VideoStore() {
		this.videoList = new ArrayList<Video>();
		this.customerList = new ArrayList<Customer>();
	}

	public Video getVideo(String ID) {
		for (int i = 0; i < this.videoList.size(); i++) {
			Video temp = this.videoList.get(i);
			if (temp.getID().equals(ID))
				return temp;
		}
		System.out.println("Video ID " + ID + " not found");
		return null;
	}
	
	public boolean addVideo(Video vid) {
		for (int i = 0; i < this.videoList.size(); i++) {
			Video temp = this.videoList.get(i);
			if (temp.getID().equals(vid.getID()))
				return false;
		}
		this.videoList.add(vid);
		return true;
	}
	
	public Customer getCustomer(String ID) {
		for (int i = 0; i < this.customerList.size(); i++) {
			Customer temp = this.customerList.get(i);
			if (temp.getID().equals(ID))
				return temp;
		}
		System.out.println("Customer " + ID + " not found");
		return null;
	}
	
	public boolean addCustomer(Customer cust) {
		for (int i = 0; i < this.customerList.size(); i++) {
			Customer temp = this.customerList.get(i);
			if (temp.getID().equals(cust.getID()))
				return false;
		}
		this.customerList.add(cust);
		return true;
	}

	public void promote(Customer cust) {
		cust = new VIPCustomer(cust.getName(), cust.getAddress(), cust.getID(), cust.getPhone());
	}
	
	public static void main(String[] args) {
		VideoStore myStore = new VideoStore();
		//create 3 video items
		Video vid = new Video("VD001", "Divergent", "Action", 1, false);
		myStore.addVideo(vid);
		vid = new Video("VD002", "Green Eggs and Ham", "Comedy", 1, false);
		myStore.addVideo(vid);
		vid = new Video("VD003", "Gone with the wind", "Drama", 2, false);
		myStore.addVideo(vid);
		
		//create 3 customers
		Customer cust = new VIPCustomer("Ngo Bao Chau", "12 Math Avenue", "VIP001", "0203050813");
		myStore.addCustomer(cust);
		cust = new GuestCustomer("Pham Nhat Vuong", "12 Money Road", "G002", "0399999999");
		myStore.addCustomer(cust);
		cust = new GuestCustomer("Nguyen Xuan Phuc", "12 Politics Street", "G003", "0311112222");
		myStore.addCustomer(cust);
		
		Customer guest1 = myStore.getCustomer("G002");
		guest1.borrowVid(myStore.getVideo("VD001"));
		guest1.borrowVid(myStore.getVideo("VD002"));
		guest1.borrowVid(myStore.getVideo("VD003"));

		guest1.returnVid(myStore.getVideo("VD001"));
		guest1.returnVid(myStore.getVideo("VD002"));
		
		Customer guest2 = myStore.getCustomer("VIP001");
		guest2.borrowVid(myStore.getVideo("VD001"));
		guest2.borrowVid(myStore.getVideo("VD002"));
		guest2.borrowVid(myStore.getVideo("VD003"));
		
		guest2.returnVid(myStore.getVideo("VD001"));
		guest2.returnVid(myStore.getVideo("VD002"));
		guest2.returnVid(myStore.getVideo("VD003"));

		// Create a new regular customer
		RegularCustomer regCustomer = new RegularCustomer("Nguyen Quang Hai", "1 Ba Dinh, Hanoi", "R001","091444444");
		myStore.addCustomer(regCustomer);

		// Regular customer borrows and returns a video 4 times, then is promoted to VIP Customer
		for (int i = 0; i < 4; i++) {
			regCustomer.borrowVid(myStore.getVideo("VD003"));
			regCustomer.returnVid(myStore.getVideo("VD003"));
		}
		if (regCustomer.promote()) myStore.promote(regCustomer);

		// A guest customer borrows a 2 day video
		guest2.borrowVid(myStore.getVideo("VD003"));
		// A VIP customer borrows a 2 day video
		myStore.getCustomer("VIP001").borrowVid(myStore.getVideo(("VD003")));

	}

}
