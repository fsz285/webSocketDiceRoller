package dieroll;

public class DieRoll {
    
    private Integer result;
    
    private Integer numSides;
    
    private String name;
    
    private Boolean privateRoll;

    public DieRoll(String name, Integer numSides, Integer result, Boolean isPrivateRoll) {
        this.result = result;
        this.numSides = numSides;
        this.name = name;
        this.privateRoll = isPrivateRoll;
    }
    
    public Boolean getPrivateRoll() {
    	return privateRoll;
    }
    
    public Integer getResult() {
		return result;
	}

    public Integer getNumSides() {
		return numSides;
	}
    
    public String getName() {
		return name;
	}

}
