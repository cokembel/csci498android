package csci498.cokembel.lunchlist;

public class Restaurant {
	
	private String name="";
	private String address="";
	private String type="";
	private String notes="";
	
	public String getName() {
		return(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return(address);
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getType() {
		return(type);
	}
	
	public String getNotes() {
		return(notes);
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString(){
		return(getName());
	}
	
}