package dieroll;

public class DieRoll {
    
    private Integer result;
    
    private Integer numSides;
    
    private String name;

    public DieRoll(String name, Integer numSides, Integer result) {
        this.result = result;
        this.numSides = numSides;
        this.name = name;
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
