package com.andblomqdasberg.mooseinvasion.weapon;

/**
 * 	Class with data for every weapon
 * 	
 * 	@author Anders Blomqvist
 */
public class WeaponList {

	// Add new weapons from here
	public static WeaponPistol PISTOL = new WeaponPistol(0, 34, 100, 40, "Pistol", false);
	public static WeaponCarbine CARBINE = new WeaponCarbine(1, 34, 150, 7, "Carbine", false);
	public static WeaponUZI UZI = new WeaponUZI(2, 15, 150, 3, "UZI", false);
	public static WeaponMelee KNUCKLES = new WeaponMelee(3, 51, 0, 40, "Knuckles", true);

	/**
	 * 	Returns the weapon which corresponds to given name
	 * 
	 * 	@param name Name of the weapon
	 */
	public static AbstractWeapon getWeaponByName(String name) {
		name = name.toLowerCase();
		switch(name) {
    		case "pistol":
    			return PISTOL;
    		case "uzi":
    			return UZI;
    		case "carbine":
    			return CARBINE;
    		case "knuckles":
    			return KNUCKLES;
		}
		return null;
	}
}
