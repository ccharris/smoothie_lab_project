package Smoothie;

public class Ingredient implements Food{


	String name;
	int quantity;
	boolean isOrganic;
	
	public Ingredient(String name, int quantity, boolean isOrganic ){
		this.name = name;
		this.quantity = quantity;
		this.isOrganic = isOrganic;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public boolean getOrganic() {
		return isOrganic; 
	}
	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void canEat() {
		System.out.println("Yes this " + name + " is edible");	
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setOrganic(boolean isOrganic) {
		this.isOrganic = isOrganic;
	}

	@Override
	public void getAllInfo() {
		System.out.println("This is a "+ name + ". There are " + quantity + " of them.");
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isOrganic ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + quantity;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		if (isOrganic != other.isOrganic)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ingredient [" + (name != null ? "name=" + name + ", " : "") + "quantity=" + quantity + ", isOrganic="
				+ isOrganic + "]";
	}


	
	
}
