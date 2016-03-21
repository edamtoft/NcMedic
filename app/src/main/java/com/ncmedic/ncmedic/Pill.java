package com.ncmedic.ncmedic;

public class Pill {
	private int pillID;
    private String activeIngredients;
    private String dosages;
    private boolean hasImage;
    private String imageLoc; 
    private String shapeCode;
    private String colorCode;
    private double size;
    private int score;
    private String imprint;
    private String deaSchedule;
    
    /*Instantiate pill class*/
    public Pill() {
    	activeIngredients = "";
    	dosages = "";
    	hasImage = false;
    	imageLoc = "";
    	size = 0;
    	score = 1;
    	shapeCode = "";
    	colorCode = "";
    	imprint = "";
    	deaSchedule = "";
    	
    }
    public boolean setImprint(String s) {
    	imprint = s;
    	return true;
    }
    public boolean setId(int i) {
        pillID = i;
        return true;
    }
    public boolean setActiveIngredients(String s) {
        activeIngredients = s;
        return true;
    }
    public boolean setDosages(String s) {
        dosages = s;
        return true;
    }
    public boolean setHasImage(boolean b) {
        hasImage = b;
        return true;
    }
    public boolean setImageLoc(String s){
    	imageLoc = s;
    	return true;
    }
    public boolean setColorCode(String s) {
        colorCode = s;
        return true;
    }
    public boolean setShapeCode(String s) {
        shapeCode = s;
        return true;
    }
    public boolean setSize(double d) {
    	if (d > 0) {
    		size = d;
    		return true;
    	}
    	return false;
    }
    public boolean setScore(int i) {
    	if (i > 0 && i <= 4) {
    		size = i;
    		return true;
    	}
    	return false;
    }
    public boolean setDeaSchedule(String s) {
    	deaSchedule = s;
    	return true;
    }
    public int getId() {
        return pillID;
    }
    public String getImprint() {
    	return imprint;
    }
    public String getActiveIngredients() {
        return activeIngredients;
    }
    public String getDosages() {
        return dosages;
    }
    public boolean getHasImage() {
        return hasImage;
    }
    public String getImageLoc() {
    	if (hasImage) {;
    		return imageLoc;
    	}
    	return null;
    }
    public String getColorCode() {
        return colorCode;
    }
    public String getShapeCode() {
        return shapeCode;
    }
    public double getSize() {
    	return size;
    }
    public int getScore() {
    	return score;
    }
    public String getDeaSchedule() {
    	return deaSchedule;
    }
    public static String translateColorCode(String s) {
    	String color = "Unknown";
    	if (s.equals("C48323")) {
    		color = "Black";
    	} else if (s.equals("C48333")) {
    		color = "Blue";
    	} else if (s.equals("C48332")) {
    		color = "Brown";
    	} else if (s.equals("C48324")) {
    		color = "Grey";
    	} else if (s.equals("C48329")) {
    		color = "Green";
    	} else if (s.equals("C48331")) {
    		color = "Orange";
    	} else if (s.equals("C48328")) {
    		color = "Pink";
    	} else if (s.equals("C48327")) {
    		color = "Purple";
    	} else if (s.equals("C48326")) {
    		color = "Red";
    	} else if (s.equals("C48334")) {
    		color = "Turquoise";
    	} else if (s.equals("C48325")) {
    		color = "White";
    	} else if (s.equals("C48330")) {
    		color = "Yellow";
    	}
		return color;
	}
}
