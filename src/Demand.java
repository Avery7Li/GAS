import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demand implements Comparable<Demand>, Serializable {
	private int demandId;
	private String name;
	private String category;
	private int amountNeeded;
	private String unit;
	private int priority; // range from 1 to 3 to represent the urgency.
	private int DemanderId;

	private static int currentDemandId = 1;
	private static final long serialVersionUID = 20190625050327L;

	public static void main(String[] args) {	
		Organization o3 = new Organization(1, "Union", 61, 3, "profitable");
		Organization o2 = new Organization(1, "Union", 96, 2, "profitable");
		Organization o1 = new Organization(1, "Union", 96, 1, "profitable");
		//demand attributes
		int demandId = 1;
		String name = "bread";
		String category = "Food";
		int amountNeeded = 850;
		String unit = "kg";
		int priority = 1;
		Demand d1 = new Demand(demandId, name, category, amountNeeded, unit, priority);
		
		
		
//		//supply attributes
		int supplyId2 = 701;
		String name2 = "bread";
		double amount2 =  100;
		String unit2 = "kg";
		int providerId2 = 501;
		UnprofitableSupply s2 = new UnprofitableSupply(supplyId2, name2, amount2, unit2, providerId2, 4);
		
		int supplyId3 = 702;
		String name3 = "bread";
		double amount3 =  200;
		String unit3 = "kg";
		int providerId3 = 502;
		UnprofitableSupply s3 = new UnprofitableSupply(supplyId3, name3, amount3, unit3, providerId3, 4);
//		
//		
//		s2.uplinkUnprofitableSupply();
//		s3.uplinkUnprofitableSupply();
//		
//		System.out.println("Finished uplinking unprofitable supplies");
		
//		s2.deductAmount(20);
//		s2.updateUnprofitableSupply();
//
//		s3.deductAmount(10);
//		s3.updateUnprofitableSupply();
//		System.out.println("Finished updating deducted amount");
		
		// query s2 to check amount
		s2.setAmount(100);
		s2.updateUnprofitableSupply();
		
		s3.setAmount(200);
		s3.updateUnprofitableSupply();
		
		d1.matchToSupply();
	}
	/*
	 * The map that matches a category to its corresponding priority.
	 */
	private static Map<String, Integer> priorityForCategoryMap = new HashMap<String, Integer>() {
		{
			put("Food", 1);
			put("Medical", 2);
			put("Rescue", 3);
			put("Epidemic Prevention", 4);
			put("Construction", 6);
		}
	};

	/**
	 * The constructor with the default priority.
	 * 
	 * @param name
	 * @param amount
	 * @param unit
	 */
	public Demand(int demandId, String name, String category, int amount, String unit) {
		super();
		this.demandId = demandId;
		this.name = name;
		this.category = category;
		this.amountNeeded = amount;
		this.unit = unit;
		this.priority = priorityForCategoryMap.get(category);
	}

	/**
	 * The constructor with user defined priority.
	 * 
	 * @param name
	 * @param amount
	 * @param unit
	 * @param priority
	 */
	public Demand(int demandId,String name, String category, int amount, String unit, int priority) {
		super();
		this.demandId = demandId;
		this.name = name;
		this.category = category;
		this.amountNeeded = amount;
		this.unit = unit;
		this.priority = priority;
	}

	/*
	 * Match this demand in the supply pools.
	 */
	public void matchToSupply() {
		SupplyManager supplyManager = new SupplyManager();

		// First match with the unprofitable supply pool
		List<Supply> unprofitableSupplyList = supplyManager.mapInUnprofitableSupplyPool(this.getName(),
				this.amountNeeded);
		double sum = supplyManager.getTotalAmount(unprofitableSupplyList);

		double amountStillNeeded = this.amountNeeded - sum;
		if (amountStillNeeded <= 0) {
			System.out.println("Unprofitable supply List:\n");
			System.out.println(unprofitableSupplyList);
			return;		
		}

		// Calculate the price needed to pay for the available resources
		// in the profitable supply pool.
		double price = supplyManager.calculatePriceInProfitableSupplyPool(this.getName(), (int) amountStillNeeded);
		
		// Map in the profitable supply pool with the fund.
		double fund = price < supplyManager.getTotalFund() ? price : supplyManager.getTotalFund();
		List<Supply> profitableSupplyList = supplyManager.mapInProfitableSupplyPool(
				this.getName(), (int) amountStillNeeded, fund);
		sum += supplyManager.getTotalAmount(profitableSupplyList);
		double fundUsed = supplyManager.getTotalPrice(profitableSupplyList);

		// Deduct the fund actually used in the unprofitable supply pool.
		List<Supply> fundList = supplyManager.mapInUnprofitableSupplyPool("Fund", fundUsed);
		if (fundUsed != supplyManager.getTotalAmount(fundList)) {
			throw new RuntimeException();
		}
		// TODO broadcast transaction to the chain. three lists & SUM & FUND USED
		List<Supply> totalList = new ArrayList<Supply>();
		totalList.addAll(unprofitableSupplyList);
		totalList.addAll(profitableSupplyList);
		totalList.addAll(fundList);
		
		System.out.println("Unprofitable supply List:\n");
		System.out.println(unprofitableSupplyList);
		
		System.out.println("Total price in profitable supply list is: " + price);
		System.out.println("Fund actually provided is " + fund );
		System.out.println("\n Fund List:");
		System.out.println(fundList);
		
		System.out.println("\n Profitable supply List:");
		System.out.println(profitableSupplyList);
		
		System.out.println("Actually gathered amount of resources:" + sum);
		
//		if (sum < this.amountNeeded) {
//			// TODO put this unmapped demand in the demand pool
//		}
	}

	@Override
	public int compareTo(Demand other) {
		if (this.priority < other.priority) {
			return 1;
		} else if (this.priority > other.priority) {
			return -1;
		}
		return 0;
	}

	// The getters and setters
	public int getDemandId() {
		return demandId;
	}

	public void setDemandId(int demandId) {
		this.demandId = demandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amountNeeded;
	}

	public void setAmount(int amount) {
		this.amountNeeded = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
