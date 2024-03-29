
public class Supply implements Comparable<Supply>, Cloneable {
	private int supplyId;
	private String name;
	private double amount;
	private String unit;
	private int providerId;
	private int providerRank;
	
	public int getProviderRank() {
		return providerRank;
	}

	public void setProviderRank(int providerRank) {
		this.providerRank = providerRank;
	}

	public Supply() {
	};

	public Supply(int supplyId, String name, double amount, String unit, int providerId, int providerRank) {
		super();
		this.supplyId = supplyId;
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.providerId = providerId;
		this.providerRank = providerRank;
	}

	public int compareTo(Supply other) {
		if (this.getProviderRank() > other.getProviderRank()) {
			return -1; 
		} else if (this.getProviderRank() < other.getProviderRank()) {
			return 1;
		} else {
			if (this.getAmount() > other.getAmount()) {
				return -1;
			} else if (this.getAmount() < other.getAmount()) {
				return 1;
			}
			return 0;
		}
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public void setSupplyId(int supplyId) {
		this.supplyId = supplyId;
	}

	@Override
	public Object clone() {
		Supply s = null;
		try {
			s = (Supply) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return s;
	}
	
//	@Override
//	public String toString() {
//		return "Supply [supplyId=" + supplyId + ", name=" + name + ", amount=" + amount + 
//				", with provider rank" + Organization.getRankById(this.providerId) + "]";
//	}

	public int getSupplyId() {
		return supplyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void deductAmount(double amountToDeduct) {
		this.amount -= amountToDeduct;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
