package gameLogic.map;

import fvs.taxe.actor.Selectable;

public class Station {
    private String name;
    private IPositionable location;
    private Selectable actor;

    public Station(String name, IPositionable location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPositionable getLocation() {
        return location;
    }

    public void setLocation(IPositionable location) {
        this.location = location;
    }

    public void setActor(Selectable actor) {
        this.actor = actor;
    }

    public Selectable getActor() {
        return actor;
    }
    
    public String getAcronym(){
    	//Special cases
    	if (name.equals("York")){
    		return "YRK";
    	} else if (name.equals("London")){
    		return "LDN";
    	} else if (name.equals("Copenhagen")){
    		return "CPH";
    	} else {
    		//First 3 letter acronyms
    		return (name.substring(0,3)).toUpperCase();
    	}
    }

    public boolean equals(Object o) {
        //Allows stations to be compared to each other, to check if they are the same station
        if (o instanceof Station) {
            Station s = (Station) o;
            return getName().equals(s.getName()) &&
                    getLocation().getX() == s.getLocation().getX() &&
                    getLocation().getY() == s.getLocation().getY();
        } else {
            return false;
        }
    }

}
